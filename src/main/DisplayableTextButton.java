package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DisplayableTextButton extends GuiComponent {
	
	
	private boolean pressed = false;
	
	private GuiComponent parent;
	private String message;
	private Color backgroundColor;
	
	
	public DisplayableTextButton(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		backgroundColor = new Color (0xA0A0A0);
		this.parent = parent;
	}
	
	
	
	public GuiComponent getParent () {
		return parent;
	}
	
	public void setBackgroundColor (Color c) {
		backgroundColor = c;
	}
	
	public void render (GuiComponent place, Rectangle reigon) {
		Graphics2D g = (Graphics2D)place.getGraphics ();
		g.setColor (backgroundColor);
		g.fillRect (reigon.x, reigon.y, reigon.width, reigon.height);
		g.setColor(new Color(0));
		g.drawString(message,1, 0 + reigon.y + 13);
	}
	public String getMessage () {
		return message;
	}
	
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public void mouseReleased (int x, int y, int button) {
		pressed = true;
	}
	
	public void reset () {
		pressed = false;
	}
	
	public boolean pressed () {
		return pressed;
	}

}
