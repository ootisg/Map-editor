package map;

import java.awt.image.BufferedImage;

import main.GameObject;
import main.Tile;
import resources.Sprite;

public class ObjectEdit implements MapEdit {
	int startX;
	int startY;
	GameObject erasedObject;
	GameObject[][] Objects;
	GameObject objectToAdd;
	public ObjectEdit (int x, int y, GameObject[][] objects, GameObject newObject) {
		startX = x;
		startY = y;
		erasedObject = null;
		Objects = objects;
		objectToAdd = newObject;
	}
	@Override
	public boolean doEdit() {
		if (objectToAdd == null) {
			erasedObject = Objects[startX][startY];
		}
		Objects[startX][startY] = objectToAdd;
		MapInterface.objectsInTheMap = Objects;
		return true;
	}
	@Override
	public boolean undo() {
		Objects[startX][startY] = erasedObject;
		MapInterface.objectsInTheMap = Objects;
		return true;
	}

	@Override
	public boolean affectsMap() {
		return true;
	}
	

}
