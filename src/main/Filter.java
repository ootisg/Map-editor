package main;

import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Filter extends DisplayableImageElement {
	
	BufferedImage texture;
	DisplayBox box = new DisplayBox (new Rectangle (0,0,1,1), getParent ());
	OptionsMenu menu = new OptionsMenu (new Rectangle (200, 100, 140, 20), this.getParent().getMainPanel(),this);
	
	public Filter(GuiComponent parent) {
		super(parent);
		box.hide ();
		menu.hide();
		String [] options = new String [] {"option 1", "lol meme", "ha dumb idiot", "option 7"};
		Checkboxes check = new Checkboxes (new Rectangle (200, 120, 140, 20), menu);
		check.setOptions(options);
		menu.addContent (check);
	}
	
	public void runFilterCode () {
		menu.show();
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
