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
		
		//Make the things
		TileSelection check = new TileSelection (new Rectangle (200, 120, 140, 80), menu);
		check.setRequest ("select a region");
		TileSelection check2 = new TileSelection (new Rectangle (200, 200, 140, 80), menu);
		check2.setRequest ("select a region");
		TileSelection check3 = new TileSelection (new Rectangle (200, 280, 140, 80), menu);
		check3.setRequest ("select a region");
		
	}
	
}
