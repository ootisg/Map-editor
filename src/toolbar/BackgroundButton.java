package toolbar;

import java.awt.Rectangle;

import main.DisplayBox;
import resources.Sprite;

public class BackgroundButton extends ToolbarItem {
DisplayBox box;
	public BackgroundButton (Toolbar parent) {
		super (parent);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Background",this);
		setIcon (new Sprite ("resources/images/background_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		getMainPanel ().getBackgroundWindow ().show ();
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
