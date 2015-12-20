package com.openbravo.pos.mant;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

import com.openbravo.pos.sales.restaurant.Place;

public class JPlaceButton extends JButton {

	/** If sets <b>TRUE</b> this component is draggable */
	private boolean draggable = true;
	/**
	 * 2D Point representing the coordinate where mouse is, relative parent
	 * container
	 */
	protected Point anchorPoint;
	/** Default mouse cursor for dragging action */
	protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	/**
	 * If sets <b>TRUE</b> when dragging component, it will be painted over each
	 * other (z-Buffer change)
	 */
	protected boolean overbearing = false;
	private Place m_Place;

	public JPlaceButton(Place place) {
		addDragListeners();
		setOpaque(false);
		setBackground(new Color(240, 240, 240));
		m_Place = place;
	}

	/**
	 * We have to define this method because a JComponent is a void box. So we
	 * have to define how it will be painted. We create a simple filled
	 * rectangle.
	 *
	 * @param g
	 *            Graphics object as canvas
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	/**
	 * Add Mouse Motion Listener with drag function
	 */
	private void addDragListeners() {
		/**
		 * This handle is a reference to THIS beacause in next Mouse Adapter
		 * "this" is not allowed
		 */
		final JPlaceButton handle = this;

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				m_Place.selectPlace();
			}

		});

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				anchorPoint = e.getPoint();
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				int anchorX = anchorPoint.x;
				int anchorY = anchorPoint.y;

				Point parentOnScreen = getParent().getLocationOnScreen();
				Point mouseOnScreen = e.getLocationOnScreen();
				Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX,
						mouseOnScreen.y - parentOnScreen.y - anchorY);
				setLocation(position);

				// Change Z-Buffer if it is "overbearing"
				if (overbearing) {
					getParent().setComponentZOrder(handle, 0);
					repaint();
				}

				for (int i = 0; i < getParent().getComponentCount(); i++) {
					if (e.getComponent() == getParent().getComponent(i)) {
						int x = getParent().getComponent(i).getX();
						int y = getParent().getComponent(i).getY();
						x = x - (x % 5);
						y = y - (y % 5);

						getParent().getComponent(i).setLocation(new Point(x, y));

						m_Place.setXYCoordinates(x, y);
					}
				}

			}

		});
	}

	/**
	 * Remove all Mouse Motion Listener. Freeze component.
	 */
	private void removeDragListeners() {
		for (MouseMotionListener listener : this.getMouseMotionListeners()) {
			removeMouseMotionListener(listener);
		}
		setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Get the value of draggable
	 *
	 * @return the value of draggable
	 */
	public boolean isDraggable() {
		return draggable;
	}

	/**
	 * Set the value of draggable
	 *
	 * @param draggable
	 *            new value of draggable
	 */
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
		if (draggable) {
			addDragListeners();
		} else {
			removeDragListeners();
		}

	}

	/**
	 * Get the value of draggingCursor
	 *
	 * @return the value of draggingCursor
	 */
	public Cursor getDraggingCursor() {
		return draggingCursor;
	}

	/**
	 * Set the value of draggingCursor
	 *
	 * @param draggingCursor
	 *            new value of draggingCursor
	 */
	public void setDraggingCursor(Cursor draggingCursor) {
		this.draggingCursor = draggingCursor;
	}

	/**
	 * Get the value of overbearing
	 *
	 * @return the value of overbearing
	 */
	public boolean isOverbearing() {
		return overbearing;
	}

	/**
	 * Set the value of overbearing
	 *
	 * @param overbearing
	 *            new value of overbearing
	 */
	public void setOverbearing(boolean overbearing) {
		this.overbearing = overbearing;
	}

}
