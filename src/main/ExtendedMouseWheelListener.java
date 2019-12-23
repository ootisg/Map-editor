package main;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ExtendedMouseWheelListener implements MouseWheelListener {

	private MouseWheelEvent mouseWheelEvent;
	private GameWindow detectionWindow;
	private Gui callUi;
	
	public ExtendedMouseWheelListener (GameWindow detectionWindow, Gui callUi) {
		this.detectionWindow = detectionWindow;
		this.callUi = callUi;
	}
	
	@Override
	public void mouseWheelMoved (MouseWheelEvent event) {
		int[] coords = detectionWindow.getViewportCoords (event.getX (), event.getY ());
		callUi.doMouseEvent (coords [0], coords [1], event);
	}

}
