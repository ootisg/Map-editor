package map;

import java.util.ArrayList;

import main.MainPanel;
import map.Map.TileLayer;

public class LayerEdit implements MapEdit {

	public static final int LAYER_EDIT_TYPE_ADD = 1;
	public static final int LAYER_EDIT_TYPE_REMOVE = 2;
	public static final int LAYER_EDIT_TYPE_SWAP = 3;
	
	private int editType;
	
	private TileLayer data;
	
	private int layer1;
	private int layer2;
	
	public LayerEdit (int type, int[] indices) {
		
		//Fill in the fields
		this.editType = type;
		layer1 = indices.length > 0 ? indices [0] : -1;
		layer2 = indices.length > 1 ? indices [1] : -1; //Bounds check
		
	}

	@Override
	public boolean doEdit () {
	
		switch (editType) {
			case LAYER_EDIT_TYPE_ADD:
				MainPanel.getLayerMenu ().getRegion ().addLayer ();
				break;
			case LAYER_EDIT_TYPE_REMOVE:
				ArrayList<TileLayer> layers = MainPanel.getMap ().getTileData ();
				data = layers.get (layer1);
				MainPanel.getLayerMenu ().getRegion ().selectLayer (layer1, true);
				MainPanel.getLayerMenu ().getRegion ().deleteLayer ();
				break;
			case LAYER_EDIT_TYPE_SWAP:
				MainPanel.getLayerMenu ().getRegion ().selectLayer (layer1, false);
				MainPanel.getLayerMenu ().getRegion ().swapLayers (layer1, layer2);
				break;
			default:
				return true;
		}
		
		return true;
		
	}

	@Override
	public boolean undo () {
		
		switch (editType) {
			case LAYER_EDIT_TYPE_ADD:
				int len = MainPanel.getMap ().getTileData ().size ();
				MainPanel.getLayerMenu ().getRegion ().selectLayer (len - 1, false);
				MainPanel.getLayerMenu ().getRegion ().deleteLayer ();
				break;
			case LAYER_EDIT_TYPE_REMOVE:
				MainPanel.getLayerMenu ().getRegion ().insertLayer (data, layer1);
				break;
			case LAYER_EDIT_TYPE_SWAP:
				MainPanel.getLayerMenu ().getRegion ().selectLayer (layer2, false);
				MainPanel.getLayerMenu ().getRegion ().swapLayers (layer2, layer1);
				break;
			default:
				return false;
		}
		
		return true;
		
	}

	@Override
	public boolean affectsMap () {
		return true;
	}

	@Override
	public boolean isDiffrent (MapEdit mapEdit) {
		return true;
	}
	
}
