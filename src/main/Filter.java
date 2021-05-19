package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Filter extends DisplayableImageElement {
	
	BufferedImage texture;
	DisplayBox box = new DisplayBox (new Rectangle (0,0,1,1), getParent ());
	
	public Filter(GuiComponent parent) {
		super(parent);
		box.hide ();
	}
	
	public void runFilterCode () {
		
	}
	
	public void runFilter () {
		
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
		box.setX(x - boxWidth);
		box.setY(y);
		box.show();
	}
	
	public void dontShowName () {
		box.hide();
	}
	
}
