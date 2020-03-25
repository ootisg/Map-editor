package toolbar;

import resources.Sprite;

public class BackgroundButton extends ToolbarItem {

	public BackgroundButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/background_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		getMainPanel ().getBackgroundWindow ().show ();
	}
	
}
