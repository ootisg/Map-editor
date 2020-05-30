package toolbar;

import java.awt.Rectangle;

import main.DisplayBox;
import main.MainPanel;
import main.SelectionRegion.TileRegion;
import map.MapInterface;
import resources.Sprite;

public class ResizeButton extends ToolbarItem {
	DisplayBox box;
	public ResizeButton (Toolbar parent) {
		super (parent);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Resize",this);		
		setIcon (new Sprite ("resources/images/resize_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		MainPanel mainPanel = getMainPanel ();
		if (mainPanel.getResizeWindow ().isHidden ()) {
			mainPanel.getResizeWindow ().show ();
		}
	}
	
	@Override
	public void useDrag (int x, int y) {
		use (x, y);
	}
	
	@Override
	public void onSelect () {
		getMainPanel ().getMapInterface ().deselect ();
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
