package main;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public abstract class MovableSelectionRegion extends SelectionRegion {

	private int dragPreviousX;
	private int dragPreviousY;
	private double oldViewX;
	private double oldViewY;
	private boolean mousePressedLastFrame;
	
	protected static final double MOUSE_WHEEL_SENSITIVITY = .05;
	
	protected MovableSelectionRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
	}

	@Override
	public void mouseDragged (int x, int y, int button) {
		if (button == MouseEvent.BUTTON3) {
			double newViewX = Math.max (dragPreviousX - x + getViewX (), 0);
			double newViewY = Math.max (dragPreviousY - y + getViewY (), 0);
			setView (newViewX, newViewY);
			oldViewX = newViewX;
			oldViewY = newViewY;
			dragPreviousX = x;
			dragPreviousY = y;
		}
	}
	@Override 
	public void mousePressed (int x, int y, int button) {
		super.mousePressed(x, y, button);
		if (button == MouseEvent.BUTTON3) {
		dragPreviousX = x;
		dragPreviousY = y;
		}
	}
	@Override
	public void mouseReleased (int x, int y, int button) {
		mousePressedLastFrame = false;
	}
	
	@Override
	public void mouseWheelMoved (int x, int y, double amount) {
		setScaleWithAnchor (Math.pow (1 + MOUSE_WHEEL_SENSITIVITY, amount) * getScale (), x + getViewX (), x + getViewY ());
	}
	
}
