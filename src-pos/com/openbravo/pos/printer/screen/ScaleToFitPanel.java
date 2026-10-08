package com.openbravo.pos.printer.screen;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * Displays a single child scaled so that it fits the available area, either by
 * width (Fit.WIDTH), by height (Fit.HEIGHT) or by the smallest scale that fits
 * both dimensions (Fit.AUTO), keeping the aspect ratio and centering the result.
 *
 * Rendering is done in two steps for quality and correctness:
 *  1. The child (a real, live subcomponent, so Swing can lay out its whole
 *     JScrollPane / JViewport / ticket subtree) is painted once, at its natural
 *     1:1 size, into an off-screen bitmap. The layout is forced right before
 *     this so the first paint is never blank.
 *  2. That bitmap is scaled with bilinear interpolation and drawn centered.
 *     Scaling a bitmap (instead of scaling the live drawing) keeps text, bar
 *     codes and images crisp and artifact-free.
 */
public class ScaleToFitPanel extends JComponent {

    public enum Fit { WIDTH, HEIGHT, AUTO }

    private final JComponent m_child;
    private Fit m_fit;

    private BufferedImage m_snapshot;
    private Dimension m_snapSize = new Dimension(0, 0);

    public ScaleToFitPanel(JComponent child, Fit fit) {
        m_child = child;
        m_fit = fit;
        add(m_child);        // live child: reliable paint() + Swing lays out its subtree
        setOpaque(true);
    }

    public void setFit(Fit fit) {
        m_fit = fit;
        repaint();
    }

    /** Call after the ticket content changes so a fresh snapshot is captured. */
    public void refresh() {
        m_snapshot = null;
        m_snapSize = new Dimension(0, 0);
        repaint();
    }

    /**
     * Returns the ticket rendered at its natural 1:1 size, building the cached
     * snapshot first if needed. May return null while the ticket is empty.
     * Useful to reuse the same bitmap on an additional display.
     */
    public BufferedImage getSnapshot() {
        if (m_snapshot == null) {
            buildSnapshot();
        }
        return m_snapshot;
    }

    @Override
    public void doLayout() {
        // Keep the child at its natural (preferred) size, anchored at the origin.
        Dimension d = m_child.getPreferredSize();
        m_child.setBounds(0, 0, Math.max(d.width, 0), Math.max(d.height, 0));
    }

    // The child is painted (scaled) from paintComponent(); skip the default pass.
    @Override
    protected void paintChildren(Graphics g) {
        // intentionally empty
    }

    /** Render the child, at its natural 1:1 size, into a cached bitmap. */
    private void buildSnapshot() {
        Dimension pref = m_child.getPreferredSize();
        if (pref.width <= 0 || pref.height <= 0) {
            return;
        }

        // Force a top-down layout over the whole subtree so the scroll pane,
        // viewport and ticket are sized/positioned before we paint (avoids a
        // blank first capture).
        m_child.setSize(pref);
        forceLayout(m_child);

        BufferedImage img = new BufferedImage(pref.width, pref.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D ig = img.createGraphics();
        ig.setColor(getBackground());
        ig.fillRect(0, 0, pref.width, pref.height);
        m_child.paint(ig);
        ig.dispose();

        m_snapshot = img;
        m_snapSize = pref;
    }

    /** Recursively runs doLayout() top-down so nested containers are laid out. */
    private static void forceLayout(Container c) {
        c.doLayout();
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component ch = c.getComponent(i);
            if (ch instanceof Container) {
                forceLayout((Container) ch);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (m_snapshot == null) {
            buildSnapshot();
        }
        if (m_snapshot == null) {
            return;
        }

        int aw = getWidth();
        int ah = getHeight();
        if (aw <= 0 || ah <= 0) {
            return;
        }

        double sw = m_snapSize.getWidth();
        double sh = m_snapSize.getHeight();

        double scale;
        switch (m_fit) {
            case WIDTH:
                scale = aw / sw;
                break;
            case HEIGHT:
                scale = ah / sh;
                break;
            default:
                scale = Math.min(aw / sw, ah / sh);
                break;
        }

        int dw = Math.max(1, (int) Math.round(sw * scale));
        int dh = Math.max(1, (int) Math.round(sh * scale));
        int dx = (aw - dw) / 2;
        int dy = (ah - dh) / 2;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(m_snapshot, dx, dy, dw, dh, null);
        g2.dispose();
    }
}