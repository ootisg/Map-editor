package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import main.DisplayableElement;
import main.Tile;

public class Map {
	
	private ArrayList<TileLayer> mapData;
	private Stack<MapEdit> edits;
	private Stack<MapEdit> undoStack;
	
	private TileLayer activeLayer;
	
	private MapInterface mapInterface;
	private DisplayableElement[][] renderedTiles;
	
	int topDisplayLayer;
	
	public static final int STARTING_WIDTH = 30;
	public static final int STARTING_HEIGHT = 30;
	
	public Map () {
		edits = new Stack<MapEdit> ();
		undoStack = new Stack<MapEdit> ();
		renderedTiles = new DisplayableElement[STARTING_WIDTH][STARTING_HEIGHT];
		mapData = new ArrayList<TileLayer> ();
		mapData.add (new TileLayer ());
		activeLayer = mapData.get (0);
	}
	
	public void doEdit (String type, String data) {
		//edits.push (new MapEdit (type, data));
	}
	
	public void undo () {
		if (!edits.isEmpty ()) {
			MapEdit working = edits.pop ();
		}
	}
	
	public void renderElements () {
		for (int wy = 0; wy < renderedTiles.length; wy ++) {
			for (int wx = 0; wx < renderedTiles [0].length; wx ++) {
				renderCell (wx, wy);
			}
		}
	}
	
	public void renderCell (int x, int y) {
		BufferedImage fullRender = new BufferedImage (mapInterface.getElementWidth (), mapInterface.getElementHeight (), BufferedImage.TYPE_INT_ARGB);
		Graphics g = fullRender.getGraphics ();
		for (int i = topDisplayLayer; i != topDisplayLayer - 1 && mapData.size () != 0; i = (i + 1) % mapData.size ()) {
			Tile iconTile = mapData.get (i).get (x, y);
			BufferedImage icon;
			if (iconTile == null) {
				icon = null;
			} else {
				icon = iconTile.getIcon ();
			}
			if (icon != null) {
				g.drawImage (icon, 0, 0, null);
			}
			//Avoid an infinite loop for the 1 layer case
			if (mapData.size () == 1) {
				break;
			}
		}
		Tile working = new Tile (fullRender, mapInterface);
		renderedTiles [y][x] = working;
	}
	
	public DisplayableElement[][] getRenderedElements () {
		return renderedTiles;
	}
	
	public TileLayer getActiveLayer () {
		return activeLayer;
	}
	
	public void setMapInterface (MapInterface mapInterface) {
		this.mapInterface = mapInterface;
	}
	
	public class TileLayer {
		
		private Tile[][] data;
		
		public TileLayer () {
			data = new Tile[30][30];
		}
		
		public int getWidth () {
			return data[0].length;
		}
		
		public int getHeight () {
			return data.length;
		}
		
		public Tile get (int x, int y) {
			if (y >= 0 && x >= 0 && y < getHeight () && x < getWidth ()) {
				return data [y][x];
			} else {
				return null;
			}
		}
		
		public boolean set (int x, int y, Tile tile) {
			if (y >= 0 && x >= 0) {
				//TODO use logs to round up to nearest power of 2
				boolean resized = false;
				if (y >= getHeight () && x < getWidth ()) {
					resize (getWidth (), y + 1);
					resized = true;
				} else if (x >= getWidth () && y < getHeight ()) {
					resize (x + 1, getHeight ());
					resized = true;
				} else if (x >= getWidth () && y >= getHeight ()) {
					resize (x + 1, y + 1);
					resized = true;
				}
				data [y][x] = tile;
				renderCell (x, y);
				return resized;
			} else {
				return false;
			}
		}
		
		public void resize (int width, int height) {
			Tile[][] temp = new Tile[height][width];
			DisplayableElement[][] displayTemp = new DisplayableElement[height][width];
			for (int wy = 0; wy < getHeight (); wy ++) {
				for (int wx = 0; wx < getWidth (); wx ++) {
					temp [wy][wx] = data [wy][wx];
					displayTemp [wy][wx] = renderedTiles [wy][wx];
				}
			}
			data = temp;
			renderedTiles = displayTemp;
		}
	}
}
