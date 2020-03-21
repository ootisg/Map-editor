package toolbar;

import main.MainPanel;
import main.SelectionRegion.TileRegion;
import map.MapInterface;
import resources.Sprite;

public class ResizeButton extends ToolbarItem {
	
	public ResizeButton (Toolbar parent) {
		super (parent);
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
}
