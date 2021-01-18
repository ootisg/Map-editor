package interpreter;

import java.util.ArrayList;

public abstract class Compiler {
	public String compile (String src) {
		String[] defaultTypenamesArray = new String[] {"char", "short", "int", "long", "float", "double"};
		String[] defaultTypeModifiersArray = new String[] {"short", "int", "long", "unsigned"};
		String[] defaultKeywordsArray = new String[] {"auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while"};
		String[] operatorsArray = new String[] {"+", "-", "*", "/", "%", "++", "--", "==", "!=", ">", "<", ">=", "<=", "&&", "||", "!", "&", "|", "^", "~", "<<", ">>", "=", "+=", "-=", "*=", "/=", "%=", "<<=", ">>=", "&=", "^=", "|=", "?", ":", "->"};
		ArrayList<String> defaultTypenames = toArrayList (defaultTypenamesArray);
		ArrayList<String> defaultTypeModifiers = toArrayList (defaultTypeModifiersArray);
		ArrayList<String> defaultKeywords = toArrayList (defaultKeywordsArray);
		ArrayList<String> operators = toArrayList (operatorsArray);
		int startIndex = 0;
		int scanLength = 0;
		boolean whitespaceScan = false;
		String workingScan = "";
		ArrayList<Token> currentExpression;
		ArrayList<ArrayList<Token>> expressions;
		return null;
	}
	private ArrayList<String> toArrayList (String[] elements) {
		ArrayList<String> resultList = new ArrayList<String> ();
		for (int i = 0; i < elements.length; i ++) {
			resultList.add (elements [i]);
		}
		return resultList;
	}
	private boolean contains (ArrayList<String> list, String element) {
		for (int i = 0; i < list.size (); i ++) {
			if (list.get (i).equals (element)) {
				return true;
			}
		}
		return false;
	}
}