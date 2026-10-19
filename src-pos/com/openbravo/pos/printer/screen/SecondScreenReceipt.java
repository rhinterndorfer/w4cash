package com.openbravo.pos.printer.screen;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Shows a receipt bitmap on an additional (second) screen as a borderless,
 * full-screen customer display. The receipt is scaled to fit the whole screen
 * (AUTO, aspect ratio preserved) and centered. If no second screen is present
 * nothing is shown. The window is meant to be dismissed together with the
 * owning dialog (see DevicePrinterDialog).
 */
public class SecondScreenReceipt {

    private JFrame m_frame;
    private ReceiptSurface m_surface;
    private GraphicsDevice m_device;

    /** True when a second display is connected. */
    public static boolean hasSecondScreen() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length > 1;
    }

    /**
     * Shows the receipt on the second screen (if any). Safe to call repeatedly;
     * the frame is created once and reused, and updated with the new bitmap.
     */
    public void show(BufferedImage receipt) {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (devices.length < 2 || receipt == null || receipt.getWidth() <= 0 || receipt.getHeight() <= 0) {
            return;
        }
        m_device = devices[1];

        if (m_frame == null) {
            m_surface = new ReceiptSurface(receipt);
            m_frame = new JFrame();
            m_frame.setUndecorated(true);              // no title bar
            m_frame.setAlwaysOnTop(true);
            m_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            m_frame.setContentPane(m_surface);
            // Place the (borderless) frame on the second screen, covering it.
            m_frame.setBounds(m_device.getDefaultConfiguration().getBounds());
            m_frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    hide();
                }
            });
        } else {
            m_surface.setReceipt(receipt);
        }
        m_frame.setVisible(true);
    }

    public void hide() {
        if (m_frame != null) {
            m_frame.setVisible(false);
        }
    }

    public void dispose() {
        if (m_frame != null) {
            m_frame.dispose();
        }
        m_frame = null;
        m_surface = null;
        m_device = null;
    }

    /** Draws the receipt bitmap scaled to fit (AUTO), centered, on a white background. */
    private static class ReceiptSurface extends JComponent {
        private BufferedImage m_receipt;

        ReceiptSurface(BufferedImage receipt) {
            m_receipt = receipt;
            setOpaque(true);
            setBackground(Color.WHITE);
        }

        void setReceipt(BufferedImage receipt) {
            m_receipt = receipt;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (m_receipt == null) {
                return;
            }
            int aw = getWidth();
            int ah = getHeight();
            int rw = m_receipt.getWidth();
            int rh = m_receipt.getHeight();
            if (aw <= 0 || ah <= 0 || rw <= 0 || rh <= 0) {
                return;
            }

            double scale = Math.min(aw / (double) rw, ah / (double) rh); // AUTO
            int dw = Math.max(1, (int) Math.round(rw * scale));
            int dh = Math.max(1, (int) Math.round(rh * scale));
            int dx = (aw - dw) / 2;
            int dy = (ah - dh) / 2;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(m_receipt, dx, dy, dw, dh, null);
            g2.dispose();
        }
    }
}