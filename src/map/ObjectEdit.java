package map;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GameObject;
import main.Tile;
import resources.Sprite;

public class ObjectEdit implements MapEdit {
	int startX;
	int startY;
	ArrayList <GameObject> erasedObject;
	ArrayList <GameObject> [][] Objects;
	ArrayList<GameObject> objectToAdd;
	public ObjectEdit (int x, int y, ArrayList<GameObject>[][] objects, ArrayList <GameObject> newObject) {
		startX = x;
		startY = y;
		Objects = objects;
		objectToAdd = newObject;
		erasedObject = new ArrayList <GameObject>();
	}
	@Override
	public boolean doEdit() {
		if ((Objects[startX][startY] == null || Objects[startX][startY].isEmpty()) && objectToAdd == null) {
			return false;
		}
		
		if (Objects[startX][startY] != null) {
			for (int i = 0; i < Objects[startX][startY].size(); i++) {
				
				erasedObject.add((GameObject) Objects[startX][startY].get(i).clone());
				
			}
		}
		
		if (objectToAdd == null) {
				Objects[startX][startY].clear();
				return true;
		}
		
		if (Objects[startX][startY] == null) {
		Objects[startX][startY] = new ArrayList <GameObject>();
		}
		Objects[startX][startY].addAll(objectToAdd);
		MapInterface.objectsInTheMap = Objects;
		return true;
	}
	@Override
	public boolean undo() {
		
		Objects[startX][startY].clear();
		Objects[startX][startY].addAll(erasedObject);
		erasedObject.clear();
		MapInterface.objectsInTheMap = Objects;
		return true;
	}

	@Override
	public boolean affectsMap() {
		return true;
	}
	@Override 
	public boolean isDiffrent(MapEdit prev) {
		return true;
	}

}
