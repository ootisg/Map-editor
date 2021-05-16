package main;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import main.SelectionRegion.TileRegion;
import resources.Sprite;
public class TileSelection extends GuiComponent {

	TileRegion selected;
	
	IconButton select;
	
	String request;

	public TileSelection(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		 select = new IconButton (new Rectangle (this.getBoundingRectangle().x + bounds.width/2 - 8, this.getBoundingRectangle().y + bounds.height/2 - 8, 16, 16), Sprite.getImage ("resources/images/Select.png"), this);
	}
	
	public void setRequest(String request) {
		this.request = request;
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent();
		
		OptionsMenu menu = (OptionsMenu) this.getParent();
		
		ArrayList <GuiComponent> otherMenus = menu.content;
		
		for (int i = 0; i < otherMenus.size(); i++) {
			if (otherMenus.get(i) instanceof TileSelection) {
				TileSelection otherSelection = (TileSelection) otherMenus.get(i);
				if (otherSelection.select.isHidden() && !this.equals(otherSelection) && select.isHidden()) {
					select.show();
				}
			}
		}
		
		if (select.pressed()) {
			select.reset();
			select.hide();
		}
		
	}
	
	@Override
	public void render () {
		super.render();
		
		Graphics g = getWindow ().getBuffer ();
		g.setColor (new Color (000));
		
		if (request != null) {
			g.drawString(request, this.getBoundingRectangle().x + this.getBoundingRectangle().width/2 - request.length() * 3, this.getBoundingRectangle().y + 10);
		}
		
		if (select.isHidden()) {
			if (selected == null) {		
				g.drawString ("selecting", this.getBoundingRectangle().x + this.getBoundingRectangle().width/2 - 27, this.getBoundingRectangle().y + this.getBoundingRectangle().height/2 - 8);
			} else {
				try {
					drawTileRegion (selected,this.getBoundingRectangle().width/2 - (selected.getTiles()[0].length * 8),16);
				} catch (NullPointerException e) {
					
				}
			}
			selected = MainPanel.getMapInterface().getSelectedRegion();
		}
	}
	
	public void drawTileRegion (TileRegion region, int startX, int startY) {
		//Null check
		if (region == null) {
			return;
		}
		Graphics2D g = (Graphics2D)getGraphics ();
		try {
			Tile[][] renderedTiles = new Tile[region.getTiles().length][region.getTiles()[0].length];
			
			for (int i = 0; i < renderedTiles.length; i++) {
				for (int j = 0; j < renderedTiles[i].length; j++) {
					renderedTiles[i][j] = MainPanel.getMap().getTile(MainPanel.getMap().getTopDisplayLayer(), region.getTiles()[i][j].x/16, region.getTiles()[i][j].y/16);
				}
			}
			for (int i = 0; i < renderedTiles.length; i ++) {
				for (int j = 0; j < renderedTiles [0].length; j ++) {
					if (renderedTiles[i][j] != null) {
						if (startX + (j*16) < this.getBoundingRectangle().width - 20 && startY + (i *16) < this.getBoundingRectangle().height - 35) {
							renderedTiles [i][j].render (new Rectangle (startX + (j * 16), startY + (i * 16), 16,16), g);
						}
					}
				}
			}
		} catch (NullPointerException e) {
			
		}
	}
	public HashMap<String, Tile[][]> getSelectedArea (){
		Tile[][] renderedTiles = new Tile[selected.getTiles().length][selected.getTiles()[0].length];
		
		for (int i = 0; i < renderedTiles.length; i++) {
			for (int j = 0; j < renderedTiles[i].length; j++) {
				renderedTiles[i][j] = MainPanel.getMap().getTile(MainPanel.getMap().getTopDisplayLayer(), selected.getTiles()[i][j].x/16, selected.getTiles()[i][j].y/16);
			}
		}
		HashMap <String, Tile[][]> map = new HashMap<String, Tile[][]>();
		map.put(request, renderedTiles);
		return map;
	}
}
