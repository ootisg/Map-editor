package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import filters.DeleateFilter;
import filters.FillFillter;
import filters.ReplaceFilter;
import resources.Sprite;

public class FilterSelectMenu extends SelectionRegion{
	Filter [] filters = new Filter [10];
	protected FilterSelectMenu(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		this.setGridDimensions(1, 10);
		this.setElementWidth(16);
		this.setElementHeight(16);
		//TODO add filters

		for (int i = 0; i < 8; i++) {filters [i] = new Filter (this);}
		for (int i = 0; i < 8; i++) {filters [i].setName ("filter" + i);}
		filters[7] = new ReplaceFilter(this);
		filters[8] = new FillFillter(this);
		filters[9] = new DeleateFilter(this);
		DisplayableImageElement[][] toUse = new DisplayableImageElement[getGridHeight ()][getGridWidth ()];
		for (int i = 0; i < filters.length; i++) {
			toUse [i][0] = new DisplayableImageElement (filters[i].getTexture(),this);
		}
		setElements (toUse);
	}
	@Override
	public void doClickOnElement (int horizontalIndex, int verticalIndex) {
		filters[verticalIndex].runFilterCode();
	}
	@Override
	public void doMouseEnter (int horizontalIndex, int verticalIndex) {
		filters[verticalIndex].showName(this.getBoundingRectangle().x, this.getBoundingRectangle().y + (verticalIndex * 16));
	}
	@Override
	public void doMouseExit (int horizontalIndex, int verticalIndex) {
		filters[verticalIndex].dontShowName();
	}
	@Override
	public void draw () {
//		Graphics g = this.getGraphics ();
//		g.setColor (new Color (0x202020));
//		g.fillRect(0, 0, (int)getBoundingRectangle ().getWidth (), (int)getBoundingRectangle ().getHeight ());
		super.draw ();
	}

}
