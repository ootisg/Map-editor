package toolbar;

import main.DisplayBox;
import main.MainPanel;
import resources.Sprite;

public class LoadButton extends ToolbarItem {
	boolean pressed = false;
	public LoadButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Load");
		setIcon (new Sprite ("resources/images/load_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		pressed = true;
	}
	@Override 
	public void frameEvent () {
		if (pressed) {
			MainPanel.getMapInterface ().load ();
			pressed = false;
		}
		if (box != null) {
			if (this.mouseInside()) {
				box.show();
			} else {
				box.hide();
			}
		}
	}
}
