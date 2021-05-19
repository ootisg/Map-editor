package filters;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import main.Checkboxes;
import main.EntryField;
import main.Filter;
import main.GuiComponent;
import main.MainPanel;
import main.OptionsMenu;
import main.SelectionRegion.TileRegion;
import main.Tile;
import main.TileSelection;
import map.TileEdit;

public class ReplaceFilter extends Filter {

	OptionsMenu menu;
	
	public ReplaceFilter (GuiComponent parent) {
		
		//Initialize the window
		super (parent);
		
		//Set the name
		setName ("Replace Filter");
		
		//Make the options menu
		menu = new OptionsMenu (new Rectangle (200, 100, 140, 20), getParent ().getMainPanel (), this);
		menu.hide ();
		
		//Make the menu components
		TileSelection check = new TileSelection (new Rectangle (200, 120, 140, 80), menu);
		check.setRequest ("Select region to replace");
		TileSelection check2 = new TileSelection (new Rectangle (200, 200, 140, 80), menu);
		check2.setRequest ("Select reigon to use");
		TileSelection check3 = new TileSelection (new Rectangle (200, 280, 140, 80), menu);
		check3.setRequest ("Select area to apply to");
		
		//Add to the menu
		menu.addContent (check);
		menu.addContent (check2);
		menu.addContent (check3);
		
	}
	
	@Override
	public void runFilterCode () {
		if (menu.isHidden ()) {
			menu.show ();
		} else {
			menu.hide ();
		}
	}
	
	@Override
	public void runFilter () {
		
		//Get the tiles
		HashMap<String, Object> objs = menu.getAllData ();
		Tile[][] rsel = ((TileSelection)objs.get ("Select region to replace")).getSelectedArea ();
		Tile[][] usel = ((TileSelection)objs.get ("Select reigon to use")).getSelectedArea ();
		Tile[][] asel = ((TileSelection)objs.get ("Select area to apply to")).getSelectedArea ();
		
		//Check the dimensions of the search and replace fields
		if (rsel.length != usel.length || rsel[0].length != usel[0].length) {
			System.out.println ("They gotta be the same size");
			return;
		}
		
		//Search
		ArrayList<Rectangle> rects = new ArrayList<Rectangle> ();
		for (int sx = 0; sx < asel[0].length; sx++) {
			for (int sy = 0; sy < asel.length; sy++) {
				
				//Search for rectangle
				boolean found = true;
				for (int wx = 0; wx < rsel[0].length; wx++) {
					for (int wy = 0; wy < rsel.length; wy++) {
						if (sx + wx >= asel[0].length || sy + wy >= asel.length) {
							found = false;
						} else {
							Tile tile1 = asel [sy + wy][sx + wx];
							Tile tile2 = rsel [wy][wx];
							if (tile1 == null) {
								found = false;
							} else if (tile1.getIcon () != tile2.getIcon ()) {
								found = false;
							}
						}
					}
				}
				
				//Mark rectangle if found
				if (found) {
					rects.add (new Rectangle (sx, sy, usel[0].length, usel.length));
					for (int wx = 0; wx < rsel[0].length; wx++) {
						for (int wy = 0; wy < rsel.length; wy++) {
							asel [sy + wy][sx + wx] = null;
						}
					}
				}
				
			}
		}
		
		//Replace the found patterns
		TileRegion ts = ((TileSelection)objs.get ("Select region to replace")).getSelected ();
		for (int i = 0; i < rects.size (); i++) {
			Rectangle working = rects.get (i);
			TileEdit edit = new TileEdit (working.x + ts.getStartX (), working.y + ts.getStartY (), working.width, working.height, MainPanel.getMap (), usel);
			MainPanel.getMapInterface ().edit (edit);
		}
		
	}
	
}
