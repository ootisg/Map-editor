package main;

import java.awt.image.BufferedImage;
import java.nio.file.NoSuchFileException;
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
	HashMap <String, String> strangeVariantInfo = new HashMap<String, String>();
	private ArrayList  <String> nameList= new ArrayList<String>();
	private ArrayList <String> strangeNameList = new ArrayList<String> ();
	static BufferedImage loadBuffer = null;
	
	public GameObject (BufferedImage img, GuiComponent parent) {
		super (img, parent);
		
	}
	public void refreshIcon () {
		try {
		this.setIcon(this.getConfig().getIcon(getVariantInfo()));
		} catch (NullPointerException e) {
			
		}
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
	public Object clone () {
		GameObject working = new GameObject (this.getPath(),this.getParent());
		Iterator<String> iter;
		iter = this.getNameList().iterator();
		while (iter.hasNext()) {
			String worker = iter.next();
			working.setVariantInfo(worker, this.getVariantInfo().get(worker));
		}
		Iterator<String> iter2;
		iter2 = this.getStrangeNameList().iterator();
		while(iter2.hasNext()) {
			String worker = iter2.next();
			working.setStrangeVariantInfo(worker, this.getStrangeVariantInfo().get(worker));
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
	public VariantConfig getConfig () {
		try {
		return new VariantConfig ("resources\\objects\\variants\\config\\" + this.getObjectName() + ".txt");
		} catch (NoSuchFileException e) {
			return null;
		}
		}
	public void changeIcon (BufferedImage img) {
		this.setIcon(img);
	}
	public void setVariantInfo (String name, String attribute){
		if (!this.getNameList().contains(name)) {
			if (this.getStrangeNameList().contains(name)) {
				strangeVariantInfo.remove(name);
				strangeNameList.remove(name);
			}
		variantInfo.put(name, attribute);
		nameList.add(name);
		} else {
			variantInfo.remove(name);
			variantInfo.put(name, attribute);
		}
	}
	public HashMap<String, String> getStrangeVariantInfo () {
		return strangeVariantInfo;
	}
	public void setStrangeVariantInfo (String name, String attribute){
		if (!this.getStrangeNameList().contains(name)) {
			if (this.getNameList().contains(name)) {
				variantInfo.remove(name);
				nameList.remove(name);
			}
		strangeVariantInfo.put(name, attribute);
		strangeNameList.add(name);
		} else {
			strangeVariantInfo.remove(name);
			strangeVariantInfo.put(name, attribute);
		}
	}
	public ArrayList <String> getNameList(){
		return nameList;
	}
	public ArrayList <String> getStrangeNameList (){
		return strangeNameList;
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
