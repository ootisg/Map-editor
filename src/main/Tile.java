package main;

import java.awt.image.BufferedImage;

public class Tile extends DisplayableImageElement {
	
	private String filepath;
	
	public Tile (BufferedImage img, GuiComponent parent) {
		super (img, parent);
	}
}
