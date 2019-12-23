package main;

import java.awt.Color;
import java.awt.Graphics;

public class QueryWindow {
	String queryType;
	String[] queryText;
	String[] headerText;
	VariantTemplate variantTemplate;
	int x;
	int y;
	int width;
	int height;
	int selectedIndex;
	boolean displayed;
	public QueryWindow (VariantTemplate template) {
		this.queryType = "variant";
		this.variantTemplate = template;
		this.x = 0;
		this.y = 240;
		this.width = 160;
		this.height = 240;
		this.selectedIndex = 0;
		this.displayed = true;
		queryText = new String[this.variantTemplate.queryList.size ()];
		headerText = new String[this.variantTemplate.queryList.size ()];
		for (int i = 0; i < headerText.length; i ++) {
			queryText [i] = "";
			headerText [i] = this.variantTemplate.queryList.get (i);
		}
	}
	public void frameEvent () {
		boolean[] keysPressedOnFrame = MainLoop.getWindow ().keysPressedOnFrame;
		if (this.queryType.equals ("variant")) {
			for (int i = 0x20; i < 0x7F; i ++) {
				if (keysPressedOnFrame [i]) {
					//Non-functional; figure out why
					//queryText [selectedIndex] += (char)i;
				}
			}
		}
		//System.out.println(queryText[selectedIndex]);
	}
	public void append (char character) {
		if (this.queryType.equals ("variant")) {
			queryText [selectedIndex] += character;
		}
	}
	public void removeChar () {
		if (this.queryType.equals ("variant")) {
			if (queryText [selectedIndex].length () > 0) {
				queryText [selectedIndex] = queryText [selectedIndex].substring (0, queryText [selectedIndex].length () - 1);
			}
		}
	}
	public void selectUp () {
		if (this.queryType.equals ("variant")) {
			if (selectedIndex > 0) {
				selectedIndex --;
			}
		}
	}
	public void selectDown () {
		if (this.queryType.equals ("variant")) {
			if (selectedIndex < queryText.length - 1) {
				selectedIndex ++;
			}
		}
	}
	public void close () {
		if (this.queryType.equals ("variant")) {
			displayed = false;
		}
	}
	public void render (Graphics g) {
		if (displayed) {
			g.setColor (new Color (0x000000));
			g.fillRect (x, y, width, height);
			if (this.queryType.equals ("variant")) {
				for (int i = 0; i < queryText.length; i ++) {
					if (i == selectedIndex) {
						g.setColor (new Color (0x00FF00));
					} else {
						g.setColor (new Color (0xFFFFFF));
					}
					g.drawRect (this.x + 16, this.y + i * 16 + 33, 128, 14);
					g.drawChars (this.queryText [i].toCharArray (), 0, this.queryText [i].length (), this.x + 20, this.y + 16 * i + 46);
				}
			}
			int textWidth = g.getFontMetrics ().stringWidth (this.headerText [selectedIndex]);
			g.setColor (new Color (0x00FF00));
			g.drawChars (this.headerText [selectedIndex].toCharArray (), 0, this.headerText [selectedIndex].length (), (this.width / 2) - (textWidth / 2), this.y + 20);
		}
	}
}