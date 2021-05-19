package filters;

import main.Filter;
import main.GuiComponent;
import resources.Sprite;

public class FillFillter extends Filter {

	public FillFillter(GuiComponent parent) {
		super(parent);
		this.setTexture(Sprite.getImage("resources/images/PaintBucketTool.png"));
		this.setName("Fill Filter");
	}
	
	

}
