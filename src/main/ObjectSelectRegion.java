package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFileChooser;

public class ObjectSelectRegion extends ScrollableSelectionRegion {
	
	private int objCount;
	
	private int selectedX = -1;
	private int selectedY = -1;
	
	public ObjectSelectRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		resetObjs ();
		
	}
	
	public void resetObjs () {
		System.out.println (getGridHeight ());
		Tileset[][] toUse = new Tileset[getGridHeight ()][getGridWidth ()];
		toUse[0][0] = new TilesetAddButton (this);
		objCount = 1;
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
		if (elementIndex == objCount - 1) {
			getWindow ().chooser = new JFileChooser ("resources/objects/");
			getWindow ().chooser.showOpenDialog (getWindow ());
			if (getWindow ().chooser.getSelectedFile () != null) {
				addTileset (getWindow ().chooser.getSelectedFile ().getPath ());
			}
			if (selectedX == -1) {
				selectedX = horizontalIndex;
				selectedY = verticalIndex;
			}
		} else {
			selectedX = horizontalIndex;
			selectedY = verticalIndex;
			if (getSelectedRegion () != null) {
				if (getSelectedRegion ().getStartX () == selectedX && getSelectedRegion ().getStartY () == selectedY) {
					//CLICKCEPTION!
					BufferedImage[][] parsedImages = ((Tileset)(getElements ()[selectedY][selectedX])).getParsedImages ();
					if (!(parsedImages.length == 1 && parsedImages [0].length == 1)) {
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
		if (/*!hasTileset (workingSet)*/true) {
			if (objCount >= getElements ().length * getElements ()[0].length) {
				addRow ();
			}
			getElements ()[(objCount) / getGridWidth ()][(objCount) % getGridWidth ()] = getElements ()[((objCount - 1) / getGridWidth ())][(objCount - 1) % getGridWidth ()];
			getElements ()[(objCount - 1) / getGridWidth ()][(objCount - 1) % getGridWidth ()] = (DisplayableElement)workingSet;
			objCount ++;
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
		Rectangle bounds = region.getBounds ();
		Rectangle componentBounds = getBoundingRectangle ();
		Graphics g = getGraphics ();
		g.setColor (new Color (0x0000FF));
		g.drawRect (bounds.x, bounds.y, bounds.width, bounds.height);
		g.drawRect (bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
	}
}
