package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.plaf.synth.Region;

import map.MapInterface;

public abstract class SelectionRegion extends GuiComponent {
	
	private int elementWidth = 16;
	private int elementHeight = 16;
	
	private int gridWidth = 9;
	private int gridHeight = 14;
	
	private double scale = 1.0;
	private double viewX = 0;
	private double viewY = 0;
	
	private boolean showGrid = true;
	
	private DisplayableElement[][] elements;
	
	private TileRegion selectedRegion;
	private LinkedList<TileRegion> scheduledRegions;
	
	public static final double MAX_SCALE = 8;
	public static final double MIN_SCALE = .25;

	
	protected SelectionRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		scheduledRegions = new LinkedList<TileRegion> ();
	}
	
	@Override
	public void draw () {
		Rectangle bounds = new Rectangle (0, 0, getBoundingRectangle ().width, getBoundingRectangle ().height);
		Graphics g = getGraphics ();
		double startX = bounds.x - viewX;
		double startY = bounds.y - viewY;
		double displayWidth = elementWidth * scale;
		double displayHeight = elementHeight * scale;
		DisplayableElement[][] elements = getElements ();
		int elementsWidth = 0;
		int elementsHeight = 0;
		if (elements != null) {
			if (elements [0] != null) {
				elementsWidth = elements [0].length;
			}
			elementsHeight = elements.length;
		}
		
		int runs = 0;
		Rectangle[][] cells = makeGrid (new Rectangle (bounds.x - (int)viewX, bounds.y - (int)viewY, (int)(elementsWidth * elementWidth * scale), (int)(elementsHeight * elementHeight * scale)), displayWidth, displayHeight);
		for (int i = 0; i <= elements.length && i < cells.length; i ++) {
			for (int j = 0; j <= elements [0].length && j < cells [0].length; j ++) {
				if (elements [i][j] != null && cells [i][j] != null) {
					try {
						((DisplayableImageElement)(elements [i][j])).render (cells [i][j], g);
					} catch (ClassCastException e) {
						((DisplayableTextElement)elements [i][j]).render(elements[i][j].getParent(), cells[i][j]);
					}
				}
			}
		}
		/*for (int i = 0; i < elementsHeight; i ++) {
			for (int j = 0; j < elementsWidth; j ++) {
				Rectangle cell = new Rectangle ((int)(startX + j * displayWidth), (int)(startY + i * displayHeight), (int)displayWidth, (int)displayHeight);
				if (bounds.intersects (cell) && elements[i][j] != null) {
					((Tileset)(elements [i][j])).render (cell);
				}
			}
		}*/
		double endX = bounds.x - viewX + displayWidth * cells [0].length;
		double endY = bounds.y - viewY + displayHeight * cells.length;
		if (showGrid) {
			g.setColor (new Color (0x000000));
			for (int i = 0; i < cells.length; i ++) {
				g.drawLine ((int)startX, cells [i][0].y, (int)endX, cells [i][0].y);
				if (i == cells.length - 1) {
					g.drawLine ((int)startX, cells [i][0].y + cells [i][0].height, (int)endX, cells [i][0].y + cells [i][0].height);
				}
			}
			for (int i = 0; i < cells[0].length; i ++) {
				g.drawLine (cells [0][i].x, (int)startY, cells [0][i].x, (int)endY);
				if (i == cells[0].length - 1) {
					g.drawLine (cells [0][i].x + cells [0][i].width, (int)startY, cells [0][i].x + cells [0][i].height, (int)endY);
				}
			}
		}
		try {
			Collections.sort (scheduledRegions);
			Iterator<TileRegion> iter = scheduledRegions.iterator ();
			while (iter.hasNext ()) {
				TileRegion working = iter.next ();
				working.setTileData (cells, MapInterface.objectsInTheMap);
				drawTileRegion (working);
			}
		} catch (Exception e) {
			System.out.println ("Exception thrown while drawing tile region: \n" + e.toString ());
			e.printStackTrace ();
			//Continue
		}
	}
	
	public int[] getCell (int x, int y) {
		Rectangle bounds = getBoundingRectangle ();
		Rectangle[][] cells = makeGrid (new Rectangle (-(int)viewX, -(int)viewY, (int)(elements [0].length * elementWidth * scale), (int)(elements.length * elementHeight * scale)), elementWidth * scale, elementHeight * scale);
		int clickedCellX = -1;
		int clickedCellY = -1;
		for (int i = 0; i < cells.length; i ++) {
			for (int j = 0; j < cells[0].length; j ++) {
				if (cells [i][j].contains (x, y)) {
					clickedCellX = j;
					clickedCellY = i;
					break;
				}
			}
		}
		return new int[] {clickedCellX, clickedCellY};
	}
	
	@Override
	public void mousePressed (int x, int y, int button) {
		if (button == MouseEvent.BUTTON1) {
			int[] cell = getCell (x, y);
			if (cell [0] != -1) {
				doClickOnElement (cell [0], cell [1]);
			}
		}
	}
	
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		
	}
	
	public boolean isInBounds (int cellX, int cellY) {
		if (cellX >= 0 && cellY >= 0 && cellX < elements[0].length && cellY < elements.length) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setElementWidth (int elementWidth) {
		this.elementWidth = elementWidth;
	}
	
	public void setElementHeight (int elementHeight) {
		this.elementHeight = elementHeight;
	}
	
	public void setGridDimensions (int horizontal, int vertical) {
		gridWidth = horizontal;
		gridHeight = vertical;
	}
	
	public void setScale (double scale) {
		if (scale >= MIN_SCALE && scale <= MAX_SCALE) {
			this.scale = scale;
		}
	}
	
	public void setScaleWithAnchor (double scale, double anchorX, double anchorY) {
		if (scale >= MIN_SCALE && scale <= MAX_SCALE) {
			double scaleDiff = scale / getScale ();
			setScale (scale);
			double scaledAnchorX = anchorX * scaleDiff;
			double scaledAnchorY = anchorY * scaleDiff;
			setView (getViewX () + (scaledAnchorX - anchorX), getViewY () + (scaledAnchorY - anchorY));
		}
	}
	
	public void setView (double x, double y) {
		this.viewX = Math.max (x, 0);
		this.viewY = Math.max (y, 0);
	}
	
	public void showGrid (boolean toShow) {
		showGrid = toShow;
	}
	
	protected void setElements (DisplayableElement[][] elements) {
		this.elements = elements;
	}
	
	public int getElementWidth () {
		return elementWidth;
	}
	
	public int getElementHeight () {
		return elementHeight;
	}
	
	public int getGridWidth () {
		return gridWidth;
	}
	
	public int getGridHeight () {
		return gridHeight;
	}
	
	public double getScale () {
		return scale;
	}
	
	public double getViewX () {
		return viewX;
	}
	
	public double getViewY () {
		return viewY;
	}
	
	public boolean isGridShown () {
		return showGrid;
	}
	
	public DisplayableElement[][] getElements () {
		return elements;
	}
	
	public ArrayList<DisplayableElement> getElementList () {
		ArrayList<DisplayableElement> elementList = new ArrayList<DisplayableElement> ();
		for (int wy = 0; wy < elements.length; wy ++) {
			for (int wx = 0; wx < elements [0].length; wx ++) {
				if (elements [wy][wx] == null) {
					return elementList;
				} else {
					elementList.add (elements [wy][wx]);
				}
			}
		}
		return elementList;
	}
	
	public int getElementIndex (int x, int y) {
		if (x >= elements [0].length || y >= elements.length || x < 0 || y < 0) {
			return -1;
		}
		return y * elements [0].length + x;
	}
	
	public void drawTileRegion (TileRegion region) {
		
	}
	
	public Rectangle[][] makeGrid (Rectangle reigon, double cellWidth, double cellHeight) {
		// TODO fix bounds pls (becasue their gay)
		int workingX = reigon.x;
		int workingY = reigon.y;
		int newX = 0;
		int newY = 0;
		int cellHoriz = 0;
		int cellVert = 0;
		ArrayList<ArrayList<Rectangle>> grid = new ArrayList<ArrayList<Rectangle>> ();
		Rectangle workingCell = new Rectangle (reigon.x, reigon.y, (int)cellWidth, (int)cellHeight);
		
		while (workingCell.intersects (reigon)) {
			
			grid.add (new ArrayList<Rectangle> ());
			
			while (workingCell.intersects (reigon)) {
				newX = (int)(cellWidth * (cellHoriz + 1)) + reigon.x;
				newY = (int)(cellHeight * (cellVert + 1)) + reigon.y;
				workingCell = new Rectangle (workingX, workingY, newX - workingX, newY - workingY);
				if (workingCell.intersects (reigon)) {
					grid.get (cellVert).add (workingCell);
					workingX = newX;
					cellHoriz ++;
				}
			}
			
			cellHoriz = 0;
			workingX = reigon.x;
			workingY = newY;
			cellVert ++;
			newX = (int)(cellWidth * (cellHoriz + 1)) + reigon.x;
			newY = (int)(cellHeight * (cellVert + 1)) + reigon.y;
			workingCell = new Rectangle (workingX, workingY, newX - workingX, newY - workingY);
		}
		
		Iterator<ArrayList<Rectangle>> vertIter = grid.iterator ();
		Iterator<Rectangle> horizIter;
		Rectangle[][] arrayGrid = new Rectangle[grid.size ()][grid.get (0).size ()];
		int vertIndex = 0;
		int horizIndex = 0;
		while (vertIter.hasNext ()) {
			horizIter = vertIter.next ().iterator ();
			while (horizIter.hasNext ()) {
				arrayGrid [vertIndex][horizIndex] = horizIter.next ();
				horizIndex ++;
			}
			horizIndex = 0;
			vertIndex ++;
		}
		return arrayGrid;
	}
	
	public void requestDrawTileRegion (TileRegion region) {
		scheduledRegions.add (region);
	}
	
	public boolean removeDrawRequest (TileRegion region) {
		Iterator<TileRegion> iter = scheduledRegions.iterator ();
		while (iter.hasNext ()) {
			if (iter.next ().equals (region)) {
				iter.remove ();
				return true;
			}
		}
		return false;
	}
	
	public void select (TileRegion region) {
		if (selectedRegion == null || !selectedRegion.equals (region)) {
			removeDrawRequest (selectedRegion);
			requestDrawTileRegion (region);
			selectedRegion = region;
		}
	}
	
	public void deselect () {
		removeDrawRequest (selectedRegion);
		selectedRegion = null;
	}
	
	public TileRegion getSelectedRegion () {
		return selectedRegion;
	}
	
	public class TileRegion implements Comparable {
		
		private int tileStartX = -1;
		private int tileStartY = -1;
		private int tileWidth;
		private int tileHeight;
		public ArrayList<GameObject>[][] objects;
		private Rectangle bounds;
		private Rectangle[][] tiles;
		private ArrayList <GameObject> selectedObject;
		private boolean hasTileData;
		public TileRegion (int startX, int startY, int width, int height) {

			setTiles (startX, startY, width, height);
		}
		public void setTileData (Rectangle[][] sourceTiles, ArrayList <GameObject>[][]sourceObjects) {
			if (tileStartX != -1 && tileStartY != -1) {
				tiles = new Rectangle[tileHeight][tileWidth];
				objects = new ArrayList[tileHeight][tileWidth];
				int vbound = -1;
				int hbound = -1;
				boolean firstObject = true;
				for (int wy = 0; wy < tileHeight; wy ++) {
					for (int wx = 0; wx < tileWidth; wx ++) {
						if (wy + tileStartY >= sourceTiles.length || wx + tileStartX >= sourceTiles[0].length) {
							if (wy + tileStartY >= sourceTiles.length) {
								if (vbound == -1) {
									vbound = wy - 1;
								}
							} else if (wx + tileStartX >= sourceTiles[0].length) {
								if (hbound == -1) {
									hbound = wx - 1;
								}
							}
						} else {
							tiles [wy][wx] = sourceTiles [wy + tileStartY][wx + tileStartX];
							objects [wy][wx] = sourceObjects[(wx + tileStartX)][(wy + tileStartY)];
							if (sourceObjects [(wx + tileStartX)][(wy + tileStartY)] != null && firstObject) {
								firstObject = false;
								selectedObject = sourceObjects [(wx + tileStartX)][(wy + tileStartY)];
							}
						}
					}
				}
				if (hbound == -1) {
					hbound = tileWidth - 1;
				}
				if (vbound == -1) {
					vbound = tileHeight - 1;
				}
				Rectangle topLeft = tiles [0][0];
				Rectangle bottomRight = tiles [vbound][hbound];
				bounds = new Rectangle (topLeft.x, topLeft.y, bottomRight.x + bottomRight.width - topLeft.x, bottomRight.y + bottomRight.height - topLeft.y);
				hasTileData = true;
			}
		}
		public ArrayList <GameObject> getSelectedGameObjects () {
			return selectedObject;
		}
		public int getStartX () {
			return tileStartX;
		}
		
		public int getStartY () {
			return tileStartY;
		}
		
		public int getTileWidth () {
			return tileWidth;
		}
		
		public int getTileHeight () {
			return tileHeight;
		}
		
		public void setTiles (int startX, int startY, int width, int height) {
			tileStartX = startX;
			tileStartY = startY;
			tileWidth = width;
			tileHeight = height;
			hasTileData = false;
			bounds = null;
			tiles = null;
			objects = null;
		}
		
		public boolean hasTilesData () {
			return hasTileData;
		}
		
		public Rectangle getBounds () {
			return bounds;
		}
		
		public Rectangle[][] getTiles () {
			return tiles;
		}
		
		@Override
		public boolean equals (Object o) {
			if (o instanceof TileRegion) {
				TileRegion obj = (TileRegion)o;
				if (tileStartX == obj.tileStartX && tileStartY == obj.tileStartY && tileWidth == obj.tileWidth && tileHeight == obj.tileHeight) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public int compareTo (Object o) {
			if (o instanceof TileRegion) {
				TileRegion obj = (TileRegion)o;
				int diffX = obj.tileStartX - tileStartX;
				if (diffX != 0) {
					return diffX;
				}
				int diffY = obj.tileStartY - tileStartY;
				if (diffY != 0) {
					return diffY;
				}
				int diffWidth = obj.tileWidth - tileWidth;
				if (diffWidth != 0) {
					return diffWidth;
				}
				int diffHeight = obj.tileHeight - tileHeight;
				if (diffHeight != 0) {
					return diffHeight;
				}
			}
			return 0;
		}
	}
}
