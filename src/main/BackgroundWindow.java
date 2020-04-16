package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFileChooser;

import map.BackgroundEdit;
import map.Map;
import map.MapInterface;
import map.ResizeEdit;
import resources.Sprite;

public class BackgroundWindow extends GuiComponent implements EntryFieldValidator {

	public static final int BAR_PADDING = 0;
	public static final int BAR_HEIGHT = 16;
	public static final int BAR_SIDE_PADDING = 12;
	
	public static final int WINDOW_PADDING_HORIZONTAL = 4;
	public static final int WINDOW_PADDING_VERTICAL = 20;
	public static final int COMPONENT_PADDING_HORIZONTAL = 12;
	public static final int COMPONENT_PADDING_VERTICAL = 4;
	
	public static final int APPLY_BUTTON_PADDING_HORIZONTAL = 8;
	public static final int APPLY_BUTTON_PADDING_VERTICAL = 10;
	
	public static final int WINDOW_WIDTH = 128;
	public static final int WINDOW_HEIGHT = 156;
	
	public static final int ENTRY_BOX_WIDTH = 60;
	public static final int ENTRY_BOX_HEIGHT = 17;
	
	public static final int TEXT_HEIGHT = 14;
	
	public static final int IMAGE_TEXT_PADDING = 18;
	
	public static final int FIELD_PADDING = 24;
	
	public static final int BACKGROUND_COLOR = 0x808080;
	public static final int BAR_COLOR = 0x26B5B0;
	public static final int OUTLINE_COLOR = 0x000000;
	public static final int TEXT_COLOR = 0x000000;
	
	public static final BufferedImage APPLY_BUTTON_ICON = Sprite.getImage ("resources/images/apply.png");
	public static final BufferedImage EXIT_BUTTON_ICON = Sprite.getImage ("resources/images/close.png");
	public static final BufferedImage IMAGE_BUTTON_ICON = Sprite.getImage ("resources/images/add_icon.png");
	
	public static final String IMAGE_PATH_DEFAULT = "(none)";
	
	private EntryField xField;
	private EntryField yField;
	private IconButton imgButton;
	private IconButton applyButton;
	private IconButton exitButton;
	
	private String imgPath = "(none)";
	
	private int imgTextY;
	private int imgButtonY;
	private int scrollFieldY;
	
	public BackgroundWindow (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		
		//Create important components and set text positioning
		int wx = WINDOW_PADDING_HORIZONTAL + COMPONENT_PADDING_HORIZONTAL;
		int wy = WINDOW_PADDING_VERTICAL;
		imgTextY = wy;
		wy += TEXT_HEIGHT + COMPONENT_PADDING_VERTICAL;
		imgButtonY = wy;
		wy += FIELD_PADDING;
		scrollFieldY = wy;
		wy += TEXT_HEIGHT + COMPONENT_PADDING_VERTICAL;
		//Make input fields
		xField = new EntryField (new Rectangle (bounds.x + WINDOW_PADDING_HORIZONTAL + COMPONENT_PADDING_HORIZONTAL, bounds.y + wy, ENTRY_BOX_WIDTH, ENTRY_BOX_HEIGHT), this);
		wy += ENTRY_BOX_HEIGHT + COMPONENT_PADDING_VERTICAL;
		yField = new EntryField (new Rectangle (bounds.x + WINDOW_PADDING_HORIZONTAL + COMPONENT_PADDING_HORIZONTAL, bounds.y + wy, ENTRY_BOX_WIDTH, ENTRY_BOX_HEIGHT), this);
		wy += ENTRY_BOX_HEIGHT + APPLY_BUTTON_PADDING_VERTICAL;
		//Set max lengths
		xField.setMaxLength (8);
		yField.setMaxLength (8);
		//Set filters
		xField.setFilter ("[0-9|.|-]");
		yField.setFilter ("[0-9|.|-]");
		
		//Create image button
		imgButton = new IconButton (new Rectangle (bounds.x + WINDOW_PADDING_HORIZONTAL, bounds.y + imgButtonY, IMAGE_BUTTON_ICON.getWidth (), IMAGE_BUTTON_ICON.getHeight ()), IMAGE_BUTTON_ICON, this);
		
		//Create resize button
		applyButton = new IconButton (new Rectangle (bounds.x + APPLY_BUTTON_PADDING_HORIZONTAL, bounds.y + wy, APPLY_BUTTON_ICON.getWidth () + 4, APPLY_BUTTON_ICON.getHeight () + 6), APPLY_BUTTON_ICON, this);
		applyButton.setMargins (2, 3);
		
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
		g.drawString ("BACKGROUND", BAR_SIDE_PADDING, BAR_PADDING + font.getAscent ());
		g.drawString ("IMAGE: ", WINDOW_PADDING_HORIZONTAL, imgTextY + font.getAscent ());
		g.drawString (imgPath, WINDOW_PADDING_HORIZONTAL + IMAGE_TEXT_PADDING, imgButtonY + font.getAscent ());
		g.drawString ("SCROLL VALUES:", WINDOW_PADDING_HORIZONTAL, scrollFieldY + font.getAscent ());
		g.drawString ("X ", WINDOW_PADDING_HORIZONTAL, (xField.getBoundingRectangle ().y - bounds.y) + font.getAscent () + ENTRY_BOX_HEIGHT - font.getHeight ());
		g.drawString ("Y ", WINDOW_PADDING_HORIZONTAL, (yField.getBoundingRectangle ().y - bounds.y) + font.getAscent () + ENTRY_BOX_HEIGHT - font.getHeight ());
	}
	
	@Override
	public void frameEvent () {
		//Check exit button
		if (exitButton.pressed ()) {
			exitButton.reset ();
			hide ();
		}
		
		//Check image button
		if (imgButton.pressed ()) {
			imgButton.reset ();
			JFileChooser chooser = new JFileChooser (MapInterface.BACKGROUND_LOCATION);
			if (chooser.showSaveDialog (getWindow ()) != JFileChooser.CANCEL_OPTION) {
				imgPath = chooser.getSelectedFile ().getName ();
			}
		}
		
		//Check apply button
		if (applyButton.pressed ()) {
			//Get map and map interface
			MapInterface mi = getMainPanel ().getMapInterface ();
			Map m = mi.getMap ();
			//Check for invalid stuffs
			if (!xField.isValid () || !yField.isValid () || imgPath.equals (IMAGE_PATH_DEFAULT)) {
				applyButton.reset ();
				return;
			}
			//Apply the background
			BackgroundEdit bgEdit = new BackgroundEdit (MapInterface.BACKGROUND_LOCATION + imgPath, Double.parseDouble (xField.getContent ()), Double.parseDouble (yField.getContent ()), m);
			mi.edit (bgEdit);
			//Reset the button
			applyButton.reset ();
		}
	}
	
	@Override
	public void show () {
		
		//NOTE: Must be called if layers are ever switched
		Map m = getMainPanel ().getMap ();
		Map.TileLayer activeLayer = m.getActiveLayer ();
		//Background cannot be added for a layer with tiles
		if (activeLayer.hasTiles ()) {
			return;
		}
		xField.resetToValue ("1");
		yField.resetToValue ("1");
		if (!activeLayer.isBackgroundLayer ()) {
			imgPath = IMAGE_PATH_DEFAULT;
		} else {
			xField.setContent (String.valueOf (activeLayer.getBackgroundScrollX ()));
			yField.setContent (String.valueOf (activeLayer.getBackgroundScrollX ()));
			imgPath = activeLayer.getBackgroundFilename ();
		}
		super.show ();
	}

	@Override
	public boolean isValid (EntryField field) {
		// TODO FIX THIS PLEASE THIS IS SO HACKY
		try {
			Double.parseDouble (field.getContent ());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
