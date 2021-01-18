package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.DisplayBox;
import resources.Sprite;

public class NewLayerButton extends ToolbarItem {
	public NewLayerButton (Toolbar parent) {
		super (parent);
		this.setBoxText("New Layer");
		setIcon (new Sprite ("resources/images/New Layer.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		getMainPanel ().getMap ().addLayer ();
	}
}
