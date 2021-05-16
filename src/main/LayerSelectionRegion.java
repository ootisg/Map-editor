package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import map.Map.TileLayer;

public class LayerSelectionRegion extends ScrollableSelectionRegion {
	int selectedLayerNum = -1;
	int numLayers = 1;
	int nextLayerNum = 1;
	LayerMenu parent;
	protected LayerSelectionRegion(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		this.setGridDimensions(1, 1);
		this.setElementWidth(bounds.width);
		DisplayableTextElement[][] toUse = new DisplayableTextElement[getGridHeight ()][getGridWidth ()];
		toUse [0][0] = new DisplayableTextElement (this, "Layer 1");
		setElements (toUse);
		this.parent = (LayerMenu) parent;
	}
	private DisplayableElement[][] getBiggerGrid (DisplayableElement[][] ogGrid){
		int size = ogGrid.length + 1;
		DisplayableElement[][] grid2ElcBeglo = new DisplayableElement[size][1];
		for (int i = 0; i < size - 1; i++) {
			grid2ElcBeglo[i][0] = ogGrid[i][0];
		}
		return grid2ElcBeglo;
	}
	private DisplayableElement[][] getSmallerGrid (DisplayableElement[][] ogGrid, int elementToRemove){
		int size = ogGrid.length - 1;
		DisplayableElement[][] grid2ElcBeglo = new DisplayableElement[size][1];
		boolean yeet = false;
		for (int i = 0; i < size; i++) {
			if (i != elementToRemove && !yeet) {
				grid2ElcBeglo[i][0] = ogGrid[i][0];
			} else {
				yeet = true;
			}
			if (yeet) {
				grid2ElcBeglo[i][0] = ogGrid[i + 1][0];
			}
		}
		return grid2ElcBeglo;
	}
	@Override
	public void frameEvent() {
		try {
			if (!parent.isHidden()) { 
				if (this.mouseInside() ) {
					MainPanel.getMap().setLayer((this.getWindow().getMouseY() - this.getBoundingRectangle().y)/this.getElementHeight());
					if (!MainPanel.getMap().inLayerMode()) {
						MainPanel.getMap().toggleLayerMode();
					}
				} else {
				
					if (selectedLayerNum != -1) {
						MainPanel.getMap().setLayer(selectedLayerNum);
						if (!MainPanel.getMap().inLayerMode()) {
							MainPanel.getMap().toggleLayerMode();
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			if (selectedLayerNum != -1) {
				MainPanel.getMap().setLayer(selectedLayerNum);
				if (!MainPanel.getMap().inLayerMode()) {
					MainPanel.getMap().toggleLayerMode();
				}
			}
		}
	}
	@Override
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		selectLayer (verticalIndex, true);
	}
	public void unselectLayer () {
		if (selectedLayerNum != -1) {
			DisplayableTextElement elem = (DisplayableTextElement)getElementList ().get (selectedLayerNum);
			elem.setBackgroundColor (new Color (0xA0A0A0));
			selectedLayerNum = -1;
		}
	}
	public void selectLayer (int index, boolean updateArrows) {
		unselectLayer ();
		selectedLayerNum = index;
		if (index != -1) {
			if (updateArrows) {parent.updateArrows (selectedLayerNum);}
			DisplayableTextElement elem = (DisplayableTextElement)getElementList ().get (selectedLayerNum);
			elem.setBackgroundColor (new Color (0x808080));
		}
	}
	public void swapLayers (int index1, int index2) {
		if (index1 >= 0 && index2 >= 0 && index1 < getElements ().length && index2 < getElements ().length) {
			DisplayableElement temp = getElements ()[index1][0];
			getElements ()[index1][0] = getElements ()[index2][0];
			getElements ()[index2][0] = temp;
			unselectLayer ();
			selectLayer (index2, false);
			MainPanel.getMap ().swapLayers (index1, index2);
		}
	}
	public int getSelectedLayer () {
		return selectedLayerNum;
	}
	public void addLayer () {
		numLayers = numLayers + 1;
		nextLayerNum = nextLayerNum + 1;
		this.setGridDimensions(1, numLayers);
		DisplayableElement [][] working = this.getBiggerGrid(this.getElements());
		working[numLayers - 1][0] = new DisplayableTextElement (this, "Layer " + nextLayerNum);
		this.setElements(working);
		MainPanel.getMap().addLayer();
		parent.setBoundingRectangle(new Rectangle (MainPanel.getLayerMenu().getBoundingRectangle().x,MainPanel.getLayerMenu().getBoundingRectangle().y, MainPanel.getLayerMenu().getBoundingRectangle().width, MainPanel.getLayerMenu().getBoundingRectangle().height + 16));
		this.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x,this.getBoundingRectangle().y, this.getBoundingRectangle().width, this.getBoundingRectangle().height + 16));
	}
	public void deleteLayer () {
		if (numLayers != 1 && selectedLayerNum != -1) {
			//deleate from menu
			numLayers = numLayers - 1;
			this.setGridDimensions(1, numLayers);
			
			DisplayableElement [][] working = this.getSmallerGrid(this.getElements(), selectedLayerNum);
			parent.setBoundingRectangle(new Rectangle (MainPanel.getLayerMenu().getBoundingRectangle().x,MainPanel.getLayerMenu().getBoundingRectangle().y, MainPanel.getLayerMenu().getBoundingRectangle().width, MainPanel.getLayerMenu().getBoundingRectangle().height - 16));
			this.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x,this.getBoundingRectangle().y, this.getBoundingRectangle().width, this.getBoundingRectangle().height - 16));  
			this.setElements(working);
			
			
			//deleate from map
			MainPanel.getMap().removeLayer(selectedLayerNum);
			selectedLayerNum = -1;
		}
	}
	
	public void insertLayer (TileLayer layer, int index) {
		
		addLayer ();
		ArrayList<TileLayer> layers = getMainPanel ().getMap ().getTileData ();
		layers.set (layers.size () - 1, layer);
		int curr = layers.size () - 1;
		while (curr != index) {
			selectLayer (curr, false);
			swapLayers (curr, --curr);
		}
		
	}
}
