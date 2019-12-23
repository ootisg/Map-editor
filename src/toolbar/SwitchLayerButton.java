package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import resources.Sprite;

public class SwitchLayerButton extends ToolbarItem {
	
	public SwitchLayerButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/Switch Layer.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		// TODO Auto-generated method stub
		System.out.println ("HIA");
	}
}
