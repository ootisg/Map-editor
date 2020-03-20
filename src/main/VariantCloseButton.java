package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import resources.Sprite;

public class VariantCloseButton extends SelectionRegion {
	public VariantCloseButton (Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		this.setElementWidth(16);
		this.setGridDimensions(1, 1);
		DisplayableElement [][] working = new DisplayableElement[1][1];
		DisplayableImageElement workingElement = new DisplayableImageElement (parent);
		Sprite loadImg = null;
		loadImg = new Sprite ("resources/images/remove_icon.png");
		BufferedImage loadBuffer = loadImg.getImageArray ()[0];
		workingElement.setIcon (loadBuffer);
		working[0][0] = workingElement;
		this.setElements(working);
	}
	@Override 
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		this.getMainPanel().getVariantMenu().hide();
		this.hide();
	}
}
