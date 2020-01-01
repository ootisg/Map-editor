package toolbar;

import resources.Sprite;

public class LayerModeButton extends ToolbarItem {

	public LayerModeButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/layer_mode_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		getMainPanel ().getMap ().toggleLayerMode ();
		getMainPanel ().getMap ().renderElements ();
	}
}
