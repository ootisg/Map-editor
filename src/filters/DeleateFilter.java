package filters;


import java.awt.Rectangle;

import main.Filter;
import main.GuiComponent;
import main.MainPanel;
import main.OptionsMenu;
import main.Tile;
import main.TileSelection;
import map.EditCluster;
import map.MapInterface;
import map.ObjectEdit;
import map.TileEdit;
import resources.Sprite;

public class DeleateFilter extends Filter {

	OptionsMenu menu;
	public DeleateFilter(GuiComponent parent) {
		super(parent);
		this.setTexture(Sprite.getImage("resources/images/deleate_icon.png"));
		this.setName("Delete Filter");
		menu = new OptionsMenu(new Rectangle (250,200,10,1 ), parent, this);
		menu.hide();
	}
	
	@Override
	public void runFilterCode () {
		menu.show();
		
		TileSelection fillWith = new TileSelection(new Rectangle (250,200,80,80),menu);
		
		menu.addContent(fillWith);
		
		menu.addContent(new TileSelection(new Rectangle (250,200,80,80),menu));
	}
	@Override
	public void runFilter() {
		EditCluster clust = new EditCluster ();
		
		for (int i = 0; i < MainPanel.getMapInterface().getSelectedRegion().getTileWidth(); i++) {
			for (int j = 0; j < MainPanel.getMapInterface().getSelectedRegion().getTileHeight(); j++) {
				clust.addEdit(new TileEdit (MainPanel.getMapInterface().getSelectedRegion().getStartX() + i,MainPanel.getMapInterface().getSelectedRegion().getStartY() + j, 1, 1, MainPanel.getMap(), new Tile [][] {{null}}));
			}
		}
		
		for (int i = 0; i < MainPanel.getMapInterface().getSelectedRegion().getTileWidth(); i++) {
			for (int j = 0; j < MainPanel.getMapInterface().getSelectedRegion().getTileHeight(); j++) {
				if (MapInterface.objectsInTheMap[i + MainPanel.getMapInterface().getSelectedRegion().getStartX()][j + MainPanel.getMapInterface().getSelectedRegion().getStartY()] != null) {
					clust.addEdit(new ObjectEdit (MainPanel.getMapInterface().getSelectedRegion().getStartX() + i,MainPanel.getMapInterface().getSelectedRegion().getStartY() + j, MapInterface.objectsInTheMap, null));
				}
			}
		}
		MainPanel.getMapInterface().edit(clust);
		
	}
	
}
