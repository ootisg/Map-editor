package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;

public class Query extends GuiComponent {
	public static final int OUTLINE_COLOR = 0xa50000;
	public static final int TOP_COLOR = 0x26B5B0;
	public static final int TEXT_COLOR = 0x000000;
	public static final int REGION_COLOR = 0xB3C9C6;
	public static final int BACKGROUND_COLOR = 0x000000;
	String value;
	Boolean finished = false;
	protected Query(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		value = "";
	}
	@Override 
	public void draw () {
		if (!mouseInside() && mouseDown()) {
			this.hide();
			this.finished = true;
		}
		Rectangle bounds = getBoundingRectangle();
		Graphics g = getGraphics();
		g.setColor (new Color (TOP_COLOR));
		g.fillRect (0, 0, bounds.width, bounds.height - 18);
		g.setColor(new Color (BACKGROUND_COLOR));
		g.fillRect(0, bounds.height - 18, bounds.width, (bounds.height - (bounds.height - 18)));
		g.setColor(new Color (REGION_COLOR));
		g.fillRect(3, bounds.height - 16, bounds.width - 8, (bounds.height - (bounds.height - 18)) -6 );
		g.setColor (new Color (OUTLINE_COLOR));
		g.drawRect(3, bounds.height - 16, bounds.width - 8, (bounds.height - (bounds.height - 18)) -6 );
		g.setColor(new Color (TEXT_COLOR));
		g.drawString ("Query",25,10);
		FontMetrics metrics = g.getFontMetrics();
		if (metrics.stringWidth(value) < bounds.width - 12) {
		g.drawString(value, 6, bounds.height - 6);
		} else {
			int amoutToMove = value.length();
			while (metrics.stringWidth(value.substring(value.length() -amoutToMove)) > bounds.width - 12 ) {
				amoutToMove = amoutToMove - 1;
			}
			g.drawString(value.substring(value.length() - amoutToMove), 6, bounds.height - 6);	
		}
		g.drawRect(0, 0, bounds.width -1, bounds.height -1);
		g.drawRect(0, bounds.height - 18, bounds.width, (bounds.height - (bounds.height - 18)));
	}
	@Override
	public void keyTyped (char keyChar) {
		if (!(keyChar == KeyEvent.VK_BACK_SPACE || keyChar == KeyEvent.VK_DELETE)) {
				addChar (keyChar);
		}
	}
	public String getContent () {
		return value;
	}
	
	private void addChar (char c) {
		value += c;
	}
	@Override
	public void keyPressed (int keyCode) {
			if (keyCode == KeyEvent.VK_ENTER) {
				this.hide();
				this.finished = true;
				
			} else if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
				//Backspace character
				if (value.length () > 0) {
					value = value.substring (0, value.length () - 1);
				}
			}
		}
	public String getValue () {
		return value;
		
	}
	public void start () {
		this.finished = false;
	}
	public void finish () {
		this.finished = true;
	}
	public boolean isFinished () {
		return finished;
	}

}
