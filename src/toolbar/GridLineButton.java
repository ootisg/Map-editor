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
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Toggle Gridlines",this);
		setIcon (new Sprite ("resources/images/totally legit icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		MainPanel.getMapInterface().showGrid(!MainPanel.getMapInterface().isGridShown());
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