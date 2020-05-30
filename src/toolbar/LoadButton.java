package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.DisplayBox;
import main.MainPanel;
import main.Tile;
import main.TileSelectMenu;
import main.Tileset;
import map.MapInterface;
import map.TileEdit;
import resources.Sprite;

public class LoadButton extends ToolbarItem {
	boolean pressed = false;
	DisplayBox box;
	public LoadButton (Toolbar parent) {
		super (parent);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Load",this);
		setIcon (new Sprite ("resources/images/load_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		pressed = true;
	}
	@Override 
	public void frameEvent () {
		if (pressed) {
			getMainPanel ().getMapInterface ().load ();
			pressed = false;
		}
		
		if (this.mouseInside()) {
			box.show();
		} else {
			box.hide();
		}
	}
}
