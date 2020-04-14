package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.MainPanel;
import main.ObjectSelectMenu;
import main.SelectionRegion.TileRegion;
import main.Tile;
import main.TileSelectMenu;
import main.Tileset;
import main.VariantSelectMenu;
import map.MapInterface;
import map.ObjectEdit;
import map.TileEdit;
import resources.Sprite;

public class PlaceButton extends ToolbarItem {
	public static boolean tilesOrObjects = false; 
	public PlaceButton (Toolbar parent) {
		super (parent);
		this.setDragable(true);
		setIcon (new Sprite ("resources/images/Place.png").getImageArray () [0]);
	}

	@Override
	public void use (int x, int y) {
		// TODO Auto-generated method stub
		MainPanel mainPanel = getMainPanel ();
		TileSelectMenu tileMenu = mainPanel.getTileMenu ();
		MapInterface mapInterface = mainPanel.getMapInterface ();
		Toolbar toolbar = (Toolbar)getParent ();
		if (!(mainPanel.getVariantMenu().getBoundingRectangle().contains(x + 160, y) && !mainPanel.getVariantMenu().isHidden())) {
		if (!tilesOrObjects) {
		Tile[][] usedTiles = null;
		
		//Get currently selected tiles
		boolean nullTile = false;
		try {
		if (!tileMenu.getTilesetSelect ().isHidden ()) {
			TileRegion selectedSetRegion = tileMenu.getTilesetSelect ().getSelectedRegion ();
			if (selectedSetRegion.getStartX () == 0 && selectedSetRegion.getStartY () == 0) {
				nullTile = true;
			}
			Tileset selectedSet = tileMenu.getTilesetSelect ().getSelectedTileset ();
			if (selectedSet != null) {
				usedTiles = selectedSet.getParsedTiles (this);
			}
		} else {
			usedTiles = tileMenu.getTileSelect ().getSelectedTiles (this);
		}
		Rectangle[][] grid = mapInterface.makeGrid (new Rectangle ((int)-mapInterface.getViewX (), (int)-mapInterface.getViewY (), (int)(mapInterface.getElements () [0].length * mapInterface.getElementWidth () * mapInterface.getScale ()), (int)(mapInterface.getElements ().length * mapInterface.getElementHeight () * mapInterface.getScale ())), mapInterface.getElementWidth () * mapInterface.getScale (), mapInterface.getElementHeight () * mapInterface.getScale ());
		int[] selectedCell = mainPanel.getMapInterface ().getCell (x, y);
		if (!nullTile) {
			mainPanel.getMapInterface ().edit (new TileEdit (selectedCell [0], selectedCell [1], usedTiles [0].length, usedTiles.length, mapInterface.getMap (), usedTiles));
		} else {
			mainPanel.getMapInterface ().edit (new TileEdit (selectedCell [0], selectedCell [1], 1, 1, mapInterface.getMap (), new Tile[][] {{null}}));
		}
		} catch (NullPointerException e) {
			tilesOrObjects = true;
		}
		} else {
			try {
	mainPanel.getMapInterface().edit (new ObjectEdit ((int) ((x/(16 * mapInterface.getScale())) + (mapInterface.getViewX()/(16 * mapInterface.getScale()))), (int) ((y/(16 * mapInterface.getScale())) + (mapInterface.getViewY()/(16 * mapInterface.getScale()))), MapInterface.objectsInTheMap,ObjectSelectMenu.objectSelect.getSelectedObject()));	
			} catch (IndexOutOfBoundsException e) {
				
			}
			}
		}
	}
	
	@Override
	public void onSelect () {
		getMainPanel ().getMapInterface ().deselect ();
	}
}
