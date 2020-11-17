package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class GuiComponent {
	
	private GuiComponent parent;
	private LinkedList<GuiComponent> children;
	
	private Gui gui;
	
	private Rectangle bounds;
	
	private BufferedImage componentRender;
	
	private boolean hidden;
	
	
	protected GuiComponent (Rectangle bounds, GuiComponent parent) {
		children = new LinkedList<GuiComponent> ();
		if (bounds != null) {
			this.bounds = new Rectangle (bounds);
		}
		this.parent = parent;
		this.gui = parent.gui;
		parent.addChild (this);
		componentRender = new BufferedImage (bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
	}
	
	protected GuiComponent (Rectangle bounds, Gui gui) {
		children = new LinkedList<GuiComponent> ();
		this.bounds = new Rectangle (bounds);
		this.parent = null;
		this.gui = gui;
	}
	
	private void mouseEvent (int x, int y, MouseEvent event) {
		if (!hidden) {
			int xOffset = getBoundingRectangle ().x - parent.getBoundingRectangle ().x;
			int yOffset = getBoundingRectangle ().y - parent.getBoundingRectangle ().y;
			int localX = x - xOffset;
			int localY = y - yOffset;
			if (getBoundingRectangle ().contains (getBoundingRectangle ().x + localX, getBoundingRectangle ().y + localY)) {
				doMouseEvent (localX, localY, event);
			}
		}
	}
	
	public void doMouseEvent (int x, int y, MouseEvent event) {
		if (!hidden) {
			Iterator<GuiComponent> iter = children.iterator ();
			while (iter.hasNext ()) {
				iter.next ().mouseEvent (x, y, event);
			}
			dispatchMouseEvent (x, y, event);
		}
	}
	
	public void doKeyEvent (KeyEvent event) {
		if (!hidden) {
			Iterator<GuiComponent> iter = children.iterator ();
			while (iter.hasNext ()) {
				iter.next ().doKeyEvent (event);
			}
			dispatchKeyEvent (event);
		}
	}
	
	public MainPanel getMainPanel () {
		GuiComponent working = this;
		while (!(working instanceof MainPanel) && (working instanceof GuiComponent)) {
			working = working.getParent ();
		}
		if (working instanceof MainPanel) {
			return (MainPanel)working;
		}
		return null;
	}
	public void dispatchMouseEvent (int x, int y, MouseEvent event) {
		if (!hidden) {
			switch (event.getID ()) {
				case MouseEvent.MOUSE_CLICKED:
					mouseClicked (x, y, event.getButton ());
					break;
				case MouseEvent.MOUSE_PRESSED:
					getGui ().setMouseDown (true);
					getGui ().setLastMouseButtonPressed (event.getButton ());
					mousePressed (x, y, event.getButton ());
					break;
				case MouseEvent.MOUSE_RELEASED:
					getGui ().setMouseDown (false);
					getGui ().setLastMouseButtonPressed (MouseEvent.NOBUTTON);
					mouseReleased (x, y, event.getButton ());
					break;
				case MouseEvent.MOUSE_DRAGGED:
					if (!mouseDown ()) {
						mousePressed (x, y, event.getButton ());
					}
					mouseDragged (x, y, lastMouseButtonPressed ());
					break;
				case MouseEvent.MOUSE_MOVED:
					mouseMoved (x, y);
					break;
				case MouseWheelEvent.MOUSE_WHEEL:
					mouseWheelMoved (x, y, ((MouseWheelEvent)(event)).getPreciseWheelRotation ());
			}
		}
	}
	
	public void dispatchKeyEvent (KeyEvent event) {
		if (!hidden) {
			switch (event.getID ()) {
				case KeyEvent.KEY_TYPED:
					keyTyped (event.getKeyChar ());
					break;
				case KeyEvent.KEY_PRESSED:
					keyPressed (event.getKeyCode ());
					if (this.getClass().getSimpleName().equals("MainPanel")) {
						getGui().pushKey(event.getKeyCode());
					}
					break;
				case KeyEvent.KEY_RELEASED:
					keyReleased (event.getKeyCode ());
					if (this.getClass().getSimpleName().equals("MainPanel")) {
						getGui().releaseKey(event.getKeyCode());
					}
					break;
			}
		}
	}
	public boolean keyDown (int key) {
		return getGui().keyDown(key);
	}
	public boolean keyHit (int key) {
		boolean working = getGui().keyPressed(key);
		getGui().keyUsed(key);
		return working;
	
	}
	public void mouseClicked (int x, int y, int button) {
		
	}
	
	public void mousePressed (int x, int y, int button) {
		
	}
	
	public void mouseReleased (int x, int y, int button) {
		
	}
	
	public void mouseDragged (int x, int y, int button) {
		
	}
	
	public void mouseMoved (int x, int y) {
		
	}
	
	public void mouseWheelMoved (int x, int y, double amount) {
		
	}
	
	public void keyTyped (char keyChar) {
		
	}
	
	public void keyPressed (int keyCode) {
		
	}
	
	public void keyReleased (int keyCode) {
		
	}
	
	public boolean mouseInside () {
		if (bounds.contains (getWindow ().getMouseX (), getWindow ().getMouseY ())) {
			return true;
		}
		return false;
	}
	public void frameEvent () {
		if (!hidden) {
			Iterator<GuiComponent> iter = children.iterator ();
			while (iter.hasNext ()) {
				iter.next ().frameEvent ();
			}
		}
	}
	
	public void render () {
		if (!hidden) {
			draw ();
			paintAndRefresh ();
			renderChildren ();
		}
	}
	
	protected void paintAndRefresh () {
		Rectangle bounds = getBoundingRectangle ();
		getWindow ().getBuffer ().drawImage (componentRender, bounds.x, bounds.y, null);
		componentRender = new BufferedImage (bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
	}
	
	protected void renderChildren () {
		Iterator<GuiComponent> iter = children.iterator ();
		while (iter.hasNext ()) {
			iter.next ().render ();
		}
	}
	
	public void draw () {
		
	}
	
	public void show () {
		hidden = false;
	}
	
	public void hide () {
		hidden = true;
	}
	
	public void setBoundingRectangle (Rectangle bounds) {
		this.bounds = new Rectangle (bounds);
	}
	
	public Rectangle getBoundingRectangle () {
		return bounds;
	}
	
	public Gui getGui () {
		return gui;
	}
	
	public GuiComponent getParent () {
		return parent;
	}
	
	public GameWindow getWindow () {
		return gui.getWindow ();
	}
	
	public void addChild (GuiComponent component) {
		children.add (component);
	}
	
	public Graphics getGraphics () {
		return componentRender.getGraphics ();
	}
	
	public boolean isHidden () {
		return hidden;
	}
	public boolean mouseDown () {
		return getGui ().mouseDown ();
	}
	
	public int lastMouseButtonPressed () {
		return getGui ().lastMouseButtonPressed ();
	}
}
