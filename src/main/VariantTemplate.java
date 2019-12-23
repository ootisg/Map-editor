package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import resources.Sprite;
import resources.Spritesheet;

public class VariantTemplate {
	String objectName;
	String args;
	String[] variantList;
	ArrayList<ArrayList<String>> attributeList;
	String[] displayTypes;
	ArrayList<Sprite> iconList;
	ArrayList<String> nameList;
	ArrayList<String> queryList;
	public VariantTemplate (String objectName, String args) {
		this.objectName = objectName;
		this.args = args;
		String[] argsArray = args.split ("\r\n");
		variantList = new String[argsArray.length];
		attributeList = new ArrayList<ArrayList<String>> ();
		queryList = new ArrayList<String> ();
		displayTypes = new String[argsArray.length];
		for (int i = 0; i < argsArray.length; i ++) {
			if (argsArray [i].split (";").length == 1) {
				variantList [i] = argsArray [i].split (":") [0];
				attributeList.add (null);
				displayTypes [i] = argsArray [i].split (":") [1];
			} else {
				variantList [i] = argsArray [i].split (";") [0];
				String[] attributeArray = argsArray [i].substring (variantList [i].length () + 1).split (":") [0].split (",");
				attributeList.add (new ArrayList<String> ());
				for (int j = 0; j < attributeArray.length; j ++) {
					attributeList.get (i).add (attributeArray [j]);
				}
				displayTypes [i] = argsArray [i].split (":") [1];
			}
		}
		for (int i = 0; i < displayTypes.length; i ++) {
			if (displayTypes [i].equals ("query")) {
				queryList.add (variantList [i]);
				attributeList.set (i, new ArrayList<String> ());
			}
		}
		if (indexOf (displayTypes, "variant") == -1) {
			
		}
		iconList = new ArrayList<Sprite> ();
		nameList = new ArrayList<String> ();
		int frameDiv = -1;
		int frameDivIndex = -1;
		int frameMod = -1;
		int frameModIndex = -1;
		String[] workingParse;
		if (indexOf (displayTypes, "frame") != -1) {
			frameDiv = 1;
			frameDivIndex = indexOf (displayTypes, "frame");
		} else {
			for (int i = 0; i < displayTypes.length; i ++) {
				workingParse = displayTypes [i].split ("/");
				if (workingParse.length == 2) {
					if (workingParse [0].equals ("frame")) {
						frameDiv = Integer.parseInt (workingParse [1]);
						frameDivIndex = i;
					}
				}
			}
			for (int i = 0; i < displayTypes.length; i ++) {
				workingParse = displayTypes [i].split ("%");
				if (workingParse.length == 2) {
					if (workingParse [0].equals ("frame")) {
						frameMod = Integer.parseInt (workingParse [1]);
						frameModIndex = i;
					}
				}
			}
		}
		int variantIndex;
		int variantCount = 1;
		variantIndex = indexOf (displayTypes, "variant");
		if (variantIndex != -1) {
			variantCount = attributeList.get (variantIndex).size ();
		}
		for (int i = 0; i < variantCount; i ++) {
			Spritesheet workingSheet;
			if (variantIndex == -1) {
				workingSheet = new Spritesheet ("resources/objects/variants/icons/" + objectName + ".nv.png");
			} else {
				workingSheet = new Spritesheet ("resources/objects/variants/icons/" + objectName + "." + attributeList.get (variantIndex).get (i) + ".png");
			}
			Sprite workingSprites = new Sprite (workingSheet, 16, 16);
			BufferedImage[] workingImages = workingSprites.getImageArray ();
			for (int j = 0; j < workingImages.length; j ++) {
				iconList.add (new Sprite (workingImages [j]));
				String workingAttributes = "";
				if (variantIndex != -1) {
					workingAttributes = variantList [variantIndex] + ":" + attributeList.get (variantIndex).get (i) + "&";
				}
				if (frameDivIndex != -1) {
					workingAttributes += (variantList [frameDivIndex] + ":" + attributeList.get (frameDivIndex).get (j / frameDiv)) + "&";
				}
				if (frameModIndex != -1) {
					workingAttributes += (variantList [frameModIndex] + ":" + attributeList.get (frameModIndex).get (j % frameMod)) + "&";
				}
				for (int k = 0; k < queryList.size (); k ++) {
					workingAttributes += (queryList.get (k) + ":null&");
				}
				if (workingAttributes.length () != -1) {
					workingAttributes = workingAttributes.substring (0, workingAttributes.length () - 1);
				}
				nameList.add (workingAttributes);
			}
		}
	}
	public void addVariant (String name, Sprite icon) {
		nameList.add (name);
		iconList.add (icon);
	}
	public Sprite getIcon (String name) {
		String[] attributeList = name.split ("&");
		ArrayList<String> iconAttributeNames = new ArrayList<String> ();
		ArrayList<String> iconAttributes = new ArrayList<String> ();
		for (int i = 0; i < displayTypes.length; i ++) {
			if (displayTypes [i].contains ("variant") || displayTypes [i].contains ("frame")) {
				iconAttributeNames.add (variantList [i]);
			}
		}
		for (int i = 0; i < attributeList.length; i ++) {
			//String[] workingAttribute = attributeList [i].split (":");
			//System.out.println(attributeList[i]);
			for (int j = 0; j < iconAttributeNames.size (); j ++) {
				if (attributeList [i].split (":")[0].equals (iconAttributeNames.get (j))) {
					iconAttributes.add (attributeList [i]);
				}
			}
		}
		if (iconAttributes.size () == 0) {
			return iconList.get (0);
		} else {
			for (int i = 0; i < nameList.size (); i ++) {
				String workingName = nameList.get (i);
				int attributeMatches = 0;
				for (int j = 0; j < iconAttributes.size (); j ++) {
					if (workingName.contains (iconAttributes.get (j))) {
						attributeMatches ++;
					}
				}
				if (attributeMatches == iconAttributeNames.size ()) {
					return iconList.get (i);
				}
			}
		}
		return iconList.get (0);
	}
	private int indexOf (String[] arr, String str) {
		for (int i = 0; i < arr.length; i ++) {
			if (arr [i].equals (str)) {
				return i;
			}
		}
		return -1;
	}
}