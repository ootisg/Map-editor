package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFileChooser;

import main.DisplayableElement;
import main.GameObject;
import main.GuiComponent;
import main.MainPanel;
import main.MovableSelectionRegion;
import main.ObjectSelectMenu;
import main.SelectionRegion;
import main.Tile;
import main.TileSelectMenu;
import main.Tileset;
import main.SelectionRegion.TileRegion;
import resources.Sprite;
import toolbar.EraseButton;
import toolbar.PasteButton;
import toolbar.PlaceButton;
import toolbar.SelectButton;
import toolbar.Toolbar;

public class MapInterface extends MovableSelectionRegion {
	
	private TileSelectMenu tileMenu;
	private ObjectSelectMenu objectMenu;
	private Toolbar toolbar;
	
	private int lastMouseX = -1;
	private int lastMouseY = -1;
	
	private int anchorX = -1;
	private int anchorY = -1;
	private int selectX = -1;
	private int selectY = -1;
	
	private Tile[][] usedTiles = new Tile[0][0];
	private Tile[][] copyTiles;
	private GameObject [][] copyObjects;
	private Tile[][] copyTilesComplete;
	public static GameObject[][] objectsInTheMap;
	private Map map;
	
	private Stack<MapEdit> edits;
	private Stack<MapEdit> undos;
	
	private int selectionMode;
	public static final int SELECTION_NONE = 0;
	public static final int SELECTION_PLACE_TILES = 1;
	public static final int SELECTION_ENCASE_TILES = 2;
	
	//For use only by the load method and internal methods it calls
	private int readPos;
	private byte[] inData;
	
	public MapInterface (Rectangle bounds, TileSelectMenu tileMenu, ObjectSelectMenu objectMenu, Toolbar toolbar, GuiComponent parent) {
		super (bounds, parent);
		this.setElements (new DisplayableElement[30][30]);
		this.tileMenu = tileMenu;
		this.objectMenu = objectMenu;
		this.toolbar = toolbar;
		objectsInTheMap = new GameObject [256] [256];
		map = ((MainPanel)getParent ()).getMap ();
		map.setMapInterface (this);
		edits = new Stack<MapEdit> ();
		undos = new Stack<MapEdit> ();
	}
	
	public boolean edit (MapEdit edit) {
		edits.push (edit);
		undos = new Stack<MapEdit> ();
		return edit.doEdit ();
	}
	
	public boolean undo () {
		if (edits.empty ()) {
			return false;
		}
		MapEdit undone = edits.pop ();
		undos.push (undone);
		return undone.undo ();
	}
	
	public boolean redo () {
		if (undos.empty ()) {
			return false;
		}
		MapEdit redone = undos.pop ();
		edits.push (redone);
		return redone.doEdit ();
	}
	
	public void save () {
		//START OF HEADER
		//Bytes 0-3: RMF# (# is version number)
		//Bytes 4-7: Map width, in tiles
		//Bytes 8-11: Map height, in tiles
		//Bytes 12-15: Number of layers
		//Bytes 16-19: Number of objects (placed)
		//END OF HEADER
		//Tileset list
		//Object import list
		//Tiles
		//Object list (x, y, id, variant)
		JFileChooser chooser = new JFileChooser ("resources/maps");
		if (chooser.showSaveDialog (getWindow ()) != JFileChooser.CANCEL_OPTION) {
			save (chooser.getSelectedFile ());
		}
	}
	
	public void save (File file) {
		
		//Get map editor components
		MainPanel mainPanel = getMainPanel ();
		TileSelectMenu tileMenu = mainPanel.getTileMenu ();
		MapInterface mapInterface = mainPanel.getMapInterface ();
		
		//Get tilesets
		ArrayList<DisplayableElement> tilesetList = tileMenu.getTilesetSelect ().getElementList ();
		
		//Make data buffer
		ArrayList<Byte> fileBuffer = new ArrayList<Byte> ();
		
		//Add the file identifier
		addString (fileBuffer, "RMF1");
		
		//Write the map dimensions/etc
		writeInt (fileBuffer, map.getWidth ());
		writeInt (fileBuffer, map.getHeight ());
		writeInt (fileBuffer, map.getNumLayers ());
		
		//Write object count info
		writeInt (fileBuffer, 0);
		
		//Write the list of tilesets
		for (int i = 0; i < tilesetList.size () - 1; i ++) {
			addString (fileBuffer, ((Tileset)(tilesetList.get (i))).getPath ());
			if (i == tilesetList.size () - 2) {
				addString (fileBuffer, ";");
			} else {
				addString (fileBuffer, ",");
			}
		}
		
		//Write the list of objects
		addString (fileBuffer, ";");
		
		//Write the tiles
		ArrayList<BufferedImage> allTiles = tileMenu.getAllTiles ();
		HashMap<BufferedImage, Integer> tileMap = new HashMap<BufferedImage, Integer> ();
		for (int i = 0; i < allTiles.size (); i ++) {
			tileMap.put (allTiles.get (i), i);
		}
		int tileCount = allTiles.size ();
		int tileSize = getByteCount (tileCount);
		for (int layer = 0; layer < map.getNumLayers (); layer ++) {
			for (int wy = 0; wy < map.getHeight (); wy ++) {
				for (int wx = 0; wx < map.getWidth (); wx ++) {
					Tile current = map.getTile (layer, wx, wy);
					int tileId;
					if (current == null) {
						tileId = 0;
					} else {
						tileId = tileMap.get (current.getIcon ());
					}
					writeBytes (fileBuffer, tileId, tileSize);
				}
			}
		}
		
		//Write the objects
		
		//Write changes to the file
		byte[] writeData = new byte[fileBuffer.size ()];
		for (int i = 0; i < fileBuffer.size (); i ++) {
			writeData [i] = fileBuffer.get (i);
		}
		file.delete ();
		try {
			file.createNewFile ();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream (file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stream.write (writeData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stream.close ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addString (ArrayList<Byte> buffer, String toAdd) {
		for (int i = 0; i < toAdd.length (); i ++) {
			buffer.add ((byte)(toAdd.charAt (i)));
		}
	}
	
	private void writeInt (ArrayList<Byte> buffer, int value) {
		buffer.add ((byte)((value & 0xFF000000) >>> 24));
		buffer.add ((byte)((value & 0x00FF0000) >>> 16));
		buffer.add ((byte)((value & 0x0000FF00) >>> 8));
		buffer.add ((byte)(value & 0xFF));
	}
	
	private void writeBytes (ArrayList<Byte> buffer, int value, int numBytes) {
		if (numBytes >= 4) {buffer.add ((byte)((value & 0xFF000000) >>> 24));}
		if (numBytes >= 3) {buffer.add ((byte)((value & 0x00FF0000) >>> 16));}
		if (numBytes >= 2) {buffer.add ((byte)((value & 0x0000FF00) >>> 8));}
		if (numBytes >= 1) {buffer.add ((byte)(value & 0xFF));}
	}
	
	private int getByteCount (int value) {
		int i = 0;
		while (value != 0) {
			value /= 256;
			i ++;
		}
		return i;
	}
	
	public void load () {
		//START OF HEADER
		//Bytes 0-3: RMF# (# is version number)
		//Bytes 4-7: Map width, in tiles
		//Bytes 8-11: Map height, in tiles
		//Bytes 12-15: Number of layers
		//Bytes 16-19: Number of objects (placed)
		//END OF HEADER
		//Tileset list
		//Object import list
		//Tiles
		//Object list (x, y, id, variant)
		JFileChooser chooser = new JFileChooser ("resources/maps");
		if (chooser.showSaveDialog (getWindow ()) != JFileChooser.CANCEL_OPTION) {
			load (chooser.getSelectedFile ());
		}
	}
	
	public void load (File file) {
		
		//Get relevant GUI components
		MainPanel mainPanel = getMainPanel ();
		TileSelectMenu tileMenu = mainPanel.getTileMenu ();
		
		//Read the file
		inData = new byte[(int) file.length ()];
		FileInputStream stream = null;
		try {
			stream = new FileInputStream (file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stream.read (inData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stream.close ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readPos = 0;
		
		//Loading code can now go here
		
		char MAX_VERSION = '1';
		
		//Check header
		String fmtString = getString (3);
		if (!fmtString.equals ("RMF")) {
			//Error: not an RMF file
		}
		
		String verString = getString (1);
		if (verString.charAt (0) < '1' || verString.charAt (0) > MAX_VERSION) {
			//Error: unsupported version
		}
		
		//Read attributes
		int mapWidth = getInteger (4);
		int mapHeight = getInteger (4);
		int numLayers = getInteger (4);
		int numObjects = getInteger (4);
		//Reset map and tilesets
		map.resetMap (mapWidth, mapHeight);
		tileMenu.resetTilesets ();
		//Read and load new tilesets
		String tilesets = getString (';');
		String[] tilesetList = tilesets.split (",");
		for (int i = 0; i < tilesetList.length; i ++) {
			tileMenu.addTileset (tilesetList [i]);
		}
		//Read and import new objects
		getString (';');
		//Read and import tile data
		ArrayList<BufferedImage> tileImgs = tileMenu.getAllTiles ();
		ArrayList<Tile> tileObjs = new ArrayList<Tile> ();
		for (int i = 0; i < tileImgs.size (); i ++) {
			tileObjs.add (new Tile (tileImgs.get (i), this));
		}
		int tileSize = getByteCount (tileObjs.size ());
		for (int l = 0; l < numLayers; l ++) {
			Map.TileLayer newLayer = map.addLayer (mapWidth, mapHeight);
			for (int wy = 0; wy < mapHeight; wy ++) {
				for (int wx = 0; wx < mapWidth; wx ++) {
					newLayer.set (wx, wy, tileObjs.get (getInteger (tileSize)));
				}
			}
		}
		//Read and import object data
		//...
	}
	
	private String getString (int length) {
		byte[] usedData = new byte[length];
		int endPos = readPos + length;
		int i = 0;
		while (readPos < endPos) {
			usedData [i] = inData [readPos];
			readPos ++;
			i ++;
		}
		return new String (usedData);
	}
	
	private String getString (char endChar) {
		int len = 0;
		int i = readPos;
		while (inData [i] != endChar) {
			len ++;
			i ++;
		}
		String str = getString (len);
		readPos ++;
		return str;
	}
	
	private int getInteger (int bytes) {
		int total = 0;
		for (int i = 0; i < bytes; i ++) {
			int toRead = inData [readPos + i];
			if (toRead < 0) {
				toRead += 256;
			}
			total += (toRead << ((bytes - 1 - i) * 8));
		}
		readPos += bytes;
		return total;
	}
	
	@Override
	public DisplayableElement[][] getElements () {
		return super.getElements ();
	}
	
	@Override
	public void render () {
		Rectangle bounds = getBoundingRectangle ();
		Graphics g = getGui ().getWindow ().getBuffer ();
		g.setColor (new Color (0xA0A0A0));
		g.fillRect (bounds.x, bounds.y, bounds.width, bounds.height);
		setElements (map.getRenderedElements ());
		super.render ();
		for (int i = 0; i < 256; i = i + 1) {
			for (int j = 0; j < 256; j = j + 1) {
				if (objectsInTheMap[i][j] != null) {
					BufferedImage oldIcon;
					oldIcon = objectsInTheMap[i][j].getIcon();
					Image scalledImage = objectsInTheMap[i][j].getIcon().getScaledInstance((int) (16 * this.getScale()), (int) (16 * this.getScale()), java.awt.Image.SCALE_DEFAULT);
					BufferedImage image = new BufferedImage((int) (16 * this.getScale()), (int) (16 * this.getScale()), 3) ;
					image.getGraphics().drawImage(scalledImage, 0,0, null);
					objectsInTheMap[i][j].setIcon(image);
					if ((((i* 16)* this.getScale()) + 160) - this.getViewX()> 160) {
					objectsInTheMap[i][j].render((int)((((16* i)* this.getScale()) + 160) - this.getViewX()), (int)(((j* 16) * this.getScale())- this.getViewY()));
					}
					objectsInTheMap[i][j].setIcon(oldIcon);
				}
			}
		}
	}
	
	@Override
	public void drawTileRegion (TileRegion region) {
		Graphics2D g = (Graphics2D)getGraphics ();
		if (toolbar.getSelectedItem () instanceof PlaceButton || toolbar.getSelectedItem () instanceof PasteButton) {
			//May be hacky, check later
			Tile[][] renderedTiles = usedTiles;
			if (toolbar.getSelectedItem () instanceof PasteButton) {
				renderedTiles = copyTiles;
			}
			g.setComposite (AlphaComposite.getInstance (AlphaComposite.SRC_OVER, 0.5f));
			for (int i = 0; i < renderedTiles.length; i ++) {
				for (int j = 0; j < renderedTiles [0].length; j ++) {
					if (j + region.getStartX () < map.getActiveLayer ().getWidth () && i + region.getStartY () < map.getActiveLayer ().getHeight () && region.getTiles () != null) {
						renderedTiles [i][j].render (region.getTiles () [i][j], g);
					}
				}
			}
		} else if (toolbar.getSelectedItem () instanceof SelectButton) {
			Rectangle bounds = region.getBounds ();
			g.setColor (new Color (0x0000FF));
			g.drawRect (bounds.x, bounds.y, bounds.width, bounds.height);
			g.drawRect (bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
		}
	}
	
	@Override
	public void mouseMoved (int x, int y) {
		if (toolbar.getSelectedItem () instanceof PlaceButton || toolbar.getSelectedItem () instanceof PasteButton) {
			//Get currently selected tiles
			if (!tileMenu.getTilesetSelect ().isHidden ()) {
				Tileset selectedSet = tileMenu.getTilesetSelect ().getSelectedTileset ();
				if (selectedSet != null) {
					usedTiles = selectedSet.getParsedTiles (this);
				}
			} else {
				usedTiles = tileMenu.getTileSelect ().getSelectedTiles (this);
			}
			
			int selectWidth = -1;
			int selectHeight = -1;
			if (toolbar.getSelectedItem () instanceof PlaceButton && usedTiles != null && usedTiles.length != 0) {
				selectWidth = usedTiles[0].length;
				selectHeight = usedTiles.length;
			}
			if (toolbar.getSelectedItem () instanceof PasteButton && copyTiles != null && copyTiles.length != 0) {
				selectWidth = copyTiles[0].length;
				selectHeight = copyTiles.length;
			}
			
			Rectangle[][] grid = makeGrid (new Rectangle ((int)-getViewX (), (int)-getViewY (), (int)(getElements () [0].length * getElementWidth () * getScale ()), (int)(getElements ().length * getElementHeight () * getScale ())), getElementWidth () * getScale (), getElementHeight () * getScale ());
			int[] selectedCell = getCell (x, y);
			if (selectWidth != -1) {
					select (new TileRegion (selectedCell [0], selectedCell [1], selectWidth, selectHeight));
			}
		}
	}
	
	@Override
	public void mouseDragged (int x, int y, int button) {
		super.mouseDragged (x, y, button);
		if (toolbar.getSelectedItem () != null && toolbar.getSelectedItem ().dragable () && toolbar.getSelectedItem ().usesClickOnElement ()) {
			Rectangle[][] grid = makeGrid (new Rectangle ((int)-getViewX (), (int)-getViewY (), (int)(getElements () [0].length * getElementWidth () * getScale ()), (int)(getElements ().length * getElementHeight () * getScale ())), getElementWidth () * getScale (), getElementHeight () * getScale ());
			int[] selectedCell = getCell (x, y);
			doClickOnElement (selectedCell [0], selectedCell [1]);
		}
	}
	
	@Override
	public void doClickOnElement (int x, int y) {
		if (toolbar.getSelectedItem () instanceof SelectButton || toolbar.getSelectedItem () instanceof EraseButton) {
			toolbar.getSelectedItem ().use (x, y);
		}
	}
	
	public TileRegion makeRegion (int anchorX, int anchorY, int selectX, int selectY) {
		int x1, y1, x2, y2;
		x1 = Math.min (anchorX, selectX);
		x2 = Math.max (anchorX, selectX);
		y1 = Math.min (anchorY, selectY);
		y2 = Math.max (anchorY, selectY);
		return new TileRegion (x1, y1, x2 - x1 + 1, y2 - y1 + 1);
	}
	
	@Override
	public void mouseReleased (int x, int y, int button) {
		if (toolbar.getSelectedItem () instanceof SelectButton) {
			anchorX = -1;
		} else {
			if (toolbar.getSelectedItem () != null) {
				toolbar.getSelectedItem ().use (x, y);
			}
		}
	}
	
	public void setAnchor (int anchorX, int anchorY) {
		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}
	
	public void setSelect (int selectX, int selectY) {
		this.selectX = selectX;
		this.selectY = selectY;
	}
	
	public void setSelectionMode (int selectionMode) {
		this.selectionMode = selectionMode;
	}
	
	public void setCopyTiles (Tile[][] tiles) {
		copyTiles = tiles;
	}
	public void setCopyObjects (GameObject[][] objects) {
		copyObjects = objects;
	}
	public int getAnchorX () {
		return anchorX;
	}
	
	public int getAnchorY () {
		return anchorY;
	}
	
	public int getSelectX () {
		return selectX;
	}
	
	public int getSelectY () {
		return selectY;
	}
	
	public int getSelectionMode () {
		return selectionMode;
	}
	
	public Tile[][] getCopyTiles () {
		return copyTiles;
	}
	public GameObject [][] getCopyObjects(){
		return copyObjects;
	}
	
	public Map getMap () { 
		return map;
	}
	
	public MainPanel getMainPanel () {
		GuiComponent working = this;
		while (!(working instanceof MainPanel) && (working instanceof GuiComponent)) {
			working = working.getParent ();
		}
		if (working instanceof MainPanel) {
			return (MainPanel)working;
		}
		return null;
	}
}
