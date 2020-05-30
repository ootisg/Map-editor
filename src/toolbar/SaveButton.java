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

public class SaveButton extends ToolbarItem {
	DisplayBox box;
	public SaveButton (Toolbar parent) {
		super (parent);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Save",this);
		setIcon (new Sprite ("resources/images/save_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		getMainPanel ().getMapInterface ().save ();
	}
	@Override
	public void frameEvent () {
		if (this.mouseInside()) {
			box.show();
		} else {
			box.hide();
		}
}
}
