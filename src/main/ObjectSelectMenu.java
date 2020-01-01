package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ObjectSelectMenu extends SelectionMenu {
	public static ObjectSelectRegion objectSelect;
	public ObjectSelectMenu (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent, "OBJECTS");
		objectSelect = new ObjectSelectRegion (new Rectangle (bounds.x, bounds.y + SelectionMenu.BAR_SIZE, bounds.width, bounds.height - SelectionMenu.BAR_SIZE), this);
	}
	
}
