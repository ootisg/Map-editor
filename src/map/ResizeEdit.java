package map;

import java.awt.Rectangle;
import java.util.ArrayList;

import main.GameObject;
import main.Tile;

public class ResizeEdit implements MapEdit {

	ArrayList<TileEdit> tileEdits;
	ArrayList<ObjectEdit> objectEdits;
	
	MapInterface mi;
	
	int initialWidth;
	int initialHeight;
	
	int newWidth;
	int newHeight;
	
	public ResizeEdit (int width, int height, Map map, MapInterface mi) {
		//Set dimensions for future use
		initialWidth = map.getWidth ();
		initialHeight = map.getHeight ();
		newWidth = width;
		newHeight = height;
		
		//Save map interface for future use
		this.mi = mi;
		
		//Create edit lists
		tileEdits = new ArrayList<TileEdit> ();
		objectEdits = new ArrayList<ObjectEdit> ();
		
		//Get the checktangles
		int oldWidth = map.getWidth ();
		int oldHeight = map.getHeight ();
		ArrayList<Rectangle> checktangles = new ArrayList<Rectangle> ();
		if (oldWidth > width) {
			int bottomY = oldHeight;
			if (oldHeight > height) {
				bottomY = height;
			}
			checktangles.add (new Rectangle (width, 0, oldWidth - width, bottomY)); //Check over here for bug
		}
		if (oldHeight > height) {
			checktangles.add (new Rectangle (0, height, oldWidth, oldHeight - height));
		}
		
		//Check the checktangles (tiles)
		for (int li = 0; li < map.getNumLayers (); li ++) {
			map.changeLayer ();
			for (int i = 0; i < checktangles.size (); i ++) {
				Rectangle checktangle = checktangles.get (i);
				Tile[][] nullTiles = new Tile[checktangle.height][checktangle.width];
				TileEdit tEdit = new TileEdit (checktangle.x, checktangle.y, checktangle.width, checktangle.height, map, nullTiles);
				tileEdits.add (tEdit);
			}
		}
		
		//Check the checktangles (objects)
		ArrayList <GameObject>[][] objs = MapInterface.objectsInTheMap;
		for (int i = 0; i < checktangles.size (); i ++) {
			Rectangle checktangle = checktangles.get (i);
			for (int wx = 0; wx < checktangle.width; wx ++) {
				for (int wy = 0; wy < checktangle.height; wy ++) {
					ArrayList<GameObject> cur = objs[checktangle.x + wx][checktangle.y + wy];
					if (cur != null) {
						ObjectEdit oEdit = new ObjectEdit (checktangle.x + wx, checktangle.y + wy, objs, null);
						objectEdits.add (oEdit);
					}
				}
			}
		}
	}
	
	@Override
	public boolean doEdit () {
		for (int i = 0; i < tileEdits.size (); i ++) {
			mi.edit (tileEdits.get (i));
		}
		for (int i = 0; i < objectEdits.size (); i ++) {
			mi.edit (objectEdits.get (i));
		}
		
		//Resize stuff
		mi.resize (newWidth, newHeight);
		return true;
	}

	@Override
	public boolean undo () {
		//Check for bugsssssss
		mi.resize (initialWidth, initialHeight);
		for (int i = 0; i < tileEdits.size (); i ++) {
			mi.undo ();
		}
		for (int i = 0; i < objectEdits.size (); i ++) {
			mi.undo ();
		}
		return true;
	}

	@Override
	public boolean affectsMap () {
		return true;
	}
	@Override
	public boolean isDiffrent(MapEdit prev) {
		return true;
	}
}
