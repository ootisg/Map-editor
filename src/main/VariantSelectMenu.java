package main;

import java.awt.Rectangle;

public class VariantSelectMenu extends SelectionMenu {
	public static VariantSelectRegion variantSelect;
	int mouseStartPosX = 0; 
	int mouseStartPosY = 0;
	int menuX = 160;
	int menuY = 0;
	public VariantSelectMenu (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent, "VARIANTS");
		variantSelect = new VariantSelectRegion (new Rectangle (bounds.x, bounds.y + SelectionMenu.BAR_SIZE, bounds.width, bounds.height - SelectionMenu.BAR_SIZE), this);
	}
	public static VariantSelectRegion getVariantSelectRegion () {
		return variantSelect;
	}
	@Override
	public void mouseDragged (int x, int y, int button) {
		if (button == 1) {
			int oldX = menuX;
			int oldY = menuY;
			menuX = menuX + (this.getWindow().getMouseX() - mouseStartPosX);
			menuY = menuY + (this.getWindow().getMouseY() - mouseStartPosY);
			this.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x + menuX - oldX, this.getBoundingRectangle().y + menuY - oldY, this.getBoundingRectangle().width,this.getBoundingRectangle().height));
			variantSelect.setBoundingRectangle(new Rectangle (variantSelect.getBoundingRectangle().x + menuX - oldX, variantSelect.getBoundingRectangle().y + menuY - oldY, variantSelect.getBoundingRectangle().width,variantSelect.getBoundingRectangle().height));
			AttributeSelectRegion region = this.getMainPanel().getAttributeSelectRegion();
			region.setBoundingRectangle(new Rectangle (region.getBoundingRectangle().x + menuX - oldX, region.getBoundingRectangle().y + menuY - oldY, region.getBoundingRectangle().width,region.getBoundingRectangle().height));
			if (!this.mouseInside()) {
			this.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x - menuX + oldX, this.getBoundingRectangle().y - menuY + oldY, this.getBoundingRectangle().width,this.getBoundingRectangle().height));
			region.setBoundingRectangle(new Rectangle (region.getBoundingRectangle().x - menuX + oldX, region.getBoundingRectangle().y - menuY + oldY, region.getBoundingRectangle().width,region.getBoundingRectangle().height));
			variantSelect.setBoundingRectangle(new Rectangle (variantSelect.getBoundingRectangle().x - menuX + oldX, variantSelect.getBoundingRectangle().y - menuY + oldY, variantSelect.getBoundingRectangle().width,variantSelect.getBoundingRectangle().height));
			menuX = oldX;
			menuY = oldY;
		}
		}
		
	}
	@Override 
	public void frameEvent () {
		if (this.mouseInside() && this.mouseDown()) {
			mouseStartPosX = this.getWindow().getMouseX();
			mouseStartPosY = this.getWindow().getMouseY();
		}
	}
	public int getMenuX() {
		return menuX;
	}
	public int getMenuY() {
		return menuY;
	}
}
