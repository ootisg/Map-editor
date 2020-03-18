package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class IconButton extends GuiComponent {

	public static final int BACKGROUND_COLOR = 0xC0C0C0;
	public static final int OUTLINE_COLOR = 0x000000;
	
	private boolean pressed = false;
	private boolean mouseDown = false;
	
	private int marginLeft = 0;
	private int marginTop = 0;
	
	BufferedImage defaultIcon;
	BufferedImage pressedIcon;
	
	public IconButton (Rectangle bounds, BufferedImage icon, GuiComponent parent) {
		super (bounds, parent);
		this.defaultIcon = icon;
	}
	
	public IconButton (Rectangle bounds, BufferedImage defaultIcon, BufferedImage pressedIcon, GuiComponent parent) {
		super (bounds, parent);
		this.defaultIcon = defaultIcon;
		this.pressedIcon = pressedIcon;
	}
	
	@Override
	public void draw () {
		Graphics g = getGraphics ();
		Rectangle bounds = getBoundingRectangle ();
		g.setColor (new Color (BACKGROUND_COLOR));
		g.fillRect (0, 0, bounds.width, bounds.height);
		if (mouseDown && pressedIcon != null) {
			g.drawImage (pressedIcon, marginLeft, marginTop, null);
		} else {
			g.drawImage (defaultIcon, marginLeft, marginTop, null);
		}
		g.setColor (new Color (OUTLINE_COLOR));
		g.drawRect (0, 0, bounds.width - 1, bounds.height - 1);
	}
	
	@Override
	public void mousePressed (int x, int y, int button) {
		mouseDown = true;
	}
	
	@Override
	public void mouseReleased (int x, int y, int button) {
		pressed = true;
		mouseDown = false;
	}
	
	public void reset () {
		pressed = false;
		mouseDown = false;
	}
	
	public boolean pressed () {
		return pressed;
	}
	
	public void setDefaultIcon (BufferedImage icon) {
		defaultIcon = icon;
	}
	
	public void setPressedIcon (BufferedImage icon) {
		pressedIcon = icon;
	}
	
	public void setMargins (int marginLeft, int marginTop) {
		this.marginLeft = marginLeft;
		this.marginTop = marginTop;
	}
	
	public int getMarginLeft () {
		return marginLeft;
	}
	
	public int getMarginTop () {
		return marginTop;
	}

}
