package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import resources.Sprite;

public class OptionsMenu extends SelectionMenu {

	public static final BufferedImage APPLY_BUTTON_ICON = Sprite.getImage ("resources/images/apply.png");
	
	int boxHeight = 60;
	
	ArrayList <GuiComponent> content = new ArrayList <GuiComponent> ();
	 
	IconButton acceptButton = new IconButton (new Rectangle (this.getBoundingRectangle().x + this.getBoundingRectangle().width - 23,this.getBoundingRectangle().y, 37, 9), APPLY_BUTTON_ICON, this);
	
	Filter managing;
	
	public OptionsMenu (Rectangle bounds, GuiComponent parent, Filter toManage) {
		super (bounds, parent, "OPTIONS");
		boxHeight = bounds.height;
		managing = toManage;
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent();
		if (acceptButton.pressed()) {
			this.hide();
			managing.runFilter();
			acceptButton.reset();
		}
	}
	
	@Override
	public void keyPressed (int key) {
		if (key == (int)'A') {
			managing.runFilter ();
		}
	}

	public void addContent (GuiComponent newContent) {
		content.add(newContent);
		int height = (int) (this.getBoundingRectangle().height + newContent.getBoundingRectangle().getHeight());
		this.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x,this.getBoundingRectangle().y,this.getBoundingRectangle().width,height));
		acceptButton.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x + this.getBoundingRectangle().width/3, this.getBoundingRectangle().y + height - 20, 37, 9));
	}
	public HashMap <String, Object> getAllData (){
		HashMap <String, Object> selectedOptions = new HashMap <String, Object> ();
		
		for (int i = 0; i < content.size(); i++) {
			if (content.get(i) instanceof Checkboxes) {
				Checkboxes cbox = (Checkboxes) content.get(i);
				selectedOptions.putAll(cbox.getSelectedOptions());
			}
			if (content.get(i) instanceof TileSelection) {
				TileSelection ts = (TileSelection) content.get(i);
				selectedOptions.put (ts.getRequest (), ts);
			}
		}
		return selectedOptions;
	}
}
