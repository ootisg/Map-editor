package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import map.Map;
import map.MapInterface;
import map.ResizeEdit;
import resources.Sprite;

public class ExtendButton extends GuiComponent {
	
	public static final int BACKGROUND_COLOR = 0xC0C0C0;
	public static final int OUTLINE_COLOR = 0x000000;
	
	public static final int COMPONENT_SPACING = 2;
	public static final int COMPONENT_MARGIN = 2;
	public static final int ICON_BUTTON_HORIZONTAL_OFFSET = 2;
	public static final int ICON_BUTTON_VERTICAL_OFFSET = 5;
	
	public static final int ENTRY_BOX_WIDTH = 25;
	public static final int ENTRY_BOX_HEIGHT = 17;
	
	public static final int ICON_BUTTON_WIDTH = 14;
	public static final int ICON_BUTTON_HEIGHT = 14;
	public static final int ICON_BUTTON_MARGIN_LEFT = 3;
	public static final int ICON_BUTTON_MARGIN_TOP = 3;
	
	public static final BufferedImage ICON_PTR_LEFT = Sprite.getImage ("resources/images/pointer_left.png");
	public static final BufferedImage ICON_PTR_RIGHT = Sprite.getImage ("resources/images/pointer_right.png");
	public static final BufferedImage ICON_PTR_UP = Sprite.getImage ("resources/images/pointer_up.png");
	public static final BufferedImage ICON_PTR_DOWN = Sprite.getImage ("resources/images/pointer_down.png");
	
	public static final int HORIZONTAL_WIDTH = ICON_BUTTON_WIDTH * 2 + ENTRY_BOX_WIDTH + COMPONENT_SPACING * 2 + COMPONENT_MARGIN + 2 + 2;
	public static final int HORIZONTAL_HEIGHT = ENTRY_BOX_HEIGHT + COMPONENT_MARGIN * 2 + 2;
	public static final int VERTICAL_WIDTH = ENTRY_BOX_WIDTH + COMPONENT_MARGIN * 2 + 2;
	public static final int VERTICAL_HEIGHT = ICON_BUTTON_HEIGHT * 2 + ENTRY_BOX_HEIGHT + COMPONENT_SPACING * 2 + COMPONENT_MARGIN + 2 + 2;
	
	private Layout layoutType;
	
	private IconButton button1;
	private IconButton button2;
	private EntryField entry;
	
	public enum Layout {
		HORIZONTAL,
		VERTICAL
	}
	
	public ExtendButton (Rectangle bounds, Layout layout, GuiComponent parent) {
		super (bounds, parent);
		//Set important shit
		layoutType = layout;
		
		if (layout == Layout.HORIZONTAL) {
			//Make components
			int workingX = COMPONENT_MARGIN + 1;
			button1 = new IconButton (new Rectangle (bounds.x + workingX, bounds.y + COMPONENT_MARGIN + ICON_BUTTON_HORIZONTAL_OFFSET + 1, ICON_BUTTON_WIDTH, ICON_BUTTON_HEIGHT), ICON_PTR_UP, this);
			workingX += ICON_BUTTON_WIDTH + COMPONENT_SPACING;
			entry = new EntryField (new Rectangle (bounds.x + workingX, bounds.y + COMPONENT_MARGIN + 1, ENTRY_BOX_WIDTH, ENTRY_BOX_HEIGHT), this);
			workingX += ENTRY_BOX_WIDTH + COMPONENT_SPACING;
			button2 = new IconButton (new Rectangle (bounds.x + workingX, bounds.y + COMPONENT_MARGIN + ICON_BUTTON_HORIZONTAL_OFFSET + 1, ICON_BUTTON_WIDTH, ICON_BUTTON_HEIGHT), ICON_PTR_DOWN, this);
			
			//Set component attributes
			button1.setMargins (ICON_BUTTON_MARGIN_LEFT, ICON_BUTTON_MARGIN_TOP);
			button2.setMargins (ICON_BUTTON_MARGIN_LEFT, ICON_BUTTON_MARGIN_TOP);
			
		} else if (layout == Layout.VERTICAL) {
			//Make components
			int workingY = COMPONENT_MARGIN + 1;
			button1 = new IconButton (new Rectangle (bounds.x + COMPONENT_MARGIN + ICON_BUTTON_VERTICAL_OFFSET + 1, bounds.y + workingY, ICON_BUTTON_WIDTH, ICON_BUTTON_HEIGHT), ICON_PTR_LEFT, this);
			workingY += ICON_BUTTON_HEIGHT + COMPONENT_SPACING;
			entry = new EntryField (new Rectangle (bounds.x + COMPONENT_MARGIN + 1, bounds.y + workingY, ENTRY_BOX_WIDTH, ENTRY_BOX_HEIGHT), this);
			workingY += ENTRY_BOX_HEIGHT + COMPONENT_SPACING;
			button2 = new IconButton (new Rectangle (bounds.x + COMPONENT_MARGIN + ICON_BUTTON_VERTICAL_OFFSET + 1, bounds.y + workingY, ICON_BUTTON_WIDTH, ICON_BUTTON_HEIGHT), ICON_PTR_RIGHT, this);

			//Set component attributes
			button1.setMargins (ICON_BUTTON_MARGIN_LEFT, ICON_BUTTON_MARGIN_TOP);
			button2.setMargins (ICON_BUTTON_MARGIN_LEFT, ICON_BUTTON_MARGIN_TOP);
			entry.setFilter ("[0-9]");
			
		}
	}

	public void moveTo (int x, int y) {
		Rectangle bounds = getBoundingRectangle ();
		Rectangle[] componentBounds = new Rectangle[] {button1.getBoundingRectangle (), button2.getBoundingRectangle (), entry.getBoundingRectangle ()};
		for (int i = 0; i < componentBounds.length; i ++) {
			componentBounds[i].x = componentBounds[i].x - bounds.x + x;
			componentBounds[i].y = componentBounds[i].y - bounds.y + y;
		}
		bounds.x = x;
		bounds.y = y;
	}
	
	@Override
	public void draw () {
		Graphics g = getGraphics ();
		Rectangle bounds = getBoundingRectangle ();
		g.setColor (new Color (BACKGROUND_COLOR));
		g.fillRect (0, 0, bounds.width, bounds.height);
		g.setColor (new Color (OUTLINE_COLOR));
		g.drawRect (0, 0, bounds.width - 1, bounds.height - 1);
		
		if (button1.pressed ()) {
			button1.reset ();
			MapInterface m = getMainPanel ().getMapInterface ();
			Map mp = getMainPanel ().getMap ();
			ResizeEdit rt = null;
			if (layoutType == Layout.HORIZONTAL) {
				rt = new ResizeEdit (mp.getWidth (), mp.getHeight () - Integer.parseInt (entry.getContent ()), mp, m);
			} else if (layoutType == Layout.VERTICAL) {
				rt = new ResizeEdit (mp.getWidth () - Integer.parseInt (entry.getContent ()), mp.getHeight (), mp, m);
			}
			m.edit (rt);
		}
		if (button2.pressed ()) {
			button2.reset ();
			MapInterface m = getMainPanel ().getMapInterface ();
			Map mp = getMainPanel ().getMap ();
			ResizeEdit rt = null;
			if (layoutType == Layout.HORIZONTAL) {
				rt = new ResizeEdit (mp.getWidth (), mp.getHeight () + Integer.parseInt (entry.getContent ()), mp, m);
			} else if (layoutType == Layout.VERTICAL) {
				rt = new ResizeEdit (mp.getWidth () + Integer.parseInt (entry.getContent ()), mp.getHeight (), mp, m);
			}
			m.edit (rt);
		}
	}
	
}
