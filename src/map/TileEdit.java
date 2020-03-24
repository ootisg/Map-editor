package map;

import java.util.Arrays;

import main.Tile;
import main.Tileset;

public class TileEdit implements MapEdit {

	int startX;
	int startY;
	int width;
	int height;
	Tile[][] newTiles;
	Tile[][] oldTiles;
	
	Map.TileLayer layer;
	
	public TileEdit (int x, int y, int width, int height, Map map, Tile[][] tiles) {
		startX = x;
		startY = y;
		this.width = width;
		this.height = height;
		this.layer = map.getActiveLayer ();
		newTiles = tiles;
	}
	
	private void fillTiles (Tile[][] putTiles, Tile[][] getTiles) {
		int mapWidth = layer.getWidth ();
		int mapHeight = layer.getHeight ();
		//Set layer
		for (int wy = 0; wy < height && startY + wy < mapHeight; wy ++) {
			for (int wx = 0; wx < width && startX + wx < mapWidth; wx ++) {
				if (getTiles != null) {
					getTiles [wy][wx] = layer.get (startX + wx, startY + wy);
				}
				layer.set (startX + wx, startY + wy, putTiles [wy][wx]);
			}
		}
	}
	
	@Override
	public boolean doEdit () {
		oldTiles = new Tile[newTiles.length][newTiles[0].length];
		fillTiles (newTiles, oldTiles);
		return true;
	}

	@Override
	public boolean undo () {
		if (oldTiles == null) {
			return false;
		}
		fillTiles (oldTiles, null);
		return true;
	}

	@Override
	public boolean affectsMap () {
		return true;
	}

}
