package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class DisplayBox extends GuiComponent {

	private String message;
	private Color bgcolor = new Color (0xA0A0A0);
	private Color textColor = new Color (0x000000);
	private Color outlineColor = new Color (0x000000);
	public static final int PADDING_TOP = 2;
	public static final int PADDING_BOTTOM = 2;
	public static final int PADDING_LEFT = 2;
	public static final int PADDING_RIGHT = 2;
	
	public DisplayBox (Rectangle bounds, String message, GuiComponent parent) {
		super (bounds, parent);
		this.message = message;
	}
	
	public void setMessage (String message) {
		this.message = message;
	}
	
	public void setBgColor (Color color) {
		this.bgcolor = color;
	}
	
	public void setTextColor (Color color) {
		this.textColor = color;
	}
	
	public void setOutlineColor (Color color) {
		this.outlineColor = color;
	}
	public void setX (int newX) {
		Rectangle bounds = getBoundingRectangle ();
		setBoundingRectangle (new Rectangle (newX, bounds.y, bounds.width,bounds.height));
	}
	public void setY (int newY) {
		Rectangle bounds = getBoundingRectangle ();
		setBoundingRectangle (new Rectangle (bounds.x, newY, bounds.width,bounds.height));
	}
	@Override
	public void draw () {
		Rectangle bounds = getBoundingRectangle ();
		Graphics2D g = (Graphics2D)getGraphics ();
		FontMetrics f = g.getFontMetrics ();
		Rectangle2D r = f.getStringBounds (message, g);
		String [] lines = message.split("/n");
		int boxWidth = 16;
		for (int i = 0; i < lines.length; i++) {
			int tempWidth = (int) f.getStringBounds (lines [i], g).getWidth ();
			if (tempWidth > boxWidth) {
				boxWidth = tempWidth;
			}
		}
		setBoundingRectangle (new Rectangle (bounds.x, bounds.y, boxWidth + PADDING_LEFT + PADDING_RIGHT, (int)((r.getHeight () *lines.length) + PADDING_TOP + PADDING_BOTTOM) ));
		g.setColor (bgcolor);
		g.fillRect (0, 0, bounds.width, bounds.height);
		g.setColor (outlineColor);
		g.drawRect (0, 0, bounds.width - 1, bounds.height - 1);
		g.setColor (textColor);
		for (int i = 0; i < lines.length; i++) {
		g.drawString (lines[i], PADDING_LEFT, PADDING_TOP + f.getAscent () + (i*(int)r.getHeight()));
		}
	}

}
