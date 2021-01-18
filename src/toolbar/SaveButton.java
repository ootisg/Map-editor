package toolbar;

import main.MainPanel;
import resources.Sprite;

public class SaveButton extends ToolbarItem {
	public SaveButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Save");
		setIcon (new Sprite ("resources/images/save_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		MainPanel.getMapInterface ().save ();
	}
}
