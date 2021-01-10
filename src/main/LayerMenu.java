package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import resources.Sprite;

public class LayerMenu extends GuiComponent {

	public static final Rectangle LAYER_MENU_BOUNDS = new Rectangle (32, 160, 128, 84);
	public static final Rectangle LAYER_SELECT_BOUNDS = new Rectangle (48, 200, 80, 130);
	public static final Rectangle LAYER_MODE_BUTTON_BOUNDS = new Rectangle (132, 224, 16, 16);
	public static final Rectangle ADD_BUTTON_BOUNDS = new Rectangle (74, 180, 16, 16);
	public static final Rectangle DELEATE_BUTTON_BOUNDS = new Rectangle (104, 180, 16, 16);
	
	
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
	
	private boolean layerMode = true;
	
	private LayerSelectionRegion region = new LayerSelectionRegion (LAYER_SELECT_BOUNDS,this);
	
	public static final BufferedImage EXIT_BUTTON_ICON = Sprite.getImage ("resources/images/close.png");
	public static final BufferedImage BOX_UNCHECKED_ICON = Sprite.getImage ("resources/images/box_unchecked.png");
	public static final BufferedImage BOX_CHECKED_ICON = Sprite.getImage ("resources/images/box_checked.png");
	public static final BufferedImage ADD_BUTTON = Sprite.getImage("resources/images/add_icon.png");
	public static final BufferedImage DELETE_BUTTON = Sprite.getImage("resources/images/remove_icon.png");
	
	
	protected LayerMenu(Rectangle bounds, GuiComponent parent) {
		super(bounds, parent);
		exitButton = new IconButton (new Rectangle (bounds.x + bounds.width - EXIT_BUTTON_ICON.getWidth (), bounds.y, EXIT_BUTTON_ICON.getWidth (), EXIT_BUTTON_ICON.getHeight ()), EXIT_BUTTON_ICON, this);
		layerModeButton = new IconButton (LAYER_MODE_BUTTON_BOUNDS, BOX_CHECKED_ICON, this);
		addButton = new IconButton (ADD_BUTTON_BOUNDS, ADD_BUTTON, this);
		deleteButton = new IconButton (DELEATE_BUTTON_BOUNDS, DELETE_BUTTON, this);
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
			region.addLayer();
			addButton.reset();
		}
		if (deleteButton.pressed()) {
			region.deleteLayer();
			deleteButton.reset();
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
		MainPanel.getMap().setLayer(region.getSelectedLayer());
	}
}
