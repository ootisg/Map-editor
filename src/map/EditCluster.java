package map;

import java.util.ArrayList;

public class EditCluster implements MapEdit {

	ArrayList <MapEdit> editsManaged = new ArrayList <MapEdit>();
	
	@Override
	public boolean doEdit() {
		
		for (int i = 0; i < editsManaged.size(); i++) {
			editsManaged.get(i).doEdit();
		}
		
		return true;
	}

	@Override
	public boolean undo() {
		
		for (int i = 0; i < editsManaged.size(); i++) {
			editsManaged.get(i).undo();
		}
		
		return true;
	}

	@Override
	public boolean affectsMap() {
		return true;
	}

	@Override
	public boolean isDiffrent(MapEdit mapEdit) {
		return true;
	}
	
	public void addEdit (MapEdit toAdd) {
		editsManaged.add(toAdd);
	}
}
