package filters;

import java.awt.Rectangle;

import main.EntryField;
import main.Filter;
import main.GuiComponent;
import main.TileSelection;

public class ReplaceFilter extends Filter {

	public ReplaceFilter (GuiComponent parent) {
		
		//Initialize the window
		super (parent);
		
		//Set the name
		setName ("Replace Filter");
		
		//Make the menu components
		TileSelection check = new TileSelection (new Rectangle (200, 120, 140, 80), getOptionsMenu ());
		check.setRequest ("select a region");
		TileSelection check2 = new TileSelection (new Rectangle (200, 200, 140, 80), getOptionsMenu ());
		check2.setRequest ("select a region");
		TileSelection check3 = new TileSelection (new Rectangle (200, 280, 140, 80), getOptionsMenu ());
		check3.setRequest ("select a region");
		
		//Add to the menu
		getOptionsMenu ().addContent (check);
		getOptionsMenu ().addContent (check2);
		getOptionsMenu ().addContent (check3);
		
	}
	
}
