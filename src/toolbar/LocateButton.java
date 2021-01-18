package toolbar;

import java.awt.Rectangle;

import main.DisplayBox;
import main.MainPanel;
import map.MapInterface;
import resources.Sprite;

public class LocateButton extends ToolbarItem {
	DisplayBox bonusBox = new DisplayBox (new Rectangle (this.getBoundingRectangle().x + 16,this.getBoundingRectangle().y,8,10),"0",this);
	public LocateButton (Toolbar parent) {
		super (parent);
		bonusBox.hide();
		this.setBoxText("Location Finder");
		setIcon (new Sprite ("resources/images/finderTool.png").getImageArray () [0]);
	}
	@Override
	public void use (int x, int y) {
		MapInterface mapInterface = MainPanel.getMapInterface();
		bonusBox.setMessage("x, " + (int) ((x/(16 * mapInterface.getScale())) + (mapInterface.getViewX()/(16 * mapInterface.getScale()))) + " y, " + (int) ((y/(16 * mapInterface.getScale())) + (mapInterface.getViewY()/(16 * mapInterface.getScale()))));
		bonusBox.setX(0);
		bonusBox.setY(432);
		bonusBox.show();
	}
	@Override
	public void onDeselect () {
		bonusBox.hide();
	}
}
