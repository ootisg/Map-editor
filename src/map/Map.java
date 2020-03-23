package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack;

import main.DisplayableElement;
import main.Tile;
import resources.Sprite;

public class Map {
	
	private ArrayList<TileLayer> mapData;
	private Stack<MapEdit> edits;
	private Stack<MapEdit> undoStack;
	
	private TileLayer activeLayer;
	
	private MapInterface mapInterface;
	private DisplayableElement[][] renderedTiles;
	
	private int topDisplayLayer = 0;
	private boolean onlyTopLayer;
	
	public static final int STARTING_WIDTH = 30;
	public static final int STARTING_HEIGHT = 30;
	
	public Map () {
		resetMap (STARTING_WIDTH, STARTING_HEIGHT);
		mapData.add (new TileLayer (STARTING_WIDTH, STARTING_HEIGHT));
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
		for (int i = 0; i < mapData.size (); i ++) {
			int l = mapData.size () - 1 - (i + topDisplayLayer) % mapData.size ();
			//Only render top mode
			if (onlyTopLayer) {
				l = topDisplayLayer;
				Tile iconTile = mapData.get (l).get (x, y);
				BufferedImage icon;
				if (iconTile == null) {
					icon = null;
				} else {
					icon = iconTile.getIcon ();
				}
				if (icon != null) {
					g.drawImage (icon, 0, 0, null);
				}
				break;
			} 
			Tile iconTile = mapData.get (l).get (x, y);
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
	
	public int getNumLayers () {
		return mapData.size ();
	}
	
	public int getWidth () {
		return mapData.get (0).getWidth ();
	}
	
	public int getHeight () {
		return mapData.get (0).getHeight ();
	}
	
	public Tile getTile (int layer, int x, int y) {
		return mapData.get (layer).get (x, y);
	}
	
	public void resetMap (int width, int height) {
		edits = new Stack<MapEdit> ();
		undoStack = new Stack<MapEdit> ();
		renderedTiles = new DisplayableElement[height][width];
		mapData = new ArrayList<TileLayer> ();
		topDisplayLayer = 0;
	}
	
	public ArrayList<TileLayer> getTileData () {
		return mapData;
	}
	
	public TileLayer addLayer (int width, int height) {
		TileLayer layer = new TileLayer (width, height);
		mapData.add (layer);
		return layer;
	}
	
	public TileLayer addLayer () {
		return addLayer (mapData.get (0).getWidth (), mapData.get (0).getHeight ());
	}
	
	public void toggleLayerMode () {
		onlyTopLayer = !onlyTopLayer;
	}
	
	public void changeLayer () {
		topDisplayLayer ++;
		if (topDisplayLayer >= mapData.size ()) {
			topDisplayLayer = 0;
		}
		activeLayer = mapData.get (topDisplayLayer);
		this.renderElements();
	}
	
	public int getTopDisplayLayer () {
		return topDisplayLayer;
	}
	
	public void resize (int width, int height) {
		for (int i = 0; i < getNumLayers (); i ++) {
			mapData.get (i).resize (width, height);
		}
	}
	
	public boolean canEdit () {
		return !getActiveLayer ().isBackgroundLayer ();
	}
	
	public class TileLayer {
		
		private Tile[][] data;
		private String backgroundPath;
		private BufferedImage background;
		
		private double backgroundScrollX;
		private double backgroundScrollY;
		
		public TileLayer (int width, int height) {
			data = new Tile[height][width];
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
			for (int wy = 0; wy < Math.min (height, getHeight ()); wy ++) {
				for (int wx = 0; wx < Math.min (width, getWidth ()); wx ++) {
					temp [wy][wx] = data [wy][wx];
					displayTemp [wy][wx] = renderedTiles [wy][wx];
				}
			}
			data = temp;
			renderedTiles = displayTemp;
		}
		
		public boolean hasTiles () {
			for (int wy = 0; wy < data.length; wy ++) {
				for (int wx = 0; wx < data [0].length; wx ++) {
					if (data [wy][wx] != null) {
						return true;
					}
				}
			}
			return false;
		}
		
		public boolean isBackgroundLayer () {
			return background != null;
		}
		
		public String getBackgroundFilename () {
			String[] elements = backgroundPath.split ("\\\\|/");
			return elements [elements.length - 1];
		}
		
		public String getBackgroundPath () {
			return backgroundPath;
		}
		
		public BufferedImage getBackground () {
			return background;
		}
		
		public void setBackgroundScroll (double scrollX, double scrollY) {
			backgroundScrollX = scrollX;
			backgroundScrollY = scrollY;
		}
		
		public double getBackgroundScrollX () {
			return backgroundScrollX;
		}
		
		public double getBackgroundScrollY () {
			return backgroundScrollY;
		}
		
		/**
		 * Sets the background to the given filepath. Throws an IllegalStateException if this layer has any tiles on it.
		 * @param path the filepath to the background
		 * @throws IllegalStateException
		 * @throws FileNotFoundException
		 */
		public void setBackground (String path) throws IllegalStateException {
			if (hasTiles ()) {
				throw new IllegalStateException ("Layers with tiles cannot have a background");
			} else {
				backgroundPath = path;
				background = Sprite.getImage (path);
			}
		}
	}
}
