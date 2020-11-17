package toolbar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.DisplayableImageElement;
import main.GuiComponent;
import main.MainPanel;

public abstract class ToolbarItem extends GuiComponent {
	
	private DisplayableImageElement icon;
	
	private boolean selectable;
	private boolean selected;
	private boolean dragable;
	private boolean useOnClick;
	
	protected ToolbarItem (Toolbar parent) {
		super (new Rectangle (Toolbar.BUTTON_OFFSET_X, Toolbar.BUTTON_OFFSET_Y, Toolbar.BUTTON_WIDTH, Toolbar.BUTTON_HEIGHT), parent);
		parent.configure (this);
		selectable = true;
	}
	
	protected void setIcon (BufferedImage icon) {
		this.icon = new DisplayableImageElement (icon, getParent ());
	}
	
	protected void setSelectable (boolean selectable) {
		this.selectable = selectable;
	}
	
	protected void setDragable (boolean dragable) {
		this.dragable = dragable;
	}
	
	public void setSelected (boolean selected) {
		this.selected = selected;
	}
	
	public void useClickOnElement (boolean e) {
		useOnClick = e;
	}
	
	public BufferedImage getIcon () {
		return icon.getIcon ();
	}
	
	public boolean selectable () {
		return selectable;
	}
	
	public boolean dragable () {
		return dragable;
	}
	
	public boolean usesClickOnElement () {
		return useOnClick;
	}
	
	public boolean selected () {
		return selected;
	}
	
	
	@Override
	public void draw () {
		icon.render (getBoundingRectangle ());
		if (selected) {
			Rectangle bounds = getBoundingRectangle ();
			Graphics g = getParent ().getGraphics ();
			g.setColor (new Color (0x000000));
			g.drawRect (bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}
	
	@Override
	public void mouseReleased (int x, int y, int button) {
		//if we add more dragabble menus we will have to work with this a bit more
		if (!MainPanel.getVariantMenu().isBeingDragged()) {
			if (selectable ()) {
				((Toolbar)getParent ()).selectItem (this);
				onSelect ();
			} else {
				use (x, y);
			}
		}
	}
	
	public void onSelect () {
		
	}
	
	public void use () {
		use (-1, -1);
	}
	
	public void use (int x, int y) {
		
	}
	public void useDrag (int x, int y) {
		
	}
	public void useIntermideite (int x, int y) {
		if (!MainPanel.getVariantMenu().isBeingDragged()) {
			use(x,y);
		}
	}
	public void useDragIntermideite (int x, int y) {
		if (!MainPanel.getVariantMenu().isBeingDragged()) {
			useDrag(x,y);
		}
	}
	
	public void doClickOnElement (int x, int y) {
		
	}
}
