package map;

import java.awt.image.BufferedImage;

import main.GameObject;
import main.Tile;
import resources.Sprite;

public class ObjectEdit implements  MapEdit {
	int startX;
	int startY;
	GameObject[][] Objects;
	GameObject objectToAdd;
	public ObjectEdit (int x, int y, GameObject[][] objects, GameObject newObject) {
		Objects = new GameObject [objects.length] [objects[0].length];
		startX = x;
		startY = y;
		Objects = objects;
		objectToAdd = newObject;
	}
	@Override
	public boolean doEdit() {
		Objects[startX][startY] = objectToAdd;
		MapInterface.objectsInTheMap = Objects;
		return true;
	}
	@Override
	public boolean undo() {
		Objects[startX][startY] = null;
		MapInterface.objectsInTheMap = Objects;
		return true;
	}

	@Override
	public boolean affectsMap() {
		return true;
	}
	

}
