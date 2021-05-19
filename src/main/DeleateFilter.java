package main;


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
		
		
		
		MainPanel.getMapInterface ().edit (new TileEdit (MainPanel.getMapInterface().getSelectedRegion().getStartX(), MainPanel.getMapInterface().getSelectedRegion().getStartY(), MainPanel.getMapInterface().getSelectedRegion().getTiles().length * MainPanel.getMapInterface().getSelectedRegion().getTileWidth(), MainPanel.getMapInterface().getSelectedRegion().getTiles()[0].length * MainPanel.getMapInterface().getSelectedRegion().getTileHeight(), MainPanel.getMap (), new Tile[][] {{null}}));
		
		
		
	}
	
}
