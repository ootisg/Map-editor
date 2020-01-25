package main;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFileChooser;

import main.SelectionRegion.TileRegion;
import toolbar.PlaceButton;





public class VariantSelectRegion extends ScrollableSelectionRegion  {
	VariantConfig c;
	GuiComponent realParent;
	ArrayList <String> names = new ArrayList();
	GameObject currentObject;
	 HashMap<String, ArrayList <String>> nameToAttributes  = new HashMap<String, ArrayList<String>>();
	public VariantSelectRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		this.resetVars();
		realParent = parent;
		this.setElementWidth(96);
		this.setGridDimensions(1, 10);
		
	}
	public void resetVars () {
		GameObject[][] toUse = new GameObject[getGridHeight ()][getGridWidth ()];
		setElements (toUse);
	}
	public void changeDisplayingVariants() {
		String oldString = getMainPanel().getMapInterface().getSelectedRegion().getSelectedGameObject().getPath();
		currentObject =  getMainPanel().getMapInterface().getSelectedRegion().getSelectedGameObject(); 
		int i = oldString.length() - 1;
		while (oldString.charAt(i) != '\\' && oldString.charAt(i) != '/'  ) {
			i = i - 1;
		}
		
		String objectType =oldString.substring(i + 1, oldString.length() - 4) + ".txt";
		oldString = oldString.substring(0,oldString.length() - objectType.length()) + "variants\\config\\" + objectType;
		c = new VariantConfig (oldString);
		ArrayList <String> variantNames = c.getAttributeNames();
		Iterator <String> iter = variantNames.iterator();
		DisplayableTextElement [][] working = new DisplayableTextElement [10][1];
		int crap = 0;
		int longestLength = 0;
		while(iter.hasNext()) {
			String currentName = iter.next();
			working[crap][0] = new DisplayableTextElement (this,currentName);
			if (currentName.length() > longestLength) {
				 longestLength = currentName.length();
			}
			names.add(currentName);
			nameToAttributes.put(currentName, c.getAllowedValues(currentName));
			crap = crap + 1;
		}
		this.setBoundingRectangle(new Rectangle (((VariantSelectMenu) realParent).getMenuX(), ((VariantSelectMenu) realParent).getMenuY() + 32, (longestLength * 6) + 16, crap* 16));
		this.setGridDimensions(1, crap);
		this.setElementWidth(longestLength * 6);
		this.setElements(working);	
		realParent.setBoundingRectangle(new Rectangle (((VariantSelectMenu) realParent).getMenuX(), ((VariantSelectMenu) realParent).getMenuY() + 16, (longestLength * 6) + 16, (crap* 16) + 16));
	}
	@Override
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		int elementIndex = getElementIndex (horizontalIndex, verticalIndex);
		ArrayList <String> currentAttributes = nameToAttributes.get(((DisplayableTextElement) this.getElements()[elementIndex][0]).getMessage());
		AttributeSelectRegion region = this.getMainPanel().getAttributeSelectRegion();
		region.setBoundingRectangle(new Rectangle (((VariantSelectMenu) realParent).getMenuX() + 16 + this.getElementWidth(), ((VariantSelectMenu) realParent).getMenuY() + 32+ (elementIndex * 16),(currentAttributes.size()) * 16 , 16));
		int index = 0;
		DisplayableImageElement [][] icons = new DisplayableImageElement [1][10];
		while (index < currentAttributes.size()) {
		HashMap<String,String> temporaryInfo = new HashMap<String, String> ();
		temporaryInfo.put(names.get(elementIndex),currentAttributes.get(index) );
		icons[0][index] = new DisplayableImageElement (c.getIcon(temporaryInfo),region);
		index = index + 1;
		}
		region.setCurrentInfo(currentAttributes, names.get(elementIndex));
		region.setObject(currentObject);
		region.setElements(icons);
		region.show();
	}

}
