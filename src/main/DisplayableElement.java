package main;

public abstract class DisplayableElement {
	
	private GuiComponent parent;
	
	public DisplayableElement (GuiComponent parent) {
		this.parent = parent;
	}
	
	public GuiComponent getParent () {
		return parent;
	}
	
	public abstract void render (int x, int y);
}
