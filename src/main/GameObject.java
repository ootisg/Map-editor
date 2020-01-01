package main;

import java.awt.image.BufferedImage;

public class GameObject extends DisplayableImageElement {
	
	private boolean mapObj = false;
	private int x;
	private int y;
	
	public GameObject (BufferedImage img, GuiComponent parent) {
		super (img, parent);
	}
	
	public void setCoords (int x, int y) {
		this.x = x;
		this.y = y;
		this.mapObj = true;
	}
	
	public boolean isMapObject () {
		return mapObj;
	}
}
