package toolbar;

import main.MainPanel;
import resources.Sprite;

public class RedoButton extends ToolbarItem {
	public RedoButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Redo");
		setIcon (new Sprite ("resources/images/Redo.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		MainPanel.getMapInterface ().redo ();
	}
}
