package toolbar;

import resources.Sprite;

public class LayerFrontButton extends ToolbarItem {
	
	public LayerFrontButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/layer_front_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		//Go to front layer
	}
}
