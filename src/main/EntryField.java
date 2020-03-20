package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryField extends GuiComponent {

	public static final int OUTLINE_COLOR = 0x000000;
	public static final int FILL_COLOR = 0xC0C0C0;
	public static final int TEXT_COLOR = 0x000000;
	
	public static final int TEXT_PADDING_LEFT = 2;
	public static final int TEXT_PADDING_TOP = 0;
	
	boolean focus = false;
	
	int maxLength = 3;
	
	String defaultString = "0";
	String value = defaultString;
	String filter;
	
	Pattern filterPattern;
	
	public EntryField (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void draw () {
		//Really shouldn't be doing this here
		if (!mouseInside () && mouseDown ()) {
			focus = false;
			if (value.equals ("")) {
				value = defaultString;
			}
		}
		
		//Okay here's the reasonable code
		Rectangle bounds = getBoundingRectangle ();
		Graphics g = getGraphics ();
		g.setColor (new Color (FILL_COLOR));
		g.fillRect (0, 0, bounds.width, bounds.height);
		g.setColor (new Color (OUTLINE_COLOR));
		g.drawRect (0, 0, bounds.width - 1, bounds.height - 1);
		g.setColor (new Color (TEXT_COLOR));
		g.drawString (value, TEXT_PADDING_LEFT, TEXT_PADDING_TOP + g.getFontMetrics ().getAscent ());
	}
	
	public void setFilter (String filter) {
		this.filter = filter;
		filterPattern = Pattern.compile (filter);
	}
	
	public void setMaxLength (int length) {
		this.maxLength = length;
	}
	
	public String getContent () {
		return value;
	}
	
	private void addChar (char c) {
		value += c;
	}
	
	@Override
	public void keyTyped (char keyChar) {
		if (focus && value.length () < maxLength) {
			if (filterPattern != null) {
				char[] chars = new char[] {keyChar};
				Matcher matcher = filterPattern.matcher (new String (chars));
				if (matcher.matches ()) {
					addChar (keyChar);
				}
			} else {
				addChar (keyChar);
			}
		}
	}
	
	@Override
	public void keyPressed (int keyCode) {
		if (focus) {
			if (keyCode == KeyEvent.VK_ENTER) {
				//End entry
				focus = false;
				if (value.equals ("")) {
					value = defaultString;
				}
			} else if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
				//Backspace character
				if (value.length () > 0) {
					value = value.substring (0, value.length () - 1);
				}
			}
		}
	}
	
	@Override
	public void mousePressed (int x, int y, int button) {
		if (button == MouseEvent.BUTTON1) {
			focus = true;
			value = "";
		}
	}

}
