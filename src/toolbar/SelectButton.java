package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.DisplayBox;
import main.MainPanel;
import main.SelectionRegion.TileRegion;
import map.MapInterface;
import resources.Sprite;

public class SelectButton extends ToolbarItem {
	public SelectButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Select Button");
		setIcon (new Sprite ("resources/images/Select.png").getImageArray () [0]);
		setDragable (true);
		useClickOnElement (true);
	}

	@Override
	public void use (int x, int y) {
		MainPanel mainPanel = getMainPanel ();
		MapInterface mapInterface = mainPanel.getMapInterface ();
		if (!(mainPanel.getVariantMenu().getBoundingRectangle().contains((x * 16) + 160, y * 16) && !mainPanel.getVariantMenu().isHidden())) {
		if (mapInterface.getAnchorX () == -1 || mapInterface.getAnchorY () == -1) {
			mapInterface.setAnchor (x, y);
		}
		if (x < mapInterface.getGridWidth () && y < mapInterface.getGridHeight ()) {
			//I'm unsure why this doesn't fix the selection outside of map bug, check on this later
			mapInterface.setSelect (x, y);
			TileRegion newRegion = mapInterface.makeRegion (mapInterface.getAnchorX (), mapInterface.getAnchorY (), mapInterface.getSelectX (), mapInterface.getSelectY ());
			if (!newRegion.equals (mapInterface.getSelectedRegion ())) {
				mapInterface.removeDrawRequest (mapInterface.getSelectedRegion ());
				mapInterface.select (newRegion);
			}
			}
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
