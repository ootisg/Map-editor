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
		MainPanel mainPanel = getMainPanel ();
		if (mainPanel.getMap ().getActiveLayer ().get (x, y) != null) {
			getMainPanel ().getMapInterface ().edit (new TileEdit (x, y, 1, 1, new Tile[][] {{null}}));
		}
	}
	
	@Override
	public void useDrag (int x, int y) {
		use (x, y);
	}
}
