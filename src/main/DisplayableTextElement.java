package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DisplayableTextElement extends DisplayableElement {
	
	private GuiComponent parent;
	private String message;
	public DisplayableTextElement (GuiComponent parent,String text) {
		super(parent);
		this.parent = parent;
		message = text;
	}
	
	public GuiComponent getParent () {
		return parent;
	}
	//this method doesen't work by the way
	@Override
	public void render (int x, int y) {
		Graphics2D g = (Graphics2D) MainLoop.getWindow().getBuffer();
		g.setColor(new Color(0));
		g.drawString(message,x + 1,30);
	}
	public void render (GuiComponent place, Rectangle region) {
		Graphics2D g = (Graphics2D) MainLoop.getWindow().getBuffer();
		g.setColor(new Color(0));
		g.drawString(message,place.getBoundingRectangle().x + 1, place.getBoundingRectangle().y + region.y + 13);
	}
	public String getMessage () {
		return message;
	}
}
