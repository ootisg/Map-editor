package main;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import resources.Sprite;

public class GameObject extends DisplayableImageElement {
	BufferedImage images;
	private boolean mapObj = false;
	private int x;
	private int y;
	HashMap<String, String> variantInfo = new HashMap<String, String>();
	String filepath = "";
	static BufferedImage loadBuffer = null;
	
	public GameObject (BufferedImage img, GuiComponent parent) {
		super (img, parent);
	}
	public GameObject (String path, GuiComponent parent) {
		super (null, parent);
		Sprite loadImg = null;
		loadImg = new Sprite (path);
		filepath = path; 
		BufferedImage loadBuffer = loadImg.getImageArray ()[0];
		setIcon (loadBuffer);
	}
	public String getPath () {
		return filepath;
	}
	public HashMap <String, String> getVariantInfo () {
		return variantInfo;
	}
	public void setVariantInfo (String name, String attribute){
		variantInfo.put(name, attribute);
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
