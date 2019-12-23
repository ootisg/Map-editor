package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import resources.Sprite;

public class Tileset extends DisplayableImageElement {
	
	private String path;
	private BufferedImage[][] images;
	
	public Tileset (String path, int tileWidth, int tileHeight, GuiComponent parent) {
		super (null, parent);
		images = parseImage (path, tileWidth, tileHeight);
		setIcon (images [0][0]);
		this.path = path;
	}
	
	public BufferedImage[][] parseImage (String path, int tileWidth, int tileHeight) {
		Sprite loadImg = null;
		loadImg = new Sprite (path);
		BufferedImage loadBuffer = loadImg.getImageArray ()[0];
		int horizTiles = loadBuffer.getWidth () / tileWidth;
		int vertTiles = loadBuffer.getHeight () / tileHeight;
		BufferedImage[][] result = new BufferedImage[vertTiles][horizTiles];
		for (int i = 0; i < vertTiles; i ++) {
			for (int j = 0; j < horizTiles; j ++) {
				result [i][j] = loadBuffer.getSubimage (j * tileWidth, i * tileHeight, tileWidth, tileHeight);
			}
		}
		return result;
	}
	
	public BufferedImage[][] getParsedImages () {
		return images;
	}
	
	public Tile[][] getParsedTiles (GuiComponent parent) {
		Tile[][] result = new Tile[images.length][images [0].length];
		for (int i = 0; i < images.length; i ++) {
			for (int j = 0; j < images[0].length; j ++) {
				result [i][j] = new Tile (images [i][j], parent);
			}
		}
		return result;
	}
	
	@Override
	public boolean equals (Object o) {
		if (o != null && o.getClass ().equals (getClass ())) {
			return ((Tileset)o).path.equals (path);
		}
		return false;
	}
}
