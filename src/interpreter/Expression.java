package interpreter;

import java.util.ArrayList;

public abstract class Expression extends Token {
	
	private ArrayList<Token> tokens;
	
	public Expression (String value, String type) {
		super (value, type);
		tokens = new ArrayList<Token> ();
	}
	
	public ArrayList<Token> getTokens () {
		return tokens;
	}
	
	public abstract void Tokenize ();
}
