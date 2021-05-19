package filters;


import main.Filter;
import main.GuiComponent;
import main.MainPanel;
import main.Tile;
import map.EditCluster;
import map.MapInterface;
import map.ObjectEdit;
import map.TileEdit;
import resources.Sprite;

public class DeleateFilter extends Filter {

	
	public DeleateFilter(GuiComponent parent) {
		super(parent);
		this.setTexture(Sprite.getImage("resources/images/deleate_icon.png"));
		this.setName("Delete Filter");
	}
	
	@Override
	public void runFilterCode () {
		
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
