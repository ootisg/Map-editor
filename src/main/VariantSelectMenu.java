package main;

import java.awt.Rectangle;

public class VariantSelectMenu extends SelectionMenu {
	public static VariantSelectRegion variantSelect;
	int mouseStartPosX = 0; 
	int mouseStartPosY = 0;
	int menuX = 160;
	int menuY = 0;
	boolean didRun = true;
	boolean hadRun = false;
	boolean use = false;
	boolean set = false;
	public VariantSelectMenu (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent, "VARIANTS");
		variantSelect = new VariantSelectRegion (new Rectangle (bounds.x, bounds.y + SelectionMenu.BAR_SIZE, bounds.width, bounds.height - SelectionMenu.BAR_SIZE), this);
	}
	public static VariantSelectRegion getVariantSelectRegion () {
		return variantSelect;
	}
	public void reset () {
		mouseStartPosX = 0; 
		mouseStartPosY = 0;
		menuX = 160;
		menuY = 0;
		didRun = true;
		hadRun = false;
	}
	@Override 
	public void frameEvent (){
		if (mouseDown()) {
			if (!set) {
				set = true;
				if (!this.isHidden()) {
					use = mouseInside();
				} else {
					use = false;
				}
			}
			if (use) {
				if (didRun) {
					hadRun = true;
					didRun = false;
				} else {
					if (hadRun) {
						int oldX = menuX;
						int oldY = menuY;
						menuX = menuX + (this.getWindow().getMouseX() - mouseStartPosX);
						menuY = menuY + (this.getWindow().getMouseY() - mouseStartPosY);
						this.setBoundingRectangle(new Rectangle ( menuX ,  menuY, this.getBoundingRectangle().width,this.getBoundingRectangle().height));
						variantSelect.setBoundingRectangle(new Rectangle ( menuX ,  menuY , variantSelect.getBoundingRectangle().width,variantSelect.getBoundingRectangle().height));
						AttributeSelectRegion region = this.getMainPanel().getAttributeSelectRegion();
						region.setBoundingRectangle(new Rectangle ( menuX,  menuY , region.getBoundingRectangle().width,region.getBoundingRectangle().height));
						this.getMainPanel().getVariantCloseButton().setBoundingRectangle(new Rectangle (this.getMainPanel().getVariantCloseButton().getBoundingRectangle().x + menuX - oldX, this.getMainPanel().getVariantCloseButton().getBoundingRectangle().y + menuY - oldY,16,16));
						mouseStartPosX = this.getWindow().getMouseX();
						mouseStartPosY = this.getWindow().getMouseY();
						} else {
					hadRun = false;
						}
				}
			}
		} else {
			didRun = false;
			hadRun = false;
			set = false;
			use = false;
			mouseStartPosX = this.getWindow().getMouseX();
			mouseStartPosY = this.getWindow().getMouseY();
		}
		variantSelect.frameEvent();
	}
	@Override
	public void mouseDragged (int x, int y, int button) {
		if (use) {
			didRun = true;
			if (mouseStartPosX != 0 && mouseStartPosY != 0) {
			if (button == 1) {
				int oldX = menuX;
				int oldY = menuY;
				menuX = menuX + (this.getWindow().getMouseX() - mouseStartPosX);
				menuY = menuY + (this.getWindow().getMouseY() - mouseStartPosY);
				this.setBoundingRectangle(new Rectangle (this.getBoundingRectangle().x + menuX - oldX, this.getBoundingRectangle().y + menuY - oldY, this.getBoundingRectangle().width,this.getBoundingRectangle().height));
				variantSelect.setBoundingRectangle(new Rectangle (variantSelect.getBoundingRectangle().x + menuX - oldX, variantSelect.getBoundingRectangle().y + menuY - oldY, variantSelect.getBoundingRectangle().width,variantSelect.getBoundingRectangle().height));
				AttributeSelectRegion region = this.getMainPanel().getAttributeSelectRegion();
				region.setBoundingRectangle(new Rectangle (region.getBoundingRectangle().x + menuX - oldX, region.getBoundingRectangle().y + menuY - oldY, region.getBoundingRectangle().width,region.getBoundingRectangle().height));
				this.getMainPanel().getVariantCloseButton().setBoundingRectangle(new Rectangle (this.getMainPanel().getVariantCloseButton().getBoundingRectangle().x + menuX - oldX, this.getMainPanel().getVariantCloseButton().getBoundingRectangle().y + menuY - oldY,16,16));
		
			}
			}
			mouseStartPosX = this.getWindow().getMouseX();
			mouseStartPosY = this.getWindow().getMouseY();
		}
	}
	public boolean isBeingDragged () {
		return use;
	}
	public int getMenuX() {
		return menuX;
	}
	public int getMenuY() {
		return menuY;
	}
}
