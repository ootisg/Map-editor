package main;

import java.awt.image.BufferedImage;

import resources.Sprite;

public class VariantAddButton extends DisplayableImageElement {
	public VariantAddButton (GuiComponent parent) {
		super (parent);
		Sprite loadImg = null;
		loadImg = new Sprite ("resources/images/add_icon.png");
		BufferedImage loadBuffer = loadImg.getImageArray ()[0];
		setIcon (loadBuffer);
	}

}
