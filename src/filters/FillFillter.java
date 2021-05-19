package filters;

import java.awt.Rectangle;
import java.util.HashMap;

import main.Filter;
import main.GuiComponent;
import main.MainPanel;
import main.OptionsMenu;
import main.Tile;
import main.TileSelection;
import map.EditCluster;
import map.TileEdit;
import resources.Sprite;

public class FillFillter extends Filter {

	OptionsMenu menu;
	
	public FillFillter(GuiComponent parent) {
		super(parent);
		this.setTexture(Sprite.getImage("resources/images/PaintBucketTool.png"));
		this.setName("Fill Filter");
	
		//Make the options menu
		menu = new OptionsMenu (new Rectangle (200, 100, 140, 20), getParent ().getMainPanel (), this);
		menu.hide ();
				
		TileSelection check2 = new TileSelection (new Rectangle (200, 120, 140, 80), menu);
		check2.setRequest ("Select reigon to use");
		TileSelection check3 = new TileSelection (new Rectangle (200, 200, 140, 80), menu);
		check3.setRequest ("Select area to apply to");
				
		//Add to the menu
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
	
		HashMap<String, Object> objs = menu.getAllData ();
		
		Tile[][] usel = ((TileSelection)objs.get ("Select reigon to use")).getSelectedArea ();
		Tile[][] asel = ((TileSelection)objs.get ("Select area to apply to")).getSelectedArea ();
		
		EditCluster clust = new EditCluster ();
		
		for (int i = 0; i < asel.length; i++) {
			for (int j = 0; j < asel[0].length; j++) {
				Tile toUse = usel[i%usel.length][j%usel[0].length];
				clust.addEdit(new TileEdit (MainPanel.getMapInterface().getSelectedRegion().getStartX() + j,MainPanel.getMapInterface().getSelectedRegion().getStartY() + i, 1, 1, MainPanel.getMap(), new Tile [][] {{toUse}}));
			}
		}
		
		MainPanel.getMapInterface().edit(clust);
	
	}
	
	
	

}
