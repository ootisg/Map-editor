package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import map.LayerEdit;
import resources.Sprite;

public class LayerMenu extends GuiComponent {

	public static final Rectangle LAYER_MENU_BOUNDS = new Rectangle (32, 160, 128, 84);
	public static final Rectangle LAYER_SELECT_BOUNDS = new Rectangle (48, 200, 80, 16);
	public static final Rectangle LAYER_MODE_BUTTON_BOUNDS = new Rectangle (132, 224, 16, 16);
	public static final Rectangle ADD_BUTTON_BOUNDS = new Rectangle (74, 180, 16, 16);
	public static final Rectangle DELEATE_BUTTON_BOUNDS = new Rectangle (104, 180, 16, 16);
	public static final Rectangle UP_BUTTON_BOUNDS = new Rectangle (132, 186, 16, 16);
	public static final Rectangle DOWN_BUTTON_BOUNDS = new Rectangle (132, 206, 16, 16);
	
	public static final int BAR_PADDING = 0;
	public static final int BAR_HEIGHT = 16;
	public static final int BAR_SIDE_PADDING = 40;
	
	public static final int LAYER_MODE_TEXT_OFFSET = 12;
	
	public static final int WINDOW_PADDING_HORIZONTAL = 4;
	public static final int WINDOW_PADDING_VERTICAL = 20;
	public static final int COMPONENT_PADDING_HORIZONTAL = 52;
	public static final int COMPONENT_PADDING_VERTICAL = 4;
	
	public static final int BACKGROUND_COLOR = 0x808080;
	public static final int BAR_COLOR = 0x26B5B0;
	public static final int OUTLINE_COLOR = 0x000000;
	public static final int TEXT_COLOR = 0x000000;
	
	private IconButton exitButton;
	private IconButton layerModeButton;
	private IconButton addButton;
	private IconButton deleteButton;
	private IconButton upButton;
	private IconButton downButton;
	
	private boolean layerMode = true;
	
	private LayerSelectionRegion region = new LayerSelectionRegion (LAYER_SELECT_BOUNDS,this);
	
	public static final BufferedImage EXIT_BUTTON_ICON = Sprite.getImage ("resources/images/close.png");
	public static final BufferedImage BOX_UNCHECKED_ICON = Sprite.getImage ("resources/images/box_unchecked.png");
	public static final BufferedImage BOX_CHECKED_ICON = Sprite.getImage ("resources/images/box_checked.png");
	public static final BufferedImage ADD_BUTTON = Sprite.getImage("resources/images/add_icon.png");
	public static final BufferedImage DELETE_BUTTON = Sprite.getImage("resources/images/remove_icon.png");
	public static final BufferedImage UP_BUTTON = Sprite.getImage ("resources/images/up_arrow_icon.png");
	public static final BufferedImage DOWN_BUTTON = Sprite.getImage ("resources/images/down_arrow_icon.png");
	
	
	protected LayerMenu(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		exitButton = new IconButton (new Rectangle (bounds.x + bounds.width - EXIT_BUTTON_ICON.getWidth (), bounds.y, EXIT_BUTTON_ICON.getWidth (), EXIT_BUTTON_ICON.getHeight ()), EXIT_BUTTON_ICON, this);
		layerModeButton = new IconButton (LAYER_MODE_BUTTON_BOUNDS, BOX_CHECKED_ICON, this);
		addButton = new IconButton (ADD_BUTTON_BOUNDS, ADD_BUTTON, this);
		deleteButton = new IconButton (DELEATE_BUTTON_BOUNDS, DELETE_BUTTON, this);
		upButton = new IconButton (UP_BUTTON_BOUNDS, UP_BUTTON, this);
		downButton = new IconButton (DOWN_BUTTON_BOUNDS, DOWN_BUTTON, this);
		upButton.hide ();
		downButton.hide ();
		this.show ();
	}
	
	@Override
	public void setBoundingRectangle (Rectangle r) {
		super.setBoundingRectangle(r);
		layerModeButton.setBoundingRectangle(new Rectangle (layerModeButton.getBoundingRectangle().x,LAYER_MODE_BUTTON_BOUNDS.y + this.getBoundingRectangle().height - LAYER_MENU_BOUNDS.height,layerModeButton.getBoundingRectangle().width,layerModeButton.getBoundingRectangle().height));
	}
	
	@Override
	public void draw () {
		Rectangle bounds = getBoundingRectangle ();
		Graphics g = getGraphics ();
		g.setColor (new Color (BACKGROUND_COLOR));
		g.fillRect (0, 0, bounds.width, bounds.height);
		g.setColor (new Color (BAR_COLOR));
		g.fillRect (0, 0, bounds.width, BAR_PADDING + BAR_HEIGHT);
		g.setColor (new Color (OUTLINE_COLOR));
		g.drawRect (0, 0, bounds.width - 1, bounds.height - 1);
		g.drawLine (0, BAR_PADDING + BAR_HEIGHT, bounds.width, BAR_PADDING + BAR_HEIGHT);
		g.setColor (new Color (TEXT_COLOR));
		
		//Draw text
		FontMetrics font = g.getFontMetrics ();
		g.drawString ("LAYERS", BAR_SIDE_PADDING, BAR_PADDING + font.getAscent ());
		g.drawString ("LAYER MODE:", LAYER_MODE_TEXT_OFFSET, LAYER_MODE_BUTTON_BOUNDS.y - LAYER_MENU_BOUNDS.y + font.getAscent () + this.getBoundingRectangle().height - LAYER_MENU_BOUNDS.height);
	}

	@Override
	public void frameEvent () {
		region.frameEvent();
		//Check exit button
		if (exitButton.pressed ()) {
			exitButton.reset ();
			closeWindow ();
		}
		
		//Check layer mode button
		if (layerModeButton.pressed ()) {
			layerModeButton.reset ();
			layerMode = !layerMode;
			updateLayerModeButton ();	
		}
		if (addButton.pressed()) {
			MainPanel.getMapInterface ().edit (new LayerEdit (LayerEdit.LAYER_EDIT_TYPE_ADD, new int[] {}));
			addButton.reset();
		}
		if (deleteButton.pressed()) {
			MainPanel.getMapInterface ().edit (new LayerEdit (LayerEdit.LAYER_EDIT_TYPE_REMOVE, new int[] {region.getSelectedLayer ()}));
			deleteButton.reset();
		}
		
		//Check up/down buttons
		if (upButton.pressed ()) {
			if (region.getSelectedLayer () > 0) {
				int idx = region.getSelectedLayer ();
				MainPanel.getMapInterface ().edit (new LayerEdit (LayerEdit.LAYER_EDIT_TYPE_SWAP, new int[] {region.getSelectedLayer (), region.getSelectedLayer () - 1}));
			}
			upButton.reset ();
		}
		if (downButton.pressed ()) {
			if (region.getSelectedLayer () < region.numLayers - 1) {
				int idx = region.getSelectedLayer ();
				MainPanel.getMapInterface ().edit (new LayerEdit (LayerEdit.LAYER_EDIT_TYPE_SWAP, new int[] {region.getSelectedLayer (), region.getSelectedLayer () + 1}));
			}
			downButton.reset ();
		}
		
		//Hide the up/down arrows if no reigon is selected or they are non-applicable
		if (region.getSelectedLayer () == -1 || region.getElements ().length <= 1) {
			upButton.hide ();
			downButton.hide ();
		}
		
		//Do other stuff
	}
	
	public LayerSelectionRegion getRegion() {
		return region;
	}
	public void setRegion(LayerSelectionRegion region) {
		this.region = region;
	}
	public void updateLayerModeButton () {
		if (layerMode) {
			layerModeButton.setDefaultIcon (BOX_CHECKED_ICON);
		} else {
			layerModeButton.setDefaultIcon (BOX_UNCHECKED_ICON);
		}
		if (MainPanel.getMap ().inLayerMode () != layerMode) {
			MainPanel.getMap ().toggleLayerMode();
		}
	}
	
	public void updateArrows (int index) {
		if (index == -1) {
			upButton.hide ();
			downButton.hide ();
		} else {
			int centerY = LAYER_SELECT_BOUNDS.y + 16 * index + 9;
			upButton.getBoundingRectangle ().y = centerY - 18;
			downButton.getBoundingRectangle ().y = centerY + 1;
			upButton.show ();
			downButton.show ();
		}
	}
	
	@Override
	public void show () {
		super.show ();
		layerMode = MainPanel.getMap ().inLayerMode ();
		updateLayerModeButton ();
	}
	
	public void closeWindow () {
		hide ();
		if (MainPanel.getMap ().inLayerMode () != layerMode) {
			MainPanel.getMap ().toggleLayerMode();
		}
		if (region.getSelectedLayer() != -1) {
			MainPanel.getMap().setLayer(region.getSelectedLayer());
		}
	}
}
