package toolbar;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import main.DisplayBox;
import main.GameObject;
import main.MainPanel;
import main.Tile;
import main.TileSelectMenu;
import main.Tileset;
import map.MapInterface;
import map.ObjectEdit;
import map.TileEdit;
import resources.Sprite;

public class PasteButton extends ToolbarItem {
	DisplayBox box;
	public PasteButton (Toolbar parent) {
		super (parent);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Paste",this);
		setIcon (new Sprite ("resources/images/Paste.png").getImageArray () [0]);
	}

	@Override
	public void use (int x, int y) {
		MainPanel mainPanel = getMainPanel ();
		MapInterface mapInterface = mainPanel.getMapInterface ();
		Tile[][] usedTiles = null;
		
		//Get currently selected tiles
		usedTiles = mapInterface.getCopyTiles ();
		int[] selectedCell = mainPanel.getMapInterface ().getCell (x, y);
		if (usedTiles != null) {
		Rectangle[][] grid = mapInterface.makeGrid (new Rectangle ((int)-mapInterface.getViewX (), (int)-mapInterface.getViewY (), (int)(mapInterface.getElements () [0].length * mapInterface.getElementWidth () * mapInterface.getScale ()), (int)(mapInterface.getElements ().length * mapInterface.getElementHeight () * mapInterface.getScale ())), mapInterface.getElementWidth () * mapInterface.getScale (), mapInterface.getElementHeight () * mapInterface.getScale ());
		mainPanel.getMapInterface ().edit (new TileEdit (selectedCell [0], selectedCell [1], usedTiles [0].length, usedTiles.length, getMainPanel ().getMap (), usedTiles));
		}
		if (mapInterface.getCopyObjects() != null) {
		for (int i = 0; i < mapInterface.getCopyObjects().length; i = i + 1) {
			for (int j = 0; j < mapInterface.getCopyObjects()[0].length; j = j + 1) {
				if (mapInterface.getCopyObjects()[i][j] != null) {
					ArrayList <GameObject> working = new ArrayList <GameObject> ();
					for (int k = 0; k < mapInterface.getCopyObjects()[i][j].size(); k++) {
						GameObject workingObject = (GameObject)mapInterface.getCopyObjects()[i][j].get(k).clone();
						workingObject.refreshIcon();
						working.add(workingObject);
					}
					mapInterface.edit(new  ObjectEdit (selectedCell [0] + j, selectedCell [1] + i,MapInterface.objectsInTheMap,working));
					}
				}
			}
		}
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
