package main;


import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
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
	 int variantNumber = 0;
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
	public void reset () {
		this.setElementWidth(96);
		this.setGridDimensions(1, 10);
		this.resetVars();
	}
	public void changeDisplayingVariants() {
	
		try {
		if (variantNumber >= MainPanel.getMapInterface().getSelectedRegion().getSelectedGameObjects().size()) {
			variantNumber = 0;
		} 
		} catch (NullPointerException e) {
			return;
		}
		GameObject selectedGameObject;
		try {
		selectedGameObject = MainPanel.getMapInterface().getSelectedRegion().getSelectedGameObjects().get(variantNumber);
		} catch (IndexOutOfBoundsException e) {
			return;
		}
		String oldString = selectedGameObject.getPath();
		GameObject oldObject;
		if (currentObject != null) {
		oldObject = (GameObject) currentObject;
		} else {
		oldObject =	selectedGameObject; 
		}
		currentObject =  selectedGameObject; 
		if (!oldObject.equals(currentObject)) {
			names = new ArrayList <String>();
			nameToAttributes = new HashMap<String,ArrayList<String>>();
			this.getMainPanel().getAttributeSelectRegion().hide();
		}
		int i = oldString.length() - 1;
		while (oldString.charAt(i) != '\\' && oldString.charAt(i) != '/'  ) {
			i = i - 1;
		}
		String objectType =oldString.substring(i + 1, oldString.length() - 4) + ".txt";
		oldString = oldString.substring(0,oldString.length() - objectType.length()) + "variants\\config\\" + objectType;
		try {
		c = new VariantConfig (oldString);
		} catch (Exception e) {
			variantNumber = variantNumber + 1;
			return;
		}
		ArrayList <String> variantNames = c.getAttributeNames();
		Iterator <String> iter = variantNames.iterator();
		DisplayableTextElement [][] working = new DisplayableTextElement [variantNames.size() + 1][1];
		int crap = 0;
		int longestLength = 0;
		while(iter.hasNext()) {
			String currentName = iter.next();
			working[crap][0] = new DisplayableTextElement (this,currentName);
			if (currentName.length() > longestLength) {
				 longestLength = currentName.length();
			}
			if (!names.contains(currentName)) {
			names.add(currentName);
			}
			// c.getAllowedValues is what I need to check out for quereys
			nameToAttributes.put(currentName, c.getAllowedValues(currentName));
			crap = crap + 1;
		}
		working [crap][0]  = new DisplayableTextElement (this,"Switch");
		if (5 > longestLength) {
			longestLength = 5;
		}
		crap = crap + 1;
		if (crap != 0) {
		this.getMainPanel().getVariantCloseButton().setBoundingRectangle(new Rectangle(((VariantSelectMenu) realParent).getMenuX() + (longestLength * 6), ((VariantSelectMenu) realParent).getMenuY() + 16,16,16));
		this.setBoundingRectangle(new Rectangle (((VariantSelectMenu) realParent).getMenuX(), ((VariantSelectMenu) realParent).getMenuY() + 32, (longestLength * 6) + 16, (crap* 16) + 16));
		this.setGridDimensions(1, crap);
		this.setElementWidth(longestLength * 6);
		this.setElements(working);	
		realParent.setBoundingRectangle(new Rectangle (((VariantSelectMenu) realParent).getMenuX(), ((VariantSelectMenu) realParent).getMenuY() + 16, (longestLength * 6) + 16, (crap* 16) + 16));
		}
	}
	@Override
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		try {
			int elementIndex = getElementIndex (horizontalIndex, verticalIndex);
			if (elementIndex == names.size()) {
				variantNumber = variantNumber + 1;
				this.changeDisplayingVariants();
			}
			ArrayList <String> currentAttributes = nameToAttributes.get(((DisplayableTextElement) this.getElements()[elementIndex][0]).getMessage());
			AttributeSelectRegion region = this.getMainPanel().getAttributeSelectRegion();
			region.setBoundingRectangle(new Rectangle (((VariantSelectMenu) realParent).getMenuX() + 16 + this.getElementWidth(), ((VariantSelectMenu) realParent).getMenuY() + 32+ (elementIndex * 16),((currentAttributes.size()) * 16) + 16, 16));
			int index = 0;
			DisplayableImageElement [][] icons = new DisplayableImageElement [1][20];
			HashMap<String,String> temporaryInfo = ((GameObject) currentObject.clone()).getVariantInfo();
			while (index < currentAttributes.size()) {
			temporaryInfo.put(names.get(elementIndex),currentAttributes.get(index) );
			icons[0][index] = new DisplayableImageElement (c.getIcon(temporaryInfo),region);
			index = index + 1;
			}
			VariantAddButton button = new VariantAddButton(region);
			icons[0][index] = new DisplayableImageElement (button.getIcon(),region);
			region.setCurrentInfo(currentAttributes, names.get(elementIndex));
			region.setObject(currentObject);
			region.setElements(icons);
			region.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}