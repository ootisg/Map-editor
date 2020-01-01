package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class VariantConfig {

	private Scanner configScanner;
	private LinkedList<String> args;
	
	private int index;
	
	public VariantConfig (String filepath) {
		try {
			configScanner = new Scanner (new File (filepath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		index = 0;
		args = new LinkedList<String> ();
		String variant;
		while (configScanner.hasNext ()) {
			variant = configScanner.next ();
			
		}
	}
	
	private String parse (String str, HashMap<String, String> varMap) {
		/*LinkedList<String> elements = new LinkedList<String>;
		String working = "";
		if (str.contains ("=")) {
			String[] parsevars = str.split ("=");
			String value = parsevars [1];
			varMap.put (parsevars [0], parse (value, varMap));
		}
		if (str.contains ("{")) {
			int numBrackets = 0;
			for (int i = 0; i < str.length (); i ++) {
				if (str.charAt (i) == '{') {
					numBrackets ++;
				}
			}
		} else if (str.contains (" ")) {
			String[] exprlist = str.split (" ");
			for (int i = 0; i < exprlist.length; i ++) {
				
			}
		}*/
	}
	
	private String getArgument (ReadState s) {
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
	}
	
	private class ReadState {
		public String str;
		public int pos;
	}
}