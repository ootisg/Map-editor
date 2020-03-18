package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
	@Override 
	protected Object clone () {
		GameObject working = new GameObject (this.getPath(),this.getParent());
		Iterator<String> iter;
		iter = this.getNameList().iterator();
		while (iter.hasNext()) {
			String worker = iter.next();
			working.setVariantInfo(worker, this.getVariantInfo().get(worker));
		}
		working.setCoords(x, y);
		working.mapObj = this.isMapObject();
		return working;
	}
	public String getPath () {
		return filepath;
	}
	public HashMap <String, String> getVariantInfo () {
		return variantInfo;
	}
	public void setVariantInfo (String name, String attribute){
		if (!this.getNameList().contains(name)) {
		variantInfo.put(name, attribute);
		nameList.add(name);
		} else {
			variantInfo.remove(name);
			variantInfo.put(name, attribute);
		}
	}
	public ArrayList <String> getNameList(){
		return nameList;
	}
	public void setCoords (int newX, int newY) {
		this.x = newX;
		this.y = newY;
		this.mapObj = true;
	}
	public int getX () {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public String getObjectName () {
		String working = this.getPath().split("\\\\|/")[this.getPath().split("\\\\|/").length -1];
		return (working.substring(0, working.length() - 4));
	}
	public boolean isMapObject () {
		return mapObj;
	}
	
}
