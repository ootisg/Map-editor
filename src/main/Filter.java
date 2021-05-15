package main;

import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Filter extends DisplayableImageElement {
	public Filter(GuiComponent parent) {
		super(parent);
		box.hide ();
		// TODO Auto-generated constructor stub
	}
	BufferedImage texture;
	DisplayBox box = new DisplayBox (new Rectangle (0,0,1,1), getParent ());
	public void runFilterCode () {
	
	}
	public BufferedImage getTexture () {
		return texture;
	}
	public void setTexture (BufferedImage texture) {
		this.texture = texture;
	}
	public void setName (String name) {
		box.setMessage(name);
	}
	public void showName (int x, int y) {
		int boxWidth = (int)box.getBoundingRectangle ().getWidth ();
		int boxHeight = (int) box.getBoundingRectangle().getHeight();
		box.setX(x - boxWidth - 4);
		box.setY(y);
		box.show();
	}
	public void dontShowName () {
		box.hide();
	}
	
}
