package main;

import java.awt.Rectangle;

public class ScrollableSelectionRegion extends SelectionRegion {
	
	protected static final double MOUSE_WHEEL_SENSITIVITY = 4;
	
	protected ScrollableSelectionRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
	}
	
	@Override
	public void mouseWheelMoved (int x, int y, double amount) {
		setView (getViewX (), Math.min (getViewY () + amount * MOUSE_WHEEL_SENSITIVITY, getElements ().length * getElementHeight () * getScale () - getBoundingRectangle ().getHeight ()));
	}
}
