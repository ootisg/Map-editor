package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.SelectionRegion.TileRegion;
import toolbar.PasteButton;
import toolbar.PlaceButton;
import toolbar.SelectButton;

public class TileSelection extends GuiComponent {

	String request;
	
	
	
	public TileSelection(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
	}

	public void setRequest(String request) {
		this.request = request;
	}
	
	public void drawTileRegion (TileRegion region) {
		//Null check
		if (region == null) {
			return;
		}
		Graphics2D g = (Graphics2D)getGraphics ();
			Tile[][] renderedTiles = usedTiles;
			for (int i = 0; i < renderedTiles.length; i ++) {
				for (int j = 0; j < renderedTiles [0].length; j ++) {
					if (j + region.getStartX () < map.getActiveLayer ().getWidth () && i + region.getStartY () < map.getActiveLayer ().getHeight () && region.getTiles () != null) {
						if (renderedTiles[i][j] != null) {
							renderedTiles [i][j].render (region.getTiles () [i][j], g);
						}
					}
				}
			}
		}
	
}
