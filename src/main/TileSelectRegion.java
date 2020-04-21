package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import toolbar.PlaceButton;

public class TileSelectRegion extends MovableSelectionRegion {
	
	private Tileset currentSet;
	private Tile[][] tiles;
	
	private int anchorX = -1;
	private int anchorY = -1;
	private int selectX = -1;
	private int selectY = -1;
	
	public TileSelectRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
	}
	
	public void setTileset (Tileset tileset) {
		currentSet = tileset;
		setElements (tileset.getParsedTiles (this));
	}
	
	@Override
	public void mousePressed (int x, int y, int button) {
		super.mousePressed (x, y, button);
	}
	
	@Override
	public void mouseDragged (int x, int y, int button) {
		super.mouseDragged (x, y, button);
		mousePressed (x, y, button);
	}
	
	@Override
	public void mouseReleased (int x, int y, int button) {
		super.mouseReleased (x, y, button);
		anchorX = -1;
	}
	
	@Override
	public void doClickOnElement (int x, int y) {
		if (anchorX == -1) {
			anchorX = x;
			anchorY = y;
		}
		if (getElements ()[y][x] != null) {
			PlaceButton.tilesOrObjects = false;
		}
		if (getElements ().length > y && getElements () [0].length > x) {
			selectX = x;
			selectY = y;
		}
		TileRegion newRegion = makeRegion (anchorX, anchorY, selectX, selectY);
		if (!newRegion.equals (getSelectedRegion ())) {
			removeDrawRequest (getSelectedRegion ());
			select (newRegion);
		}
	}
	
	public Tile[][] getSelectedTiles (GuiComponent parent) {
		if (getSelectedRegion () != null) {
			Rectangle[][] regionTiles = getSelectedRegion ().getTiles ();
			Tile[][] tiles = new Tile[regionTiles.length][regionTiles [0].length];
			int startX = getSelectedRegion ().getStartX ();
			int startY = getSelectedRegion ().getStartY ();
			for (int i = 0; i < tiles.length; i ++) {
				for (int j = 0; j < tiles [0].length; j ++) {
					tiles [i][j] = new Tile (((Tile)getElements ()[startY + i][startX + j]).getIcon (), parent);
				}
			}
			return tiles;
		} else {
			return null;
		}
	}
	
	@Override
	public void drawTileRegion (TileRegion region) {
		Rectangle bounds = region.getBounds ();
		Graphics g = getGraphics ();
		g.setColor (new Color (0x0000FF));
		g.drawRect (bounds.x, bounds.y, bounds.width, bounds.height);
		g.drawRect (bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
	}
	
	public TileRegion makeRegion (int anchorX, int anchorY, int selectX, int selectY) {
		int x1, y1, x2, y2;
		x1 = Math.min (anchorX, selectX);
		x2 = Math.max (anchorX, selectX);
		y1 = Math.min (anchorY, selectY);
		y2 = Math.max (anchorY, selectY);
		return new TileRegion (x1, y1, x2 - x1 + 1, y2 - y1 + 1);
	}
}
