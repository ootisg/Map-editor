package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.DisplayBox;
import main.MainPanel;
import resources.Sprite;

public class RedoButton extends ToolbarItem {
	DisplayBox box;
	public RedoButton (Toolbar parent) {
		super (parent);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Redo",this);
		setIcon (new Sprite ("resources/images/Redo.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		((MainPanel)(getParent ().getParent ())).getMapInterface ().redo ();
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
