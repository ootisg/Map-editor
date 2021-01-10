package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.DisplayBox;
import main.MainPanel;
import resources.Sprite;

public class GridLineButton extends ToolbarItem {
	DisplayBox box;
	public GridLineButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Toggle Gridlines");
		setIcon (new Sprite ("resources/images/totally legit icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		MainPanel.getMapInterface().showGrid(!MainPanel.getMapInterface().isGridShown());
	}
}