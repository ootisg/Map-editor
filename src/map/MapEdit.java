package map;

public interface MapEdit {
	
	/**
	 * Updates the map data to include this MapEdit.
	 * @return true if the edit was successful; false otherwise
	 */
	public boolean doEdit ();
	
	/**
	 * Reverses the effect of this MapEdit
	 * @return
	 */
	public boolean undo ();
	
	/**
	 * Whether this map edit affects the map data.
	 * @return true if the map is affected; false otherwise
	 */
	public boolean affectsMap ();
}
