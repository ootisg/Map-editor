package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import map.Map;
import map.MapInterface;
import map.ResizeEdit;
import resources.Sprite;

public class ResizeWindow extends GuiComponent {

	public static final int BAR_PADDING = 0;
	public static final int BAR_HEIGHT = 16;
	public static final int BAR_SIDE_PADDING = 28;
	
	public static final int WINDOW_PADDING_HORIZONTAL = 4;
	public static final int WINDOW_PADDING_VERTICAL = 20;
	public static final int COMPONENT_PADDING_HORIZONTAL = 52;
	public static final int COMPONENT_PADDING_VERTICAL = 4;
	
	public static final int RESIZE_BUTTON_PADDING_HORIZONTAL = 32;
	public static final int RESIZE_BUTTON_PADDING_VERTICAL = 6;
	
	public static final int WINDOW_WIDTH = 128;
	public static final int WINDOW_HEIGHT = 96;
	
	public static final int ENTRY_BOX_WIDTH = 32;
	public static final int ENTRY_BOX_HEIGHT = 17;
	
	public static final int BACKGROUND_COLOR = 0x808080;
	public static final int BAR_COLOR = 0x26B5B0;
	public static final int OUTLINE_COLOR = 0x000000;
	public static final int TEXT_COLOR = 0x000000;
	
	public static final BufferedImage BUTTON_UP_ICON = Sprite.getImage ("resources/images/resize.png");
	public static final BufferedImage EXIT_BUTTON_ICON = Sprite.getImage ("resources/images/close.png");
	
	private EntryField widthField;
	private EntryField heightField;
	private IconButton resizeButton;
	private IconButton exitButton;
	
	public ResizeWindow (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		
		//Create important components
		int wx = WINDOW_PADDING_HORIZONTAL + COMPONENT_PADDING_HORIZONTAL;
		int wy = WINDOW_PADDING_VERTICAL;
		widthField = new EntryField (new Rectangle (bounds.x + wx, bounds.y + wy, ENTRY_BOX_WIDTH, ENTRY_BOX_HEIGHT), this);
		wy += ENTRY_BOX_HEIGHT + COMPONENT_PADDING_VERTICAL;
		heightField = new EntryField (new Rectangle (bounds.x + wx, bounds.y + wy, ENTRY_BOX_WIDTH, ENTRY_BOX_HEIGHT), this);
		wy += ENTRY_BOX_HEIGHT + RESIZE_BUTTON_PADDING_VERTICAL;
		
		//Set max lengths
		widthField.setMaxLength (4);
		heightField.setMaxLength (4);
		//Set filters
		widthField.setFilter ("[0-9]");
		heightField.setFilter ("[0-9]");
		
		//Create resize button
		resizeButton = new IconButton (new Rectangle (bounds.x + RESIZE_BUTTON_PADDING_HORIZONTAL, bounds.y + wy, BUTTON_UP_ICON.getWidth () + 4, BUTTON_UP_ICON.getHeight () + 6), BUTTON_UP_ICON, this);
		resizeButton.setMargins (2, 3);
		
		//Create exit button
		exitButton = new IconButton (new Rectangle (bounds.x + WINDOW_WIDTH - EXIT_BUTTON_ICON.getWidth (), bounds.y, EXIT_BUTTON_ICON.getWidth (), EXIT_BUTTON_ICON.getHeight ()), EXIT_BUTTON_ICON, this);
	
		//Hide by default
		hide ();
	}
	
	@Override
	public void draw () {
		Rectangle bounds = getBoundingRectangle ();
		Graphics g = getGraphics ();
		g.setColor (new Color (BACKGROUND_COLOR));
		g.fillRect (0, 0, bounds.width, bounds.height);
		g.setColor (new Color (BAR_COLOR));
		g.fillRect (0, 0, bounds.width, BAR_PADDING + BAR_HEIGHT);
		g.setColor (new Color (OUTLINE_COLOR));
		g.drawRect (0, 0, bounds.width - 1, bounds.height - 1);
		g.drawLine (0, BAR_PADDING + BAR_HEIGHT, bounds.width, BAR_PADDING + BAR_HEIGHT);
		g.setColor (new Color (TEXT_COLOR));
		
		//Draw text
		FontMetrics font = g.getFontMetrics ();
		g.drawString ("RESIZE MAP", BAR_SIDE_PADDING, BAR_PADDING + font.getAscent ());
		g.drawString ("WIDTH: ", WINDOW_PADDING_HORIZONTAL, (widthField.getBoundingRectangle ().y - bounds.y) + font.getAscent () + ENTRY_BOX_HEIGHT - font.getHeight ());
		g.drawString ("HEIGHT: ", WINDOW_PADDING_HORIZONTAL, (heightField.getBoundingRectangle ().y - bounds.y) + font.getAscent () + ENTRY_BOX_HEIGHT - font.getHeight ());
	}
	
	@Override
	public void frameEvent () {
		//Check exit button
		if (exitButton.pressed ()) {
			exitButton.reset ();
			hide ();
		}
		
		//Check resize button
		if (resizeButton.pressed ()) {
			//Get map and map interface
			MapInterface mi = getMainPanel ().getMapInterface ();
			Map m = mi.getMap ();
			//Check for invalid stuffs
			if (widthField.getContent ().equals ("") || heightField.getContent ().equals ("") || Integer.parseInt (widthField.getContent ()) == 0 || Integer.parseInt (heightField.getContent ()) == 0) {
				resizeButton.reset ();
				return;
			}
			//Resize the map
			ResizeEdit rt = new ResizeEdit (Integer.parseInt (widthField.getContent ()), Integer.parseInt (heightField.getContent ()), m, mi);
			mi.edit (rt);
			//Reset the button
			resizeButton.reset ();
			hide ();
		}
	}
	
	@Override
	public void show () {
		widthField.reset ();
		heightField.reset ();
		super.show ();
	}

}
