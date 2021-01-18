package toolbar;

import java.awt.Rectangle;
import java.io.IOException;

import main.BackgroundWindow;
import main.DisplayBox;
import main.GuiComponent;
import main.MainPanel;
import resources.Sprite;

public class SwitchLayerButton extends ToolbarItem {
	
	private boolean mouseWasInside = false;
	
	private DisplayBox tbox;
	
	public static final int BOX_PADDING = 4;
	public static final int BOX_HEIGHT_OFFSET = -1;
	
	public SwitchLayerButton (Toolbar parent) {
		super (parent);
		setIcon (new Sprite ("resources/images/Switch Layer.png").getImageArray () [0]);
		setSelectable (false);
		tbox = getMainPanel ().addDisplayBox (new Rectangle (0, 0, 16, 16), "HIYA");
		tbox.hide ();
	}

	@Override
	public void use (int x, int y) {
		MainPanel.getLayerMenu().show();
		//Validate the background window
		GuiComponent bgWindow = getMainPanel ().getBackgroundWindow ();
		if (!bgWindow.isHidden ()) {
			bgWindow.hide ();
			bgWindow.show ();
		}
		tbox.setMessage (getBoxMessage ());
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent ();
		if (!mouseWasInside && mouseInside ()) {
			Rectangle r;
			Rectangle bounds = getBoundingRectangle ();
			r = new Rectangle (bounds.x + bounds.width + BOX_PADDING, bounds.y + BOX_HEIGHT_OFFSET, 1, 1);
			tbox.setBoundingRectangle (r);
			tbox.setMessage (getBoxMessage ());
			tbox.show ();
		} else if (mouseWasInside && !mouseInside ()) {
			if (tbox != null) {
				tbox.hide ();
			}
		}
		mouseWasInside = mouseInside ();
	}
	
	private String getBoxMessage () {
		MainPanel p = getMainPanel ();
		int currentLayer = p.getMap ().getTopDisplayLayer ();
		int numLayers = p.getMap ().getNumLayers ();
		String extra = "";
		if (currentLayer == 0) {
			extra = " (TOP)";
		} else if (currentLayer == numLayers - 1) {
			extra = " (BOTTOM)";
		}
		return "LAYER: " + currentLayer + extra;
	}
}
