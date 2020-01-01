package main;

import java.awt.Rectangle;
import java.util.LinkedList;

import map.Map;
import map.MapInterface;
import toolbar.Toolbar;

public class MainPanel extends GuiComponent {
	
	private TileSelectMenu tileMenu;
	private ObjectSelectMenu objectMenu;
	private MapInterface canvas;
	private Toolbar toolbar;
	private LinkedList<DisplayBox> boxes;
	
	private Map map;
	
	public MainPanel (Rectangle bounds, Gui gui) {
		super (bounds, gui);
		//Create the map
		map = new Map ();
		//Default dimensions
		tileMenu = new TileSelectMenu (new Rectangle (16, 0, 144, 240), this);
		objectMenu = new ObjectSelectMenu (new Rectangle (16, 240, 144, 240), this);
		toolbar = new Toolbar (new Rectangle (0, 0, 16, 480), this);
		canvas = new MapInterface (new Rectangle (160, 0, 480, 480), tileMenu, objectMenu, toolbar, this);
		boxes = new LinkedList<DisplayBox> ();
	}
	
	public MapInterface getMapInterface () {
		return canvas;
	}
	
	public TileSelectMenu getTileMenu () {
		return tileMenu;
	}
	
	public ObjectSelectMenu getObjectMenu () {
		return objectMenu;
	}
	
	public Map getMap () {
		return map;
	}

	public Toolbar getToolbar () {
		return toolbar;
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
}
