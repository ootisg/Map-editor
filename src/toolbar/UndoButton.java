package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.MainPanel;
import resources.Sprite;

public class UndoButton extends ToolbarItem {
	
	public UndoButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/Undo.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		((MainPanel)(getParent ().getParent ())).getMapInterface ().undo ();
	}
}
