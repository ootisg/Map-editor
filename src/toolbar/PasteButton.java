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

public class PasteButton extends ToolbarItem {
	
	public PasteButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/Paste.png").getImageArray () [0]);
	}

	@Override
	public void use (int x, int y) {
		MainPanel mainPanel = getMainPanel ();
		MapInterface mapInterface = mainPanel.getMapInterface ();
		Tile[][] usedTiles = null;
		
		//Get currently selected tiles
		usedTiles = mapInterface.getCopyTiles ();
		Rectangle[][] grid = mapInterface.makeGrid (new Rectangle ((int)-mapInterface.getViewX (), (int)-mapInterface.getViewY (), (int)(mapInterface.getElements () [0].length * mapInterface.getElementWidth () * mapInterface.getScale ()), (int)(mapInterface.getElements ().length * mapInterface.getElementHeight () * mapInterface.getScale ())), mapInterface.getElementWidth () * mapInterface.getScale (), mapInterface.getElementHeight () * mapInterface.getScale ());
		int[] selectedCell = mainPanel.getMapInterface ().getCell (x, y);
		mainPanel.getMapInterface ().edit (new TileEdit (selectedCell [0], selectedCell [1], usedTiles [0].length, usedTiles.length, getMainPanel ().getMap (), usedTiles));
		for (int i = 0; i < mapInterface.getCopyObjects().length; i = i + 1) {
			for (int j = 0; j < mapInterface.getCopyObjects()[0].length; j = j + 1) {
				if (mapInterface.getCopyObjects()[i][j] != null) {
		mapInterface.edit(new ObjectEdit (selectedCell [0] + j, selectedCell [1] + i,MapInterface.objectsInTheMap,mapInterface.getCopyObjects()[i][j]));
				}
			}
		}
	}
}
