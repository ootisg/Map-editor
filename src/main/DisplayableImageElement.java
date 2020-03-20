package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import resources.Sprite;

public class DisplayableImageElement extends DisplayableElement {

	BufferedImage icon;
	
	public DisplayableImageElement (BufferedImage icon, GuiComponent parent) {
		super (parent);
		this.icon = icon;
	}
	public DisplayableImageElement (GuiComponent parent) {
		super (parent);
	}
	public void setIcon (BufferedImage icon) {
		this.icon = icon;
	}
	
	public BufferedImage getIcon () {
		return icon;
	}
	
	@Override
	public void render (int x, int y) {
		Graphics g = getParent ().getWindow ().getBuffer ();
		g.drawImage (icon, x, y, null);
	}
	
	public void render (int x, int y, Graphics g) {
		g.drawImage (icon, x, y, null);
	}
	
	public void render (Rectangle region) {
		Graphics g = getParent ().getGraphics ();
		g.drawImage (icon, region.x, region.y, region.x + region.width, region.y + region.height, 0, 0, icon.getWidth (), icon.getHeight (), null);
	}
	
	public void render (Rectangle region, Graphics g) {
		if (icon != null && region != null) {
			g.drawImage (icon, region.x, region.y, region.x + region.width, region.y + region.height, 0, 0, icon.getWidth (), icon.getHeight (), null);
		}
	}
}
