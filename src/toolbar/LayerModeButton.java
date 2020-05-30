package toolbar;

import java.awt.Rectangle;

import main.DisplayBox;
import resources.Sprite;

public class LayerModeButton extends ToolbarItem {
	DisplayBox box;
	public LayerModeButton (Toolbar parent) {
		super (parent);
		box = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"Layer Mode",this);
		setIcon (new Sprite ("resources/images/layer_mode_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		getMainPanel ().getMap ().toggleLayerMode ();
		getMainPanel ().getMap ().renderElements ();
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
