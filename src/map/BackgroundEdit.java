package map;

public class BackgroundEdit implements MapEdit {

	private String oldBackgroundPath;
	private double oldScrollX;
	private double oldScrollY;
	
	private String backgroundPath;
	private double scrollX;
	private double scrollY;
	
	private Map.TileLayer usedLayer;
	
	public BackgroundEdit (String filepath, double scrollX, double scrollY, Map m) {
		//Set layer
		usedLayer = m.getActiveLayer ();
		
		//Set old vars
		oldBackgroundPath = usedLayer.getBackgroundPath ();
		oldScrollX = usedLayer.getBackgroundScrollX ();
		oldScrollY = usedLayer.getBackgroundScrollY ();
		
		//Update new vars and background stuff
		backgroundPath = filepath;
		this.scrollX = scrollX;
		this.scrollY = scrollY;
	}
	
	public BackgroundEdit (String filepath, Map m) {
		this (filepath, m.getActiveLayer ().getBackgroundScrollX (), m.getActiveLayer ().getBackgroundScrollY (), m);
	}
	
	@Override
	public boolean doEdit () {
		try {
			usedLayer.setBackground (backgroundPath);
			usedLayer.setBackgroundScroll (scrollX, scrollY);
		} catch (IllegalStateException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean undo () {
		try {
			usedLayer.setBackground (oldBackgroundPath);
			usedLayer.setBackgroundScroll (oldScrollX, oldScrollY);
		} catch (IllegalStateException e) {
			return false;
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
