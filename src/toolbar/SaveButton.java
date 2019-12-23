package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.MainPanel;
import main.Tile;
import main.TileSelectMenu;
import main.Tileset;
import map.MapInterface;
import map.TileEdit;
import resources.Sprite;

public class SaveButton extends ToolbarItem {
	
	public SaveButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/save_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		
	}
}
