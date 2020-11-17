package toolbar;

import java.awt.Rectangle;

import main.DisplayBox;
import main.MainPanel;
import main.SelectionMenu;
import main.VariantSelectMenu;
import resources.Sprite;

public class VariantButton extends ToolbarItem{
	DisplayBox box;
	protected VariantButton(Toolbar parent) {
		super(parent);
		this.setSelectable(false);
		this.useClickOnElement(true);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Variant",this);
		setIcon (new Sprite ("resources/images/Variants.png").getImageArray () [0]);
	}
	@Override 
	public void use (int x, int y) {
		if (MainPanel.getVariantMenu().isHidden()) {
		MainPanel.getVariantMenu().show();
		MainPanel.getVariantCloseButton().show();
		} else {
			MainPanel.getVariantMenu().setBoundingRectangle(new Rectangle (160, 0, 96, 160));
			VariantSelectMenu.getVariantSelectRegion().setBoundingRectangle(new Rectangle (160, SelectionMenu.BAR_SIZE, 96, 160 - SelectionMenu.BAR_SIZE));
			MainPanel.getAttributeSelectRegion().hide();
			MainPanel.getVariantMenu().reset();
			VariantSelectMenu.getVariantSelectRegion().reset();
			MainPanel.getVariantCloseButton().setBoundingRectangle(new Rectangle (240,0,16,16));
		}
	}
	@Override
	public void frameEvent () {
		if (this.mouseInside()) {
			box.show();
		} else {
			box.hide();
		}
}
}
