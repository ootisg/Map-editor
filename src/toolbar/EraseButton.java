package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.MainPanel;
import main.Tile;
import main.TileSelectMenu;
import main.Tileset;
import map.MapInterface;
import map.ObjectEdit;
import map.TileEdit;
import resources.Sprite;

public class EraseButton extends ToolbarItem {
	
	public EraseButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/Eraser.png").getImageArray () [0]);
		setDragable (true);
		useClickOnElement (true);
	}

	@Override
	public void use (int x, int y) {
		MainPanel mainPanel = getMainPanel ();
		if (mainPanel.getMap ().getActiveLayer ().get (x, y) != null) {
			getMainPanel ().getMapInterface ().edit (new TileEdit (x, y, 1, 1, getMainPanel ().getMap (), new Tile[][] {{null}}));
		}
		try {
		if (MapInterface.objectsInTheMap[x][y] != null) {
			getMainPanel ().getMapInterface().edit(new ObjectEdit (x, y, MapInterface.objectsInTheMap, null));
		}
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
	
	@Override
	public void useDrag (int x, int y) {
		use (x, y);
	}
}
