package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Query extends GuiComponent {
	public static final int OUTLINE_COLOR = 0xed261f;
	public static final int TOP_COLOR = 0x000000;
	public static final int TEXT_COLOR = 0xFFFFFF;
	public static final int BACKGROUND_COLOR = 0xB3C9C6;
	protected Query(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
	}
	@Override 
	public void draw () {
		Rectangle bounds = getBoundingRectangle();
		Graphics g = getGraphics();
		g.setColor (new Color (TOP_COLOR));
		g.fillRect (0, 0, bounds.width, bounds.height - 18);
		g.setColor(new Color (BACKGROUND_COLOR));
		g.fillRect(0, bounds.height - 18, bounds.width, bounds.height - (bounds.height - 18));
		g.setColor (new Color (OUTLINE_COLOR));
		g.drawRect(3, bounds.height - 18, bounds.width - 8, (bounds.height - (bounds.height - 18)) -1 );
		g.setColor(new Color (TEXT_COLOR));
		g.drawString ("Query",25,10);
	}

}
