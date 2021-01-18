package toolbar;

import java.util.ArrayList;

import main.GameObject;
import main.MainPanel;
import main.SelectionRegion.TileRegion;
import main.Tile;
import map.Map.TileLayer;
import map.MapInterface;
import resources.Sprite;

public class CopyButton extends ToolbarItem {
	public CopyButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Copy");
		setIcon (new Sprite ("resources/images/Copy.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		Tile[][] tiles;
		ArrayList <GameObject>[][] objects;
		MainPanel mainPanel = getMainPanel ();
		if (mainPanel.getToolbar ().getSelectedItem () instanceof SelectButton) {
			MapInterface mapInterface = mainPanel.getMapInterface ();
			TileRegion workingRegion = mapInterface.getSelectedRegion ();
			if (workingRegion != null) {
				int startX = workingRegion.getStartX ();
				int startY = workingRegion.getStartY ();
				int width = workingRegion.getTileWidth ();
				int height = workingRegion.getTileHeight ();
				objects = workingRegion.objects;
				//Support for multiple layers to be added later
				TileLayer currentLayer = mainPanel.getMap ().getActiveLayer ();
				tiles = new Tile[height][width];
				for (int wy = 0; wy < height; wy++) {
					for (int wx = 0; wx < width; wx++) {
						tiles[wy][wx] = currentLayer.get (startX + wx, startY + wy);
					}
				}
				mapInterface.setCopyTiles (tiles);
				mapInterface.setCopyObjects(objects);
			}
		}
	}
}
