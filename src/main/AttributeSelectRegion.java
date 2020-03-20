package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class AttributeSelectRegion extends SelectionRegion {
	ArrayList<String> currentAttributes = new ArrayList<String>();
	String name;
	GameObject currentObject;
	private DisplayBox tbox;
	public AttributeSelectRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		this.setGridDimensions(10, 1);
		tbox = getMainPanel ().addDisplayBox (new Rectangle (0, 0, 60, 16), "HIYA");
		tbox.setBgColor(new Color (0x1D0EC3));
		tbox.hide ();
		
	}
	@Override
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		try {
		int elementIndex = getElementIndex (horizontalIndex, verticalIndex);
		VariantConfig c = new VariantConfig("resources/objects/variants/config/" + currentObject.getObjectName() + ".txt");
		currentObject.setVariantInfo(name, currentAttributes.get(elementIndex));
		currentObject.setIcon(c.getIcon(currentObject.getVariantInfo()));
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
	public void setCurrentInfo(ArrayList <String> attributes, String Name) {
		name = Name;
		currentAttributes = attributes;
	}
	public void setObject (GameObject object) {
		currentObject = object;
	}
	@Override 
	public void frameEvent () {
		try {
		if (this.mouseInside() && !this.isHidden()) {
			tbox.setMessage(currentAttributes.get((this.getWindow().getMouseX() - this.getBoundingRectangle().x)/16));
			tbox.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x + (this.getWindow().getMouseX() - this.getBoundingRectangle().x), this.getBoundingRectangle().y + 16, 60, 16));
			tbox.show();			
		} else {
			tbox.hide();
		}
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
}
