package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DisplayableTextElement extends DisplayableElement {
	
	private GuiComponent parent;
	private String message;
	private Color backgroundColor;
	
	public DisplayableTextElement (GuiComponent parent,String text) {
		super(parent);
		this.parent = parent;
		message = text;
		backgroundColor = new Color (0xA0A0A0);
	}
	
	public GuiComponent getParent () {
		return parent;
	}
	
	public void setBackgroundColor (Color c) {
		backgroundColor = c;
	}
	
	//this method doesen't work by the way
	@Override
	public void render (int x, int y) {
		//Do nothing boo
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
}
