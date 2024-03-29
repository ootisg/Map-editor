package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFileChooser;

import main.DisplayBox;
import main.DisplayableElement;
import main.EntryField;
import main.ExtendButton;
import main.GameObject;
import main.GuiComponent;
import main.IconButton;
import main.MainLoop;
import main.MainPanel;
import main.MovableSelectionRegion;
import main.ObjectSelectMenu;
import main.ObjectSelectRegion;
import main.SelectionRegion;
import main.Tile;
import main.TileSelectMenu;
import main.Tileset;
import main.VariantConfig;
import main.SelectionRegion.TileRegion;
import resources.Sprite;
import toolbar.EraseButton;
import toolbar.PasteButton;
import toolbar.PlaceButton;
import toolbar.SelectButton;
import toolbar.Toolbar;

public class MapInterface extends MovableSelectionRegion {
	
	public static String BACKGROUND_LOCATION = "resources/backgrounds/";
	
	private TileSelectMenu tileMenu;
	private ObjectSelectMenu objectMenu;
	private Toolbar toolbar;
	
	private int lastMouseX = -1;
	private int lastMouseY = -1;
	
	private int anchorX = -1;
	private int anchorY = -1;
	private int selectX = -1;
	private int selectY = -1;
	
	private String mapName = "new map";
	private int mapNumber = 0;
	private int timer = 0;
	
	private Tile[][] usedTiles = new Tile[0][0];
	private Tile[][] copyTiles;
	private ArrayList <GameObject> [][] usedObjects = new ArrayList[0][0];
	private ArrayList <GameObject> [][] copyObjects;
	private Tile[][] copyTilesComplete;
	public static ArrayList<GameObject>[][] objectsInTheMap;
	private Map map;
	
	private int mapWidth;
	private int mapHeight;
	
	private Stack<MapEdit> edits;
	private Stack<MapEdit> undos;
	
	private int selectionMode;
	public static final int SELECTION_NONE = 0;
	public static final int SELECTION_PLACE_TILES = 1;
	public static final int SELECTION_ENCASE_TILES = 2;
	
	//Resize buttons
	ExtendButton rightExtend;
	ExtendButton bottomExtend;
	
	//For use only by the load method and internal methods it calls
	private int readPos;
	private byte[] inData;
	
	BufferedImage testImg;
	
	DisplayBox workingBox;
	
	public MapInterface (Rectangle bounds, TileSelectMenu tileMenu, ObjectSelectMenu objectMenu, Toolbar toolbar, GuiComponent parent) {
		super (bounds, parent);
		this.setElements (new DisplayableElement[30][30]);
		this.tileMenu = tileMenu;
		this.objectMenu = objectMenu;
		this.toolbar = toolbar;
		objectsInTheMap =  new ArrayList [30] [30];
		map = ((MainPanel)getParent ()).getMap ();
		map.setMapInterface (this);
		edits = new Stack<MapEdit> ();
		undos = new Stack<MapEdit> ();
		workingBox = getMainPanel ().addDisplayBox (new Rectangle (0, 0, 60, 16), "HIYA");
		workingBox.setBgColor(new Color (0x77787d));
		workingBox.hide ();
		//Make extend buttons
		rightExtend = new ExtendButton (new Rectangle (bounds.x, bounds.y, ExtendButton.VERTICAL_WIDTH, ExtendButton.VERTICAL_HEIGHT), ExtendButton.Layout.VERTICAL, this);
		bottomExtend = new ExtendButton (new Rectangle (bounds.x, bounds.y, ExtendButton.HORIZONTAL_WIDTH, ExtendButton.HORIZONTAL_HEIGHT), ExtendButton.Layout.HORIZONTAL, this);
	}
	
	public boolean edit (MapEdit edit) {
		if (edit instanceof BackgroundEdit || 
			edit instanceof ResizeEdit ||
			edit instanceof ObjectEdit ||
			edit instanceof LayerEdit ||
			map.canEdit () //If map is editable and edit is not one of the above exceptions
		) {
			//Do the edit
			undos = new Stack<MapEdit> ();
			boolean editResult;
			try {
				if (!edit.isDiffrent(edits.peek())) {
					return false;
				}
				editResult = edit.doEdit ();
			} catch (EmptyStackException e) {
				editResult = edit.doEdit ();
			}
			if (editResult) {
				edits.push (edit);
			}
			return editResult;
		}
		//Don't do the edit and maybe display a toast or something idfk
		//Also return false because it didn't do the edit
		return false;
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
	
	private void resizeObjects (int width, int height) {
		ArrayList <GameObject> [][] newObjs = new ArrayList[width][height];
		for (int wx = 0; wx < Math.min (objectsInTheMap.length, width); wx ++) {
			for (int wy = 0; wy < Math.min (objectsInTheMap [0].length, height); wy ++) {
				newObjs [wx][wy] = objectsInTheMap [wx][wy];
			}
		}
		objectsInTheMap = newObjs;
	}
	
	public void resize (int width, int height) {
		mapWidth = width;
		mapHeight = height;
		map.resize (width, height);
		resizeObjects (width, height);
	}
	
	public void save () {
		//START OF HEADER
		//Bytes 0-3: RMF# (# is version number)
		//Bytes 4-7: Map width, in tiles
		//Bytes 8-11: Map height, in tiles
		//Bytes 12-15: Number of layers
		//Bytes 16-19: Number of objects (placed)
		//END OF HEADER
		//Tileset list (background layers are excluded)
		//Object import list
		//Background list
		//Tiles
		//Object list (x, y, id, variant)
		JFileChooser chooser = new JFileChooser ("resources/maps");
		if (chooser.showSaveDialog (getWindow ()) != JFileChooser.CANCEL_OPTION) {
			save (chooser.getSelectedFile ());
		}
	}
	
	public void save (File file) {
		if (file.getName().length() > 3) {
			mapName = file.getName().substring(0,file.getName().length()-4);
		} else {
			mapName = file.getName();
		}
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
		int objectCount = 0;
		ArrayList <GameObject> objects = new ArrayList <GameObject>();
		for (int i = 0; i < map.getWidth(); i = i + 1) {
			for (int j = 0; j < map.getHeight(); j = j + 1) {
				if 	(objectsInTheMap[i][j] != null) {
				for (int g = 0; g < objectsInTheMap[i][j].size(); g++) {
						objectsInTheMap[i][j].get(g).setCoords(i, j);
						objects.add(objectsInTheMap[i][j].get(g));
						objectCount = objectCount + 1; 
					}
				}
			}
		}
		writeInt (fileBuffer, objectCount);
		
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
		Iterator<GameObject> iter = objects.iterator();
		ArrayList <GameObject> objectsUsed = new ArrayList <GameObject>();
		while (iter.hasNext()) {
			GameObject objectToCheck = iter.next();
				Iterator<GameObject> iter87electricBooglo = objectsUsed.iterator();
				boolean unused = true;
					while (iter87electricBooglo.hasNext()) {
						if (objectToCheck.getObjectName().equals(iter87electricBooglo.next().getObjectName())){
							unused = false;
							break;
						}
					}
					if (unused) {
					if (!objectsUsed.isEmpty()) {
					addString (fileBuffer, ",");
					}
					addString (fileBuffer, objectToCheck.getObjectName());
					objectsUsed.add(objectToCheck);
					}
			}
		addString (fileBuffer, ";");
		
		//Write background list
		String layerString = "";
		ArrayList<Map.TileLayer> layers = map.getTileData ();
		for (int i = 0; i < layers.size (); i ++) {
			if (i != 0) {
				addString (fileBuffer, ",");
			}
			if (layers.get (i).isBackgroundLayer ()) {
				//Store layer and scroll info
				addString (fileBuffer, layers.get (i).getBackgroundFilename ());
				addString (fileBuffer, ",");
				addString (fileBuffer, Double.toString (layers.get (i).getBackgroundScrollX ()));
				addString (fileBuffer, ",");
				addString (fileBuffer, Double.toString (layers.get (i).getBackgroundScrollY ()));
			} else {
				addString (fileBuffer, "_NULL");
			}
		}
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
			if (!layers.get (layer).isBackgroundLayer ()) {
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
		}
		
		//Write the objects
		Iterator <GameObject>iter2 = objects.iterator();
		while(iter2.hasNext()) {
			GameObject currentObject = iter2.next();
			this.writeBytes(fileBuffer, currentObject.getX(),this.getByteCount(this.getMap().getWidth()));
			this.writeBytes(fileBuffer, currentObject.getY(),this.getByteCount(this.getMap().getHeight()));
			Iterator <GameObject> iter3 = objectsUsed.iterator();
			int index = 0;
			while (iter3.hasNext()) {
				if (currentObject.getObjectName().equals(iter3.next().getObjectName())) {
					break;
				}
				index = index + 1;
			}
			this.writeBytes(fileBuffer, index,this.getByteCount(objectsUsed.size()));
			Iterator <String> iter4 = currentObject.getNameList().iterator();
			while (iter4.hasNext()) {
				String currentName = iter4.next();
				addString (fileBuffer, currentName + ":" + currentObject.getVariantInfo().get(currentName));
				if (iter4.hasNext()) {
					addString (fileBuffer, ",");
				}
			}
			if (!currentObject.getStrangeNameList().isEmpty()) {
			addString (fileBuffer, "#");
			}
			Iterator <String> iter5 = currentObject.getStrangeNameList().iterator();
			while (iter5.hasNext()) {
				String currentName = iter5.next();
				addString (fileBuffer, currentName + ":" + currentObject.getStrangeVariantInfo().get(currentName));
				if (iter5.hasNext()) {
					addString (fileBuffer, ",");
				}
			}
			addString (fileBuffer, ";");
		}
		
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
		//Background list
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
		ObjectSelectRegion objectMenu = mainPanel.getObjectMenu().objectSelect;
		tileMenu.getTilesetSelect().deselect();
		tileMenu.getTileSelect().deselect();
		objectMenu.deselect();
		
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
		resizeObjects(mapWidth,mapHeight);
		map.resetMap (mapWidth, mapHeight);
		tileMenu.resetTilesets ();
		for (int i= 0; i < mapWidth; i++) {
			Arrays.fill(objectsInTheMap[i], null);
		}
		//Read and load new tilesets
		String tilesets = getString (';');
		String[] tilesetList = tilesets.split (",");
		for (int i = 0; i < tilesetList.length; i ++) {
			tileMenu.addTileset (tilesetList [i]);
		}
		
		//Read and import new objects
		objectMenu.resetObjs ();
		String objects = getString (';');
		String[] objectList = objects.split(",");
		if (!(objectList [0].equals ("") && objectList.length == 1)) {
			for (int i = 0; i < objectList.length; i++) {
				objectMenu.addGameObject("resources/objects/" + objectList[i] + ".png");
			}
		}
		
		//Add the correct number of layers
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		MainPanel.getLayerMenu ().getRegion ().resetMenu ();
		for (int i = 0; i < numLayers; i++) {
			MainPanel.getLayerMenu ().getRegion ().addLayer ();
		}
		MainPanel.getLayerMenu ().getRegion ().adjustBounds ();
		
		//Read and import backgrounds
		ArrayList<Map.TileLayer> tiledLayers = new ArrayList<Map.TileLayer> ();
		String backgrounds = getString (';');
		String[] backgroundList = backgrounds.split (",");
		int bi = 0;
		for (int l = 0; l < numLayers; l ++) {
			Map.TileLayer newLayer = map.getTileData ().get (l);
			if (backgroundList [bi].equals ("_NULL")) {
				//Layer is a tile layer
				tiledLayers.add (newLayer);
				bi ++;
			} else {
				//Layer is a background layer
				newLayer.setBackground (BACKGROUND_LOCATION + backgroundList [bi ++]);
				double scrollX = Double.parseDouble (backgroundList [bi ++]);
				double scrollY = Double.parseDouble (backgroundList [bi ++]);
				newLayer.setBackgroundScroll (scrollX, scrollY);
			}
		}
		
		//Read and import tile data
		ArrayList<BufferedImage> tileImgs = tileMenu.getAllTiles ();
		ArrayList<Tile> tileObjs = new ArrayList<Tile> ();
		for (int i = 0; i < tileImgs.size (); i ++) {
			tileObjs.add (new Tile (tileImgs.get (i), this)); //TODO provide non-blank string here or let null tiles save
		}
		int tileSize = getByteCount (tileObjs.size ());
		for (int l = 0; l < tiledLayers.size (); l ++) {
			Map.TileLayer currentLayer = tiledLayers.get (l);
			for (int wy = 0; wy < mapHeight; wy ++) {
				for (int wx = 0; wx < mapWidth; wx ++) {
					int tileId = getInteger (tileSize);
					if (tileId == 0) {
						currentLayer.set (wx, wy, null);
					} else {
						currentLayer.set (wx, wy, tileObjs.get (tileId));
					}
				}
			}
		}
		
		//Read and import object data
		for (int i = 0; i < numObjects; i++) {
			int x = getInteger (this.getByteCount(mapWidth));
			int y = getInteger (this.getByteCount(mapHeight));
			int object = getInteger (this.getByteCount(objectList.length));
			GameObject currentObject = new GameObject ("resources/objects/" + objectList[object] + ".png",this);
			ArrayList <GameObject> wokeing = new ArrayList <GameObject> ();
			wokeing.add(currentObject);
			this.edit(new ObjectEdit (x,y,objectsInTheMap,wokeing));
			String variantInfo = getString (';');
			String [] variantInfos = new String [2];
			boolean strangeInfo = false;
			if (variantInfo.contains("#")) {
				strangeInfo = true;
				variantInfos = variantInfo.split("#");
			} else {
				variantInfos[0] = variantInfo;
			}
			String [] variantList = variantInfos[0].split(",");
			if(variantInfos[0].contains(":")) {
				for (int j = 0; j < variantList.length; j ++) {
					String[] infoSegments = variantList[j].split(":");
					currentObject.setVariantInfo(infoSegments[0],infoSegments[1]);
				}
				VariantConfig c;
				try {
					c = new VariantConfig ("resources/objects/variants/config/" + currentObject.getObjectName() +".txt");
					currentObject.changeIcon(c.getIcon(currentObject.getVariantInfo()));
				} catch (NoSuchFileException e) {
					// TODO Auto-generated catch block
				}
		}
			if (strangeInfo) {
			String [] strangevariantList = variantInfos[1].split(",");
			if(variantInfos[1].contains(":")) {
				for (int j = 0; j < strangevariantList.length; j ++) {
					String[] infoSegments = strangevariantList[j].split(":");
					currentObject.setStrangeVariantInfo(infoSegments[0],infoSegments[1]);
				}
			}
		}
	}
		//...
		if (file.getName().length() > 3) {
			mapName = file.getName().substring(0,file.getName().length()-4);
		} else {
			mapName = file.getName();
		}
		mapNumber = 0;
		mapNumber = mapNumber + 1;
		File newFile = new File ("resources/maps/backups/" + mapName + "_" + mapNumber + ".rmf");
		this.save(newFile);
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
		//Move right extend button
		Rectangle bounds = getBoundingRectangle ();
		Rectangle rightExtendBounds = rightExtend.getBoundingRectangle ();
		int endX = (int)(getElementWidth () * getGridWidth () * getScale () - getViewX ());
		int endY = (int)(getElementHeight () * getGridHeight () * getScale () - getViewY ());
		int newY = bounds.height / 2 - rightExtendBounds.height / 2;
		if (endY < bounds.height) {
			newY = endY / 2 - rightExtendBounds.height / 2;
		}
		rightExtend.moveTo (bounds.x + endX + 16, bounds.y + newY);
		//Hide or show
		if (endX + 16 < 0) {
			rightExtend.hide ();
		} else if (rightExtend.isHidden ()) {
			rightExtend.show ();
		}
		
		//Move left extend button
		Rectangle bottomExtendBounds = bottomExtend.getBoundingRectangle ();
		int newX = bounds.width / 2 - bottomExtendBounds.width / 2;
		if (endX < bounds.width) {
			newX = endX / 2 - bottomExtendBounds.width / 2;
		}
		bottomExtend.moveTo (bounds.x + newX, bounds.y + endY + 16);
		//Hide or show
		if (newX < 0) {
			bottomExtend.hide ();
		} else if (bottomExtend.isHidden ()) {
			bottomExtend.show ();
		}
		
		//Calculate redraw area and re-render tiles
		double redrawX = getViewX () / (getElementWidth () * getScale ());
		double redrawWidth = bounds.getWidth () / (getElementWidth () * getScale ());
		double redrawY = getViewY () / (getElementHeight () * getScale ());
		double redrawHeight = bounds.getHeight () / (getElementHeight () * getScale ());
		map.renderElements (redrawX, redrawY, redrawWidth, redrawHeight);
		
		//Draw tiles
		Graphics g = getGui ().getWindow ().getBuffer ();
		g.setColor (new Color (0xA0A0A0));
		g.fillRect (bounds.x, bounds.y, bounds.width, bounds.height);
		setElements (map.getRenderedElements ());
		super.render ();
		for (int i = 0; i < map.getWidth(); i = i + 1) {
			for (int j = 0; j < map.getHeight(); j = j + 1) {
				if (objectsInTheMap[i][j] != null) {
					for (int e = 0; e < objectsInTheMap[i][j].size(); e++) {
						try {
							BufferedImage oldIcon;
							oldIcon = objectsInTheMap[i][j].get(e).getIcon();
							Image scalledImage = objectsInTheMap[i][j].get(e).getIcon().getScaledInstance((int) (16 * this.getScale()), (int) (16 * this.getScale()), java.awt.Image.SCALE_DEFAULT);
							BufferedImage image = new BufferedImage((int) (16 * this.getScale()), (int) (16 * this.getScale()), 3) ;
							image.getGraphics().drawImage(scalledImage, 0,0, null);
							objectsInTheMap[i][j].get(e).setIcon(image);
							if ((((i* 16)* this.getScale()) + 160) - this.getViewX()>= 160) {
							objectsInTheMap[i][j].get(e).render((int)((((16* i)* this.getScale()) + 160) - this.getViewX()), (int)(((j* 16) * this.getScale())- this.getViewY()));
							}
							objectsInTheMap[i][j].get(e).setIcon(oldIcon);
						} catch (IndexOutOfBoundsException noooooooooooooo) {
							
						}
					}
				}
			}
		}
	}
	@Override 
	public void frameEvent() {
		timer = timer + 1;
		if (timer == 36000) {
			mapNumber = mapNumber + 1;
			File newFile = new File ("resources/maps/backups/" + mapName + "_" + mapNumber + ".rmf");
			this.save(newFile);
			timer = 0;
		}
		if (keyDown(KeyEvent.VK_CONTROL) && keyHit('Z')) {
			this.undo();
		}
		if (keyDown(KeyEvent.VK_CONTROL) && keyHit('Y')) {
			this.redo();
		}
		if (keyDown(KeyEvent.VK_CONTROL) && keyHit('G')) {
			this.showGrid(!this.isGridShown());
		}
		if (keyDown(KeyEvent.VK_CONTROL) && keyHit('C')) {
			MainPanel.getToolbar().getCopyButton().use();
		}
		if (keyDown(KeyEvent.VK_CONTROL) && keyHit('V')) {
			MainPanel.getToolbar().selectItem(MainPanel.getToolbar().getPasteButton());
		}
		if (keyDown(KeyEvent.VK_CONTROL) && keyHit('S')) {
			if (mapName.equals("new map")) {
				this.save();
			} else {
				this.save(new File ("resources/maps/" + mapName + ".rmf"));
			}
		}
		int elementX = (int) (((this.getWindow().getMouseX() -160)/(16* this.getScale())) + (this.getViewX()/(16*this.getScale())));
		int elementY = (int) (((this.getWindow().getMouseY())/(16* this.getScale())) + (this.getViewY()/(16*this.getScale())));
		if (elementX >= 0){
			try {
		ArrayList <GameObject> selectedList = objectsInTheMap[elementX][elementY];
		if (selectedList != null) {
			if (!selectedList.isEmpty()) {
			String working = "";
			for (int i = 0; i < selectedList.size(); i++) {
				working = working + selectedList.get(i).getObjectName().toString();
				working = working + "/n";
				if ( !selectedList.get(i).getVariantInfo().isEmpty()) {
					String [] varants = selectedList.get(i).getVariantInfo().toString().split(",");
					for (int j = 0; j < varants.length; j++) {
						working = working + varants[j];
						working = working + "     /n";
						}
					}
				if (!selectedList.get(i).getStrangeVariantInfo().isEmpty()) {
					String [] varants = selectedList.get(i).getStrangeVariantInfo().toString().split(",");
					for (int j = 0; j < varants.length; j++) {
						working = working + varants[j];
						working = working + "     /n";
						}
					}
				working = working + "/n";
				}
		//	workingBox.setBoundingRectangle(new Rectangle (this.getWindow().getMouseX(), this.getWindow().getMouseY(),80,16));
			workingBox.setX(this.getWindow().getMouseX());
			workingBox.setY(this.getWindow().getMouseY());
			workingBox.show();
			workingBox.setMessage(working);
			} else {
				workingBox.hide();
			}
		} else {
			workingBox.hide();
		}
		} catch (IndexOutOfBoundsException e) {
			workingBox.hide();
		}
	} else {
		workingBox.hide();
	}
		
}
	@Override
	public void drawTileRegion (TileRegion region) {
		//Null check
		if (region == null) {
			return;
		}
		Graphics2D g = (Graphics2D)getGraphics ();
		if (toolbar.getSelectedItem () instanceof PlaceButton || toolbar.getSelectedItem () instanceof PasteButton) {
			//May be hacky, check later
			Tile[][] renderedTiles = usedTiles;
			ArrayList <GameObject>[][] usedObjects = this.usedObjects;
			if (usedTiles == null) {
				return;
			}
			if (toolbar.getSelectedItem () instanceof PasteButton) {
				if (copyTiles != null) {
				renderedTiles = copyTiles;
				}
			}
			g.setComposite (AlphaComposite.getInstance (AlphaComposite.SRC_OVER, 0.5f));
			for (int i = 0; i < renderedTiles.length; i ++) {
				for (int j = 0; j < renderedTiles [0].length; j ++) {
					if (j + region.getStartX () < map.getActiveLayer ().getWidth () && i + region.getStartY () < map.getActiveLayer ().getHeight () && region.getTiles () != null) {
						if (renderedTiles[i][j] != null) {
							renderedTiles [i][j].render (region.getTiles () [i][j], g);
						}
					}
				}
			}
			if (usedObjects == null) {
				return;
			}
			for (int i = 0; i < usedObjects.length; i ++) {
				for (int j = 0; j < usedObjects [0].length; j ++) {
					if (j + region.getStartX () < map.getActiveLayer ().getWidth () && i + region.getStartY () < map.getActiveLayer ().getHeight () && region.objects != null) {
						if (usedObjects[i][j] != null) {
							for (int k = 0; k < usedObjects[i][j].size(); k++) {
								
								usedObjects [i][j].get(k).render (region.getTiles() [i][j], g);
							}
						}
					}
				}
			}
		} else if (toolbar.getSelectedItem () instanceof SelectButton && region.getBounds () != null) {
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
			if (PlaceButton.tilesOrObjects && toolbar.getSelectedItem() instanceof PlaceButton) {
				usedObjects = new ArrayList[1][1];
				//may be hacky be sure to check back here
				usedObjects [0][0] = ObjectSelectMenu.objectSelect.getSelectedObject ();
				
				usedTiles = new Tile[][] {{null}};
			} else {
				if (!tileMenu.getTilesetSelect ().isHidden ()) {
					Tileset selectedSet = tileMenu.getTilesetSelect ().getSelectedTileset ();
					if (selectedSet != null) {
						usedTiles = selectedSet.getParsedTiles (this);
					}
				} else {
					usedTiles = tileMenu.getTileSelect ().getSelectedTiles (this);
				}
				usedObjects = new ArrayList[][] {{null}};
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
				usedObjects =  copyObjects;
				}
			if (toolbar.getSelectedItem () instanceof PlaceButton && PlaceButton.tilesOrObjects && ObjectSelectMenu.objectSelect.getSelectedObject () != null) {
				selectWidth = 1;
				selectHeight = 1;
			}
			
			Rectangle[][] grid = makeGrid (new Rectangle ((int)-getViewX (), (int)-getViewY (), (int)(getElements () [0].length * getElementWidth () * getScale ()), (int)(getElements ().length * getElementHeight () * getScale ())), getElementWidth () * getScale (), getElementHeight () * getScale ());
			int[] selectedCell = getCell (x, y);
			if (selectWidth != -1 && PlaceButton.tilesOrObjects && toolbar.getSelectedItem() instanceof PlaceButton) {
				select (new TileRegion (selectedCell[0], selectedCell[1], 1, 1));
			} else if (selectWidth != -1) {
				select (new TileRegion (selectedCell [0], selectedCell [1], selectWidth, selectHeight));
			}
		}
	}
	
	@Override
	public void mouseDragged (int x, int y, int button) {
		super.mouseDragged (x, y, button);
		if (toolbar.getSelectedItem () != null && (toolbar.getSelectedItem () instanceof PlaceButton || (toolbar.getSelectedItem ().dragable () && toolbar.getSelectedItem ().usesClickOnElement ()))) {
			if (toolbar.getSelectedItem () instanceof PlaceButton && button == 1) {
				if (x < (int) (this.getGridWidth() *(16*this.getScale())) && y < (int)(this.getGridHeight()* (16*this.getScale()))) {
					try {
						boolean dontUse = false;
						int workX = x + (int)this.getViewX();
						int workY = y + (int)this.getViewY();
					for (int i = 0; i < objectsInTheMap[(int)(workX/(16*this.getScale()))][(int)(workY/(16*this.getScale()))].size(); i++) {
						if (objectsInTheMap[(int)(workX/(16*this.getScale()))][(int)(workY/(16*this.getScale()))].get(i).getObjectName().equals(ObjectSelectMenu.objectSelect.getSelectedObject().get(0).getObjectName())) {
							dontUse = true;
							break;
						}
					}
					if (!dontUse) {
						if (toolbar.getSelectedItem().dragable()) {
							toolbar.getSelectedItem().useDragIntermideite(x, y);
						}
					}
					} catch (NullPointerException e) {
						if (toolbar.getSelectedItem().dragable()) {
							toolbar.getSelectedItem().useDragIntermideite(x, y);
						}
					}
				}
			} else {
				Rectangle[][] grid = makeGrid (new Rectangle ((int)-getViewX (), (int)-getViewY (), (int)(getElements () [0].length * getElementWidth () * getScale ()), (int)(getElements ().length * getElementHeight () * getScale ())), getElementWidth () * getScale (), getElementHeight () * getScale ());
				int[] selectedCell = getCell (x, y);
				doClickOnElement (selectedCell [0], selectedCell [1]);
			}
		}
	}
	
	
	@Override
	public void doClickOnElement (int x, int y) {
		if (this.lastMouseButtonPressed () == MouseEvent.BUTTON1) {
			if (toolbar.getSelectedItem () instanceof SelectButton || toolbar.getSelectedItem () instanceof EraseButton) {
				toolbar.getSelectedItem ().useIntermideite (x, y);
			}
			if (toolbar.getSelectedItem() instanceof PlaceButton) {
				toolbar.getSelectedItem ().useIntermideite ((int)Math.ceil(((x* 16* this.getScale() - this.getViewX()))),(int)Math.ceil((( y * 16 * this.getScale() - this.getViewY()))));
			}
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
		super.mouseReleased (x, y, button);
			if (button == 1) {
				if (toolbar.getSelectedItem () instanceof SelectButton) {
					anchorX = -1;
				} else {
					if (!(toolbar.getSelectedItem() instanceof PlaceButton)) {
						if (toolbar.getSelectedItem () != null) {
							if (x < this.getGridWidth() *(16*this.getScale()) && y < this.getGridHeight()* (16*this.getScale())) {
								toolbar.getSelectedItem ().use (x, y);
							}
						}
					} 
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
	public void setCopyObjects (ArrayList <GameObject>[][] objects) {
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
	public ArrayList<GameObject>[][] getCopyObjects(){
		return copyObjects;
	}
	
	public Map getMap () { 
		return map;
	}
	
	public int getMapWidth () {
		return mapWidth;
	}
	
	public int getMapHeight () {
		return mapHeight;
	}
	
	@Override
	public int getGridWidth () {
		return getElements ()[0].length;
	}
	
	@Override
	public int getGridHeight () {
		return getElements ().length;
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
