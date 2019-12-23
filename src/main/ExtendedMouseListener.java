package main;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

public class ExtendedMouseListener implements MouseInputListener {
	public int mouseX;
	public int mouseY;
	private boolean isClicked;
	private MouseEvent mouseEvent;
	private GameWindow detectionWindow;
	private Gui callUi;
	public ExtendedMouseListener (GameWindow detectionWindow, Gui callUi) {
		this.detectionWindow = detectionWindow;
		this.callUi = callUi;
		this.mouseX = 0;
		this.mouseY = 0;
		this.isClicked = false;
	}
	@Override
	public void mouseClicked (MouseEvent event) {
		int[] coords = detectionWindow.getViewportCoords (event.getX (), event.getY ());
		mouseX = coords [0];
		mouseY = coords [1];
		callUi.doMouseEvent (coords [0], coords [1], event);
	}
	@Override
	public void mouseEntered (MouseEvent event) {
	}
	@Override
	public void mouseExited (MouseEvent event) {
	}
	@Override
	public void mousePressed (MouseEvent event) {
		int[] coords = detectionWindow.getViewportCoords (event.getX (), event.getY ());
		mouseX = coords [0];
		mouseY = coords [1];
		callUi.doMouseEvent (coords [0], coords [1], event);
	}
	@Override
	public void mouseReleased (MouseEvent event) {
		int[] coords = detectionWindow.getViewportCoords (event.getX (), event.getY ());
		mouseX = coords [0];
		mouseY = coords [1];
		callUi.doMouseEvent (coords [0], coords [1], event);
	}
	@Override
	public void mouseDragged (MouseEvent event) {
		int[] coords = detectionWindow.getViewportCoords (event.getX (), event.getY ());
		mouseX = coords [0];
		mouseY = coords [1];
		callUi.doMouseEvent (coords [0], coords [1], event);
	}
	@Override
	public void mouseMoved (MouseEvent event) {
		int[] coords = detectionWindow.getViewportCoords (event.getX (), event.getY ());
		mouseX = coords [0];
		mouseY = coords [1];
		callUi.doMouseEvent (coords [0], coords [1], event);
	}
}