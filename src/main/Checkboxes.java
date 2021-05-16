package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import resources.Sprite;

public class Checkboxes extends GuiComponent {
	
	public static final BufferedImage BOX_UNCHECKED_ICON = Sprite.getImage ("resources/images/box_unchecked.png");
	public static final BufferedImage BOX_CHECKED_ICON = Sprite.getImage ("resources/images/box_checked.png");
	
	String [] options;
	
	IconButton [] checkboxes;
	
	public Checkboxes(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
	}
	
	@Override
	public void frameEvent () {
		for (int i = 0; i < checkboxes.length; i++) {
			if (checkboxes[i].pressed ()) {
				checkboxes[i].reset();
				updateCheckbox (checkboxes[i]);	
			}
		}
	}
	
	@Override
	public void render () {
		super.render();
		if (!isHidden ()) {	
			Rectangle bounds = getBoundingRectangle ();
			Graphics g = getWindow ().getBuffer ();
			g.setColor (new Color (000));
			int heightCopy = 0;
			for (int i = 0; i < options.length; i++) {
				g.drawString (options[i], bounds.x + 12, bounds.y + heightCopy + 16);
				heightCopy = heightCopy + 20;
			}
			
		}
	}
	
	public void setOptions (String [] options) {
		this.options = options;
		checkboxes = new IconButton[options.length];
		int heightCopy = 0;
		for (int i = 0; i < options.length; i++) {
			checkboxes[i] = new IconButton (new Rectangle (this.getBoundingRectangle().x + this.getBoundingRectangle().width - 23,this.getBoundingRectangle().y + heightCopy, 16, 16), BOX_UNCHECKED_ICON, this);
			heightCopy = heightCopy + 20;
		}
		this.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x, this.getBoundingRectangle().y, this.getBoundingRectangle().width, heightCopy + 32));
		
	}
	
	public void updateCheckbox (IconButton checkbox) {
		if (checkbox.defaultIcon.equals(BOX_CHECKED_ICON)) {
			checkbox.setDefaultIcon(BOX_UNCHECKED_ICON);
		} else {
			checkbox.setDefaultIcon(BOX_CHECKED_ICON);
		}
	}
	public HashMap <String, Boolean> getSelectedOptions () {
		HashMap <String, Boolean> selectedOptions = new HashMap <String, Boolean> ();
		
		for (int i = 0; i < checkboxes.length; i++) {
			if (checkboxes[i].defaultIcon.equals(BOX_CHECKED_ICON)) {
				selectedOptions.put(options[i],true);
			} else {
				selectedOptions.put(options[i], false);
			}
		}
		return selectedOptions;
	}
}
