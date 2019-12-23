package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

import resources.Sprite;

public class TileSelectMenu extends SelectionMenu {

	private TilesetSelectRegion tilesetSelect;
	private TileSelectRegion tileSelect;
	
	private Sprite backButton;
	
	public TileSelectMenu (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent, "TILES");
		tilesetSelect = new TilesetSelectRegion (new Rectangle (bounds.x, bounds.y + SelectionMenu.BAR_SIZE, bounds.width, bounds.height - SelectionMenu.BAR_SIZE), this);
		tileSelect = new TileSelectRegion (new Rectangle (bounds.x, bounds.y + SelectionMenu.BAR_SIZE, bounds.width, bounds.height - SelectionMenu.BAR_SIZE), this);
		tileSelect.hide ();
		backButton = new Sprite ("resources/images/arrow_left_icon.png");
	}
	
	public TilesetSelectRegion getTilesetSelect () {
		return tilesetSelect;
	}
	
	public TileSelectRegion getTileSelect () {
		return tileSelect;
	}
	
	@Override
	public void render () {
		super.render ();
		Rectangle bounds = getBoundingRectangle ();
		if (tilesetSelect.isHidden ()) {
			backButton.draw (bounds.x + SelectionMenu.BACKBUTTON_PADDING_LEFT, bounds.y + SelectionMenu.BAR_SIZE - backButton.getImageArray ()[0].getHeight () - SelectionMenu.BACKBUTTON_PADDING_BOTTOM);
		}
	}
	
	@Override
	public void mousePressed (int x, int y, int button) {
		if (tilesetSelect.isHidden ()) {
			int buttonWidth = backButton.getImageArray ()[0].getWidth ();
			int buttonHeight = backButton.getImageArray ()[0].getHeight ();
			Rectangle backButton = new Rectangle (SelectionMenu.BACKBUTTON_PADDING_LEFT, SelectionMenu.BAR_SIZE - buttonHeight - SelectionMenu.BACKBUTTON_PADDING_BOTTOM, buttonWidth, buttonHeight);
			if (backButton.contains (x, y)) {
				tileSelect.hide ();
				tilesetSelect.show ();
			}
		}
	}
}
