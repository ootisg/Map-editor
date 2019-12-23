package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.util.Stack;

import main.DisplayableElement;
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
	private Tile[][] copyTilesComplete;
	
	private Map map;
	
	private Stack<MapEdit> edits;
	private Stack<MapEdit> undos;
	
	private int selectionMode;
	public static final int SELECTION_NONE = 0;
	public static final int SELECTION_PLACE_TILES = 1;
	public static final int SELECTION_ENCASE_TILES = 2;
	
	public MapInterface (Rectangle bounds, TileSelectMenu tileMenu, ObjectSelectMenu objectMenu, Toolbar toolbar, GuiComponent parent) {
		super (bounds, parent);
		this.setElements (new DisplayableElement[30][30]);
		this.tileMenu = tileMenu;
		this.objectMenu = objectMenu;
		this.toolbar = toolbar;
		map = ((MainPanel)getParent ()).getMap ();
		map.setMapInterface (this);
		edits = new Stack<MapEdit> ();
		undos = new Stack<MapEdit> ();
	}
	
	public boolean edit (MapEdit edit) {
		edits.push (edit);
		undos = new Stack<MapEdit> ();
		return edit.doEdit (map);
	}
	
	public boolean undo () {
		if (edits.empty ()) {
			return false;
		}
		MapEdit undone = edits.pop ();
		undos.push (undone);
		return undone.undo (map);
	}
	
	public boolean redo () {
		if (undos.empty ()) {
			return false;
		}
		MapEdit redone = undos.pop ();
		edits.push (redone);
		return redone.doEdit (map);
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
}
