package map;

public interface MapEdit {
	
	/**
	 * Updates the map data to include this MapEdit.
	 * @param map the map to edit
	 * @return true if the edit was successful; false otherwise
	 */
	public boolean doEdit (Map map);
	
	/**
	 * Reverses the effect of this MapEdit
	 * @param map
	 * @return
	 */
	public boolean undo (Map map);
	
	/**
	 * Whether this map edit affects the map data.
	 * @return true if the map is affected; false otherwise
	 */
	public boolean affectsMap ();
}
