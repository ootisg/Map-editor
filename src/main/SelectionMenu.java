package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class SelectionMenu extends GuiComponent {
	
	public static final int BAR_SIZE = 16;
	public static final int TITLE_PADDING_BOTTOM = 2;
	public static final int BACKBUTTON_PADDING_LEFT = 2;
	public static final int BACKBUTTON_PADDING_BOTTOM = 0;
	
	private String title;
	
	protected SelectionMenu (Rectangle bounds, GuiComponent parent, String title) {
		super (bounds, parent);
		this.title = title;
	}
	
	protected SelectionMenu (Rectangle bounds, Gui gui, String title) {
		super (bounds, gui);
		this.title = title;
	}
	
	@Override
	public void render () {
		if (!isHidden ()) {
			Rectangle bounds = getBoundingRectangle ();
			Graphics g = getWindow ().getBuffer ();
			//Draw background
			g.setColor (new Color (0xC0C0C0));
			g.fillRect (bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
			//Draw outline and structure
			renderChildren ();
			g.setColor (new Color (0x000000));
			g.drawRect (bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
			g.drawLine (bounds.x, bounds.y + BAR_SIZE, bounds.x + bounds.width - 1, bounds.y + BAR_SIZE);
			//Draw title
			int titleWidth = g.getFontMetrics ().stringWidth (title);
			int titleXOffset = (bounds.width - titleWidth) / 2;
			g.drawString (title, bounds.x + titleXOffset, bounds.y + g.getFontMetrics ().getHeight () - TITLE_PADDING_BOTTOM);
		}
	}
}
