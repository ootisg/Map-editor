package toolbar;

import main.MainPanel;
import resources.Sprite;

public class ResizeButton extends ToolbarItem {
	public ResizeButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Resize");		
		setIcon (new Sprite ("resources/images/resize_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		if (MainPanel.getResizeWindow ().isHidden ()) {
			MainPanel.getResizeWindow ().show ();
		}
	}
	
	@Override
	public void useDrag (int x, int y) {
		use (x, y);
	}
	
	@Override
	public void onSelect () {
		MainPanel.getMapInterface ().deselect ();
	}
}
