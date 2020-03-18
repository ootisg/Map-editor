package main;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

public class Gui {
	
	private GameWindow displayWindow;
	
	private MainPanel panel;
	
	private boolean mouseDown;
	private int lastButtonPressed;
	
	public Gui (GameWindow wind) {
		displayWindow = wind;
		panel = new MainPanel (new Rectangle (0, 0, 640, 480), this);
	}
	
	public void doMouseEvent (int x, int y, MouseEvent event) {
		panel.doMouseEvent (x, y, event);
	}
	
	public void doKeyEvent (KeyEvent event) {
		panel.doKeyEvent (event);
	}
	
	public void frameEvent () {
		panel.frameEvent ();
	}
	
	public void render () {
		panel.render ();
	}
	
	public GameWindow getWindow () {
		return displayWindow;
	}
	
	public void setMouseDown (boolean mouseDown) {
		this.mouseDown = mouseDown;
	}
	
	public void setLastMouseButtonPressed (int lastButtonPressed) {
		this.lastButtonPressed = lastButtonPressed;
	}
	
	public boolean mouseDown () {
		return mouseDown;
	}
	
	public int lastMouseButtonPressed () {
		return lastButtonPressed;
	}
	
}