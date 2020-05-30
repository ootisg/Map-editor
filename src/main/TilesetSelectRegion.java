package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFileChooser;

import toolbar.PlaceButton;

public class TilesetSelectRegion extends ScrollableSelectionRegion {
	private int setCount;
	private int selectedX = -1;
	private int selectedY = -1;
	public TilesetSelectRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		resetTilesets ();
		addTileset (null);
	}
	
	public void resetTilesets () {
		Tileset[][] toUse = new Tileset[getGridHeight ()][getGridWidth ()];
		toUse[0][0] = new TilesetAddButton (this);
		setCount = 1;
		setElements (toUse);
	}
	
	@Override
	public void render () {
		//Render elements
		super.render ();
	}
	
	@Override
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		int elementIndex = getElementIndex (horizontalIndex, verticalIndex);
		if (elementIndex == setCount - 1) {
			getWindow ().chooser = new JFileChooser ("resources/tilesets/");
			getWindow ().chooser.showOpenDialog (getWindow ());
			if (getWindow ().chooser.getSelectedFile () != null) {
				String[] parsedPath = getWindow ().chooser.getSelectedFile ().getPath ().split("\\\\|/");
				boolean pastResources = false;
				String newPath = "resources";
				for (int i = 0; i < parsedPath.length; i ++) {
					if (!pastResources) {
						if (parsedPath[i].equals ("resources")) {
							pastResources = true;
						}
					} else {
						newPath += "/" + parsedPath[i];
					}
				}
				addTileset (newPath);
				
			}
			if (selectedX == -1) {
				selectedX = horizontalIndex;
				selectedY = verticalIndex;
				PlaceButton.tilesOrObjects = false;
			}
		} else {
			selectedX = horizontalIndex;
			selectedY = verticalIndex;
			PlaceButton.tilesOrObjects = false;
			if (getSelectedRegion () != null) {
				if (getSelectedRegion ().getStartX () == selectedX && getSelectedRegion ().getStartY () == selectedY) {
					//CLICKCEPTION!
					BufferedImage[][] parsedImages = ((Tileset)(getElements ()[selectedY][selectedX])).getParsedImages ();
					if (parsedImages != null && !(parsedImages.length == 1 && parsedImages [0].length == 1)) {
						TileSelectMenu frame = (TileSelectMenu)getParent ();
						frame.getTileSelect ().setTileset ((Tileset)getElements ()[selectedY][selectedX]);
						frame.getTileSelect ().show ();
						frame.getTilesetSelect ().hide ();
					}
				}
			}
			if (getElements ()[selectedY][selectedX] != null) {
				select (new TileRegion (selectedX, selectedY, 1, 1));
			}
		}
	}
	
	public Tileset getTileset (String path) {
		return new Tileset (path, getElementWidth (), getElementHeight (), this);
	}
	
	public void addRow () {
		if (getElements () == null) {
			setElements (new Tileset[1][getGridWidth ()]);
		} else {
			Tileset[][] newElements = new Tileset[getElements ().length + 1][getElements ()[0].length];
			for (int i = 0; i < getElements ().length; i ++) {
				for (int j = 0; j < getElements ()[0].length; j ++) {
					newElements [i][j] = (Tileset)(getElements () [i][j]);
				}
			}
			setElements (newElements);
		}
	}
	
	public boolean hasTileset (Tileset tileset) {
		for (int i = 0; i < getElements ().length; i ++) {
			for (int j = 0; j < getElements ()[0].length; j ++) {
				if (tileset.equals (getElements () [i][j])) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void addTileset (String path) {
		Tileset workingSet = getTileset (path);
		if (!hasTileset (workingSet)) {
			if (setCount >= getElements ().length * getElements ()[0].length) {
				addRow ();
			}
			getElements ()[(setCount) / getGridWidth ()][(setCount) % getGridWidth ()] = getElements ()[((setCount - 1) / getGridWidth ())][(setCount - 1) % getGridWidth ()];
			getElements ()[(setCount - 1) / getGridWidth ()][(setCount - 1) % getGridWidth ()] = (DisplayableElement)workingSet;
			setCount ++;
		}
	}
	
	public Tileset getSelectedTileset () {
		if (selectedX != -1) {
			return (Tileset)getElements ()[selectedY][selectedX];
		} else {
			return null;
		}
	}
	
	@Override 
	public void drawTileRegion (TileRegion region) {
		if (!PlaceButton.tilesOrObjects) {
		Rectangle bounds = region.getBounds ();
		Rectangle componentBounds = getBoundingRectangle ();
		Graphics g = getGraphics ();
		g.setColor (new Color (0x0000FF));
		g.drawRect (bounds.x, bounds.y, bounds.width, bounds.height);
		g.drawRect (bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
	}
	}
}
