package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import resources.Sprite;

import java.awt.image.BufferedImage;

public class VariantConfig {

	private String configFile;
	
	private Scanner configScanner;
	private ArrayList<String> attributeNames;
	private HashMap<String, ArrayList<String>> possibleArgs = null;
	
	public VariantConfig (String filepath) throws NoSuchFileException{
		try {
			configFile = new String (Files.readAllBytes (Paths.get (filepath)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new NoSuchFileException (filepath);
		}
		attributeNames = new ArrayList<String> ();
		configScanner = new Scanner (configFile);
		configScanner.nextLine ();
		String variant;
		possibleArgs = new HashMap<String, ArrayList<String>> ();
		while (configScanner.hasNextLine ()) {
			variant = configScanner.nextLine ();
			LinkedList<String> tokens = getTokens (variant);
			Iterator<String> tokenIter = tokens.iterator ();
			String currName = null;
			ArrayList<String> attributes = new ArrayList<String> ();
			while (tokenIter.hasNext ()) {
				String curr = tokenIter.next ();
				String[] varSplit = curr.split ("=");
				if (varSplit.length >= 2) {
					if (varSplit [0].equals ("name")) {
						currName = varSplit [1];
						attributeNames.add (varSplit [1]);
					} else if (varSplit [0].equals ("attributes")) {
						String attributesString = curr.substring (11);
						String[] attributesSplit = removeBrackets (attributesString).split (" ");
						for (int i = 0; i < attributesSplit.length; i ++) {
							String[] lolzSplit = attributesSplit [i].split ("=");
							attributes.add (lolzSplit [0]);
						}
					}
				}
			}
			possibleArgs.put (currName, attributes);
		}
	}
	
	public BufferedImage getIcon (HashMap<String, String> variantAttributes) {
		
		//Initialize variable map and default values
		HashMap<String, String> varMap = new HashMap<String, String> ();
		varMap.put ("name", "NULL");
		varMap.put ("query", "FALSE");
		varMap.put ("attributes", "NOT_SET");
		varMap.put ("value", "NV");
		varMap.put ("filename", "NULL");
		varMap.put ("frame", "0");
		varMap.put ("tileIndex", "0");
		varMap.put ("tileWidth", "16");
		varMap.put ("tileHeight", "16");
		varMap.put ("tileX", "NULL");
		varMap.put ("tileY", "NULL");
		varMap.put ("iconX", "NULL");
		varMap.put ("iconY", "NULL");
		
		//Parse out file/icon info
		configScanner = new Scanner (configFile);
		int lineNum = 0;
		while (configScanner.hasNextLine ()) {
			String line = configScanner.nextLine ();
			if (lineNum == 0) {
				//TODO header and version check
			} else {
				String firstArg = line.split (" ")[0];
				String[] assignmentVals = firstArg.split ("=");
				String usedName = null;
				String usedValue;
				if (assignmentVals [0].equals ("name")) {
					usedValue = variantAttributes.get (assignmentVals [1]);
					if (usedValue == null) {
						ArrayList<String> argsList = possibleArgs.get (assignmentVals [1]);
						if (argsList != null && argsList.size () > 0) {
							usedValue = argsList.get (0);
						}
					}
					varMap.put ("value", usedValue);
				} else {
					varMap.put ("name", "NULL");
					varMap.put ("value", "NULL");
				}
				parse (line, varMap);
			}
			lineNum ++;
		}
		
		//Get the image
		String tileIndex = varMap.get ("tileIndex");
		String tileX = varMap.get ("tileX");
		String tileY = varMap.get ("tileY");
		String iconX = varMap.get ("iconX");
		String iconY = varMap.get ("iconY");
		int tileWidth = Integer.parseInt (varMap.get ("tileWidth"));
		int tileHeight = Integer.parseInt (varMap.get ("tileHeight"));
		String filename = varMap.get ("filename");
		Sprite loadSrc = new Sprite ("resources/objects/variants/icons/" + filename);
		BufferedImage loadImg = loadSrc.getImageArray ()[0];
		//Find the region to parse out and return it
		if (!(iconX.equals ("NULL")) && !(iconY.equals ("NULL"))) {
			int xGet = Integer.parseInt (iconX);
			int yGet = Integer.parseInt (iconY);
			return loadImg.getSubimage (xGet, yGet, tileWidth, tileHeight);
		} else if (!(tileX.equals ("NULL")) && !(tileY.equals ("NULL"))) {
			int xGet = Integer.parseInt (tileX) * tileWidth;
			int yGet = Integer.parseInt (tileY) * tileHeight;
			return loadImg.getSubimage (xGet, yGet, tileWidth, tileHeight);
		} else if (!(tileIndex.equals ("NULL"))) {
			int rowLength = loadImg.getWidth () / tileWidth;
			int xGet = (Integer.parseInt (tileIndex) % rowLength) * tileWidth;
			int yGet = (Integer.parseInt (tileIndex) / rowLength) * tileHeight;
			return loadImg.getSubimage (xGet, yGet, tileWidth, tileHeight);
		} else {
			return loadImg;
		}
		
	}
	
	public ArrayList<String> getAttributeNames () {
		return attributeNames;
	}
	
	public ArrayList<String> getAllowedValues (String attributeName) {
		return possibleArgs.get (attributeName);
	}
	
	private String parse (String str, HashMap<String, String> varMap) {
		
		//Set defaults
		if (varMap.get ("value") == null) {
			varMap.put ("value", "NULL");
		}
		
		LinkedList<String> elements = getTokens (str);
		LinkedList<String> tokens = new LinkedList<String> ();
		LinkedList<String> parsedTokens = new LinkedList<String> ();
		Iterator<String> elementIter = elements.iterator ();
		
		//Loop through elements
		while (elementIter.hasNext ()) {
			tokens = new LinkedList<String> ();
			String working = "";
			str = elementIter.next ();
			if (str.contains ("{")) {
				if (str.split ("=")[0].equals ("attributes")) {
					varMap.put ("attributes", "set");
					String attributeValsRaw = str.substring (11);
					String attributeVals = removeBrackets (attributeValsRaw);
					String[] valsList = attributeVals.split (" ");
					String variantValue = varMap.get ("value");
					for (int i = 0; i < valsList.length; i ++) {
						if (valsList [i].split ("=").length == 2 && valsList [i].split ("=")[0].equals (variantValue)) {
							varMap.put ("value", valsList [i].split ("=")[1]);
						}
					}
				} else {
					for (int i = 0; i < str.length (); i ++) {
						
						//Separate out tokens
						if (str.charAt (i) == '{') {
							//Yeet working into the list thing
							if (!(working.equals (""))) {
								tokens.add (working);
								working = "";
							}
							//Parse out the bracketed section
							int depth = 0;
							do {
								char c = str.charAt (i);
								if (c == '{') {
									depth ++;
								} else if (c == '}') {
									depth --;
								} else if (depth != 0) {
									working += c;
									if (i == str.length () - 1) {
										//Should throw an error here
										return null;
									}
								} 
								i ++;
							} while (depth != 0);
							tokens.add (parse (working, varMap));
							working = "";
						} else {
							working += str.charAt (i);
							if (i == str.length () - 1 && !(working.equals (""))) {
								tokens.add (working);
							}
						}
					}
				}
				//Reconstruct the string with bracketed sections
				Iterator<String> iter = tokens.iterator ();
				working = "";
				while (iter.hasNext ()) {
					working += iter.next ();
				}
			} else {
				working = str;
			}
			
			String cur = working;
			if (cur.contains ("=")) {
				String[] exprSplit = cur.split ("=");
				if (exprSplit.length == 2) {
					varMap.put (exprSplit [0], getVariable (exprSplit [1], varMap));
					//Do not add to evaluated tokens
				}
			} else {
				if (isSpecialToken (cur)) {
					//Special token logic
					String b = getVariable (parsedTokens.removeLast (), varMap);
					String a = getVariable (parsedTokens.removeLast (), varMap);
					parsedTokens.add (getOperationResult (cur, a, b));
				} else {
					//Add string the list of arguments
					parsedTokens.add (cur);
				}
			}
		}
		
		//Piece together tokens
		String working = "";
		Iterator<String> iter = parsedTokens.iterator ();
		//System.out.println (parsedTokens);
		while (iter.hasNext ()) {
			String curr = iter.next ();
			if (!curr.equals ("")) {
				curr = getVariable (curr, varMap);
				working += removeQuotes (curr);
			}
		}
		//System.out.println (working);
		return working;
	}
	
	//Completely replaced by getTokens
	/*private String getArgument (ReadState s) {
		int depth = 0;
		String str = s.str;
		String working = "";
		while (true) {
			if (depth == 0) {
				while (str.charAt (s.pos) != ' ' && s.pos < str.length () - 1) {
					if (str.charAt (s.pos) == '{') {
						depth = 1;
						s.pos ++;
						break;
					}
					working += str.charAt (s.pos);
					s.pos ++;
				}
				if (depth == 0) {
					while (str.charAt (s.pos) == ' ' && s.pos < str.length () - 1) {
						s.pos ++;
					}
					return working;
				}
			} else {
				while (true) {
					if (str.charAt (s.pos) == '}') {
						depth --;
						if (depth == 0) {
							return working;
						}
					} else {
						working += str.charAt (s.pos);
						if (str.charAt (s.pos) == '{') {
							depth ++;
						}
					}
				}
			}
		}
	}*/
	
	private String getVariable (String str, HashMap<String, String> varMap) {
		if (str.charAt (0) == '$') {
			str = str.substring (1);
			if (varMap.get (str) != null) {			
				str = '\"' + varMap.get (str) + '\"';
			}
		} else {
			if (varMap.get (str) != null) {
				str = varMap.get (str);
			}
		}
		return str;
	}
	
	private LinkedList<String> getTokens (String str) {
		LinkedList<String> tokens = new LinkedList<String> ();
		String working = "";
		boolean whitespaceMode = false;
		int depth = 0;
		for (int i = 0; i < str.length (); i ++) {
			if (whitespaceMode) {
				if (i == str.length () - 1) {
					tokens.add (String.valueOf (str.charAt (i)));
					break;
				}
				if (str.charAt (i) != ' ') {
					whitespaceMode = false;
				}
			}
			if (!whitespaceMode) {
				if (i == str.length () - 1 && !working.equals ("") && str.charAt (i) != ' ') {
					working += str.charAt (i);
					tokens.add (working);
					break;
				}
				if (depth == 0 && str.charAt (i) == ' ') {
					whitespaceMode = true;
					tokens.add (working);
					working = "";
				} else {
					if (str.charAt (i) == '{') {
						depth ++;
					} else if (str.charAt (i) == '}') {
						if (depth <= 0) {
							//ERROR
						} else {
							depth --;
						}
					}
				}
			}
			if (!whitespaceMode) {
				working += str.charAt (i);
			}
		}
		return tokens;
	}
	
	private boolean isSpecialToken (String str) {
		if (str.length () == 1) {
			char c = str.charAt (0);
			if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
				return true;
			}
		}
		return false;
	}
	
	private boolean isInt (String str) {
		for (int i = 0; i < str.length (); i ++) {
			char c = str.charAt (i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}
	
	private String removeQuotes (String str) {
		if (str.charAt (0) == '\"' && str.charAt (str.length () - 1) == '\"') {
			return str.substring (1, str.length () - 2);
		} else {
			return str;
		}
	}
	
	private String removeBrackets (String str) {
		if (str.charAt (0) == '{' && str.charAt (str.length () - 1) == '}') {
			return str.substring (1, str.length () - 1);
		} else {
			return str;
		}
	}
	
	private String getOperationResult (String op, String a, String b) {
		if (op.length () == 1) {
			char opChar = op.charAt (0);
			if (isInt (a) && isInt (b)) {
				int aval = Integer.parseInt (a);
				int bval = Integer.parseInt (b);
				switch (opChar) {
					case '+':
						return String.valueOf (aval + bval);
					case '-':
						return String.valueOf (aval - bval);
					case '*':
						return String.valueOf (aval * bval);
					case '/':
						return String.valueOf (aval / bval);
					case '%':
						return String.valueOf (aval % bval);
					default:
						return null;
				}
			} else if (opChar == '+') {
				String res = removeQuotes (a) + removeQuotes (b);
				if (isInt (res)) {
					return '\"' + res + '\"';
				} else {
					return res;
				}
			} else if (opChar == '*' && isInt (b)) {
				String res = "";
				int bval = Integer.parseInt (b);
				for (int i = 0; i < bval; i ++) {
					res += a;
				}
				if (isInt (res)) {
					return '\"' + res + '\"';
				}
				return res;
			}
		}
		return null;
	}
	
	private String removeSpaces (String str) {
		String res = "";
		for (int i = 0; i < str.length (); i ++) {
			char c = str.charAt (i);
			if (i != ' ') {
				res += str.charAt (i);
			}
		}
		return new String (res);
	}
	
	private class ReadState {
		public String str;
		public int pos;
	}
}