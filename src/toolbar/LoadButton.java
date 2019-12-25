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

public class LoadButton extends ToolbarItem {
	
	public LoadButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/load_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		getMainPanel ().getMapInterface ().load ();
	}
}
