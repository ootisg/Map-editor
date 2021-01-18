package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	public static final BufferedImage IMAGE_MISSING = getMissingTexture ();
	private BufferedImage[] imageArray;
	private boolean isAnimated;
	private int frame;
	private int width;
	private int height;
	public Sprite (BufferedImage image) {
		imageArray = new BufferedImage[] {image};
		width = image.getWidth ();
		height = image.getHeight ();
		isAnimated = false;
	}
	public Sprite (String filepath) {
		imageArray = new BufferedImage[1];
		imageArray [0] = getImage (filepath);
		width = imageArray [0].getWidth ();
		height = imageArray [0].getHeight ();
		isAnimated = false;
	}
	public Sprite (String[] filepaths) throws IOException {
		int files = filepaths.length;
		imageArray = new BufferedImage[files];
		for (int i = 0; i < files; i ++) {
			imageArray [i] = getImage (filepaths [i]);
		}
		width = imageArray [0].getWidth ();
		height = imageArray [0].getHeight ();
		isAnimated = true;
	}
	public Sprite (Spritesheet spritesheet, int x, int y, int width, int height) {
		imageArray = new BufferedImage[1];
		imageArray[0] = spritesheet.getImage ().getSubimage (x, y, width, height);
		this.width = width;
		this.height = height;
		isAnimated = true;
	}
	public Sprite (Spritesheet spritesheet, int[] x, int[] y, int width, int height) {
		imageArray = new BufferedImage[x.length];
		if (x.length == y.length) {
			for (int i = 0; i < x.length; i ++) {
				imageArray [i] = spritesheet.getImage ().getSubimage (x[i], y[i], width, height);
			}
		}
		this.width = width;
		this.height = height;
		isAnimated = true;
	}
	public Sprite (Spritesheet spritesheet, int width, int height) {
		int sheetWidth = spritesheet.getWidth ();
		int sheetHeight = spritesheet.getHeight ();
		imageArray = new BufferedImage[(int) (Math.floor (sheetHeight / height) * Math.floor (sheetWidth / width))];
		for (int i = 0; i < Math.floor (sheetHeight / height); i ++) {
			for (int c = 0; c < Math.floor (sheetWidth / width); c ++) {
				imageArray [i * width + c] = spritesheet.getImage ().getSubimage (c * width, i * height, width, height);
			}
		}
		this.width = width;
		this.height = height;
		isAnimated = true;
	}
	public void draw (int x, int y) {
		Graphics bufferImage = MainLoop.getWindow ().getBuffer ();
		if (bufferImage != null) {
			bufferImage.drawImage (this.imageArray [frame], x, y, null);
		}
	}	
	public void draw (int x, int y, boolean flipHorizontal, boolean flipVertical) {
		int x1, x2, y1, y2;
		if (flipHorizontal) {
			x1 = width;
			x2 = 0;
		} else {
			x1 = 0;
			x2 = width;
		}
		if (flipVertical) {
			y1 = height;
			y2 = 0;
		} else {
			y1 = 0;
			y2 = height;
		}
		Graphics bufferImage = MainLoop.getWindow ().getBuffer ();
		if (bufferImage != null) {
			bufferImage.drawImage (this.imageArray [frame], x, y, x + width, y + height, x1, y1, x2, y2, null);
		}
	}
	public void setFrame (int frame) {
		this.frame = frame;
	}
	public int getFrame () {
		return frame;
	}
	public void setIsAnimated (boolean isAnimated) {
		this.isAnimated = isAnimated;
	}
	public boolean getIsAnimated () {
		return isAnimated;
	}
	public BufferedImage[] getImageArray () {
		return imageArray;
	}
	public int getFrameCount () {
		return imageArray.length;
	}
	public static BufferedImage getImage (String filepath) {
		try {
			return ImageIO.read (new File (filepath));
		} catch (IOException e) {
			return IMAGE_MISSING;
		}
	}
	private static BufferedImage getMissingTexture () {
		try {
			return ImageIO.read (new File ("missing.png"));
		} catch (IOException e) {
			return null;
		}
	}
}