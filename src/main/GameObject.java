package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import resources.Sprite;

public class GameObject extends DisplayableImageElement {
	BufferedImage images;
	private boolean mapObj = false;
	private int x;
	private int y;
	HashMap<String, String> variantInfo = new HashMap<String, String>();
	String filepath = "";
	private ArrayList  <String> nameList= new ArrayList<String>();
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
		nameList.add(name);
	}
	public ArrayList <String> getNameList(){
		return nameList;
	}
	public void setCoords (int x, int y) {
		this.x = x;
		this.y = y;
		this.mapObj = true;
	}
	public int getX () {
		return x;
	}
	public int getY() {
		return y;
	}
	public String getObjectName () {
		String working = this.getPath().split("\\\\|/")[this.getPath().split("\\\\|/").length -1];
		return (working.substring(0, working.length() - 4));
	}
	public boolean isMapObject () {
		return mapObj;
	}
	
}
