package toolbar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;

import main.DisplayableImageElement;
import main.GuiComponent;
import map.MapInterface;
import resources.Sprite;

public class Toolbar extends GuiComponent {
	
	public static final int BUTTON_OFFSET_X = 0;
	public static final int BUTTON_OFFSET_Y = 0;
	public static final int BUTTON_WIDTH = 16;
	public static final int BUTTON_HEIGHT = 16;

	private ToolbarItem lastConfigured = null;
	private ToolbarItem selected;
	
	public Toolbar (Rectangle bounds, GuiComponent parent) {
		super (bounds, parent);
		new SelectButton (this);
		new PlaceButton (this);
		new EraseButton (this);
		new CopyButton (this);
		new PasteButton (this);
		new UndoButton (this);
		new RedoButton (this);
		new ResizeButton (this);
		new NewLayerButton (this);
		new SwitchLayerButton (this);
		new LayerModeButton (this);
		new SaveButton (this);
		new LoadButton (this);
	}
	
	@Override
	public void draw () {
		super.draw ();
		Rectangle bounds = getBoundingRectangle ();
		Graphics g = getGraphics ();
		g.setColor (new Color (0x000000));
		g.drawRect (0, 0, bounds.width - 1, bounds.height - 1);
	}
	
	public void selectItem (ToolbarItem toSelect) {
		if (selected != null) {
			selected.setSelected (false);
		}
		toSelect.setSelected (true);
		selected = toSelect;
	}
	
	public ToolbarItem getSelectedItem () {
		return selected;
	}
	
	public void configure (ToolbarItem toConfigure) {
		int workingY = BUTTON_OFFSET_Y;
		if (lastConfigured != null) {
			workingY = (int)lastConfigured.getBoundingRectangle ().getY () + BUTTON_HEIGHT + BUTTON_OFFSET_X;
		}
		toConfigure.setBoundingRectangle (new Rectangle (BUTTON_OFFSET_X, workingY, BUTTON_WIDTH, BUTTON_HEIGHT));
		lastConfigured = toConfigure;
	}
}
