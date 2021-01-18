package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;

public class AttributeSelectRegion extends SelectionRegion {
	ArrayList<String> currentAttributes = new ArrayList<String>();
	String name;
	GameObject currentObject;
	Query query;
	private DisplayBox tbox;
	public AttributeSelectRegion (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		query = new Query (new Rectangle (this.getBoundingRectangle().x,this.getBoundingRectangle().y - 10,100,30),this);
		this.setGridDimensions(10, 1);
		tbox = getMainPanel ().addDisplayBox (new Rectangle (0, 0, 60, 16), "HIYA");
		tbox.setBgColor(new Color (0x77787d));
		tbox.hide ();
		query.hide();
	}
	@Override
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		try {
		int elementIndex = getElementIndex (horizontalIndex, verticalIndex);
		VariantConfig c;
		try {
			c = new VariantConfig("resources/objects/variants/config/" + currentObject.getObjectName() + ".txt");
			currentObject.setVariantInfo(name, currentAttributes.get(elementIndex));
			currentObject.setIcon(c.getIcon(currentObject.getVariantInfo()));
		} catch (NoSuchFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} catch (IndexOutOfBoundsException e) {
			query.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x + ((getElementIndex(horizontalIndex,verticalIndex)) * 16),this.getBoundingRectangle().y - 10,100,30));
			query.show();
			query.start();
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
			tbox.setMessage(currentAttributes.get((this.getWindow().getMouseX() - this.getBoundingRectangle().x)/this.getElementWidth()));
			tbox.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x + (this.getWindow().getMouseX() - this.getBoundingRectangle().x), this.getBoundingRectangle().y + 16, 60, 16));
			tbox.show();			
		} else {
			tbox.hide();
		}
		if (query.isFinished()) {
			currentObject.setStrangeVariantInfo(name, query.getValue());
			query.start();
		}
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
}
