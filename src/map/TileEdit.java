package map;

import java.util.Arrays;

import main.Tile;

public class TileEdit implements MapEdit {

	int startX;
	int startY;
	int width;
	int height;
	int layer;
	Tile[][] newTiles;
	Tile[][] oldTiles;
	
	public TileEdit (int x, int y, int width, int height, Tile[][] tiles) {
		startX = x;
		startY = y;
		this.width = width;
		this.height = height;
		newTiles = tiles;
	}
	
	private void fillTiles (Map map, Tile[][] putTiles, Tile[][] getTiles) {
		int mapWidth = map.getActiveLayer ().getWidth ();
		int mapHeight = map.getActiveLayer ().getHeight ();
		//Set layer
		for (int wy = 0; wy < height && startY + wy < mapHeight; wy ++) {
			for (int wx = 0; wx < width && startX + wx < mapWidth; wx ++) {
				if (getTiles != null) {
					getTiles [wy][wx] = map.getActiveLayer ().get (startX + wx, startY + wy);
				}
				map.getActiveLayer ().set (startX + wx, startY + wy, putTiles [wy][wx]);
			}
		}
	}
	
	@Override
	public boolean doEdit (Map map) {
		oldTiles = new Tile[newTiles.length][newTiles[0].length];
		fillTiles (map, newTiles, oldTiles);
		return true;
	}

	@Override
	public boolean undo (Map map) {
		if (oldTiles == null) {
			return false;
		}
		fillTiles (map, oldTiles, null);
		return true;
	}

	@Override
	public boolean affectsMap () {
		return true;
	}

}
