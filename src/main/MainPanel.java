package main;

import java.awt.Rectangle;
import java.util.LinkedList;

import map.Map;
import map.MapInterface;
import toolbar.Toolbar;

public class MainPanel extends GuiComponent {
	
	private static TileSelectMenu tileMenu;
	private static ObjectSelectMenu objectMenu;
	private static VariantSelectMenu variantMenu;
	private static MapInterface canvas;
	private static Toolbar toolbar;
	private static VariantCloseButton closeButton;
	private static LinkedList<DisplayBox> boxes;
	private static ResizeWindow resizeWindow;
	private static LayerMenu layerMenu;
	private static BackgroundWindow backgroundWindow;
	private static AttributeSelectRegion attributeRegion;
	private static Map map;
	
	public MainPanel (Rectangle bounds, Gui gui) {
		super (bounds, gui);
		//Create the map
		map = new Map ();
		//Default dimensions
		tileMenu = new TileSelectMenu (new Rectangle (16, 0, 144, 240), this);
		objectMenu = new ObjectSelectMenu (new Rectangle (16, 240, 144, 240), this);
		toolbar = new Toolbar (new Rectangle (0, 0, 16, 480), this);
		canvas = new MapInterface (new Rectangle (160, 0, 480, 480), tileMenu, objectMenu, toolbar, this);
		variantMenu = new VariantSelectMenu (new Rectangle (160, 0, 96, 160), this);
		closeButton = new VariantCloseButton (new Rectangle (240,0,16,16),this);
		attributeRegion = new AttributeSelectRegion (new Rectangle (160,0,96,160), this);
		attributeRegion.hide();
		variantMenu.hide();
		closeButton.hide();
		boxes = new LinkedList<DisplayBox> ();
		setLayerMenu(new LayerMenu (LayerMenu.LAYER_MENU_BOUNDS, this));
		getLayerMenu().hide();
		backgroundWindow = new BackgroundWindow (new Rectangle (24, 104, BackgroundWindow.WINDOW_WIDTH, BackgroundWindow.WINDOW_HEIGHT), this);
		resizeWindow = new ResizeWindow (new Rectangle (24, 104, ResizeWindow.WINDOW_WIDTH, ResizeWindow.WINDOW_HEIGHT), this);
		attributeRegion.hide();
		
	}
	
	public static MapInterface getMapInterface () {
		return canvas;
	}
	public static VariantCloseButton getVariantCloseButton () {
		return closeButton;
	}
	public static AttributeSelectRegion getAttributeSelectRegion () {
		return attributeRegion;
	}
	public static TileSelectMenu getTileMenu () {
		return tileMenu;
	}
	
	public static ObjectSelectMenu getObjectMenu () {
		return objectMenu;
	}
	
	public static Map getMap () {
		return map;
	}

	public static Toolbar getToolbar () {
		return toolbar;
	}
	public static VariantSelectMenu getVariantMenu () {
		return variantMenu;
	}
	
	public static ResizeWindow getResizeWindow () {
		return resizeWindow;
	}
	
	public static BackgroundWindow getBackgroundWindow () {
		return backgroundWindow;
	}
	
	public DisplayBox addDisplayBox (Rectangle bounds, String message) {
		DisplayBox b = new DisplayBox (bounds, message, this);
		return b;
	}
	
	public void destroyDisplayBox (DisplayBox b) {
		boxes.remove (b);
	}
	
	@Override
	public void render () {
		super.render ();
	}

	public static LayerMenu getLayerMenu() {
		return layerMenu;
	}

	public static void setLayerMenu(LayerMenu layerMenu) {
		MainPanel.layerMenu = layerMenu;
	}
}
