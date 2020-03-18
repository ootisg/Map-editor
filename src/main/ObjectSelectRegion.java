package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFileChooser;

import toolbar.PlaceButton;

public class ObjectSelectRegion extends ScrollableSelectionRegion {
	
	private int objCount;
	
	private int selectedX = -1;
	private int selectedY = -1;
	
	public ObjectSelectRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		resetObjs ();
		
	}
	
	public void resetObjs () {
		GameObject[][] toUse = new GameObject[getGridHeight ()][getGridWidth ()];
		toUse[0][0] = new ObjectAddButton (this);
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
				addGameObject (getWindow ().chooser.getSelectedFile ().getPath ());
			}
			if (selectedX == -1) {
				selectedX = horizontalIndex;
				selectedY = verticalIndex;
				PlaceButton.tilesOrObjects = true;
			}
		} else {
			PlaceButton.tilesOrObjects = true;
			selectedX = horizontalIndex;
			selectedY = verticalIndex;
			if (getElements ()[selectedY][selectedX] != null) {
				select (new TileRegion (selectedX, selectedY, 1, 1));
			}
		}
	}
	
	public GameObject getObject (String path) {
		return new GameObject (path, this);
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
	
	public void addGameObject (String path) {
		GameObject workingSet = getObject (path);
			if (objCount >= getElements ().length * getElements ()[0].length) {
				addRow ();
			}
			getElements ()[(objCount) / getGridWidth ()][(objCount) % getGridWidth ()] = getElements ()[((objCount - 1) / getGridWidth ())][(objCount - 1) % getGridWidth ()];
			getElements ()[(objCount - 1) / getGridWidth ()][(objCount - 1) % getGridWidth ()] = (DisplayableElement)workingSet;
			objCount ++;
	}
	
	public GameObject getSelectedObject () {
		if (selectedX != -1) {
			GameObject working = (GameObject)getElements ()[selectedY][selectedX];
			return (GameObject) working.clone();
		} else {
			return null;
		}
	}
	
}
