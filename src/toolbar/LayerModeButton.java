package toolbar;


import main.MainPanel;
import resources.Sprite;

public class LayerModeButton extends ToolbarItem {
	public LayerModeButton (Toolbar parent) {
		super (parent);
		this.setBoxText("Layer Mode");
		setIcon (new Sprite ("resources/images/layer_mode_icon.png").getImageArray () [0]);
		setSelectable (false);
	}

	@Override
	public void use (int x, int y) {
		MainPanel.getMap ().toggleLayerMode ();
		MainPanel.getMap ().renderElements ();
	}
}
