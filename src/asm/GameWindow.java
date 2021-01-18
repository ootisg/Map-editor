package asm;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class GameWindow extends JFrame {
	boolean[] keysPressed;
	boolean[] keysPressedOnFrame;
	boolean[] keysReleasedOnFrame;
	ExtendedMouseListener mouseListener;
	ExtendedMouseMotionListener motionListener;
	BufferedImage bufferImage;
	BufferedImage consoleImage;
	Graphics bufferGraphics;
	WritableRaster bufferRaster;
	Insets insets;
	int numtest = 0;
	int[] resolution = {256, 144};
	int[] mouseCoords = null;
	int[] imageData;
	public GameWindow () {
		//Create buffers
		bufferImage = new BufferedImage (256, 144, BufferedImage.TYPE_INT_RGB); //Used for sprites
		//consoleImage = new BufferedImage (256, 144, BufferedImage.TYPE_INT_RGB); //The buffer for the dev console
		keysPressed = new boolean[256]; //Array for tracking which keys are down
		keysPressedOnFrame = new boolean[256]; //Array for tracking which keys have just been pressed
		keysReleasedOnFrame = new boolean[256]; //Array for tracking which keys have just been released
		imageData = new int[256 * 144];
		short[] many = new short[16 * 16];
		for (int i = 0; i < many.length; i ++) {
			many [i] = (short) (i * 128);
		}
		imageData [0] = rgb15To24 ((short)0x037F);
		drawSprite ((short)0, (short)0, (short)16, (short)16, many);
		bufferGraphics = bufferImage.getGraphics (); //Get a graphics interface for bufferedimage
		WritableRaster bufferRaster = bufferImage.getRaster ();
		//Makes sure that java closes
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit (0);
			}
		});
		this.pack (); //This line makes the code work for some reason
		this.insets = this.getInsets(); //Get offsets for the actual display part of the window
		this.setSize (1024 + insets.left + insets.right, 576 + insets.top + insets.bottom); //Sets the size of the window to get a useable size of 640x480
		this.setVisible (true); //Makes the window visible
		mouseListener = new ExtendedMouseListener (insets.left, insets.top); //Makes a mouse listener
		motionListener = new ExtendedMouseMotionListener (insets.left, insets.top); //Makes a mouse motion listener
		this.addMouseListener (mouseListener);
		this.addMouseMotionListener (motionListener);
		//This section handles keystroke detection
		KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager ();
		keyboardFocusManager.addKeyEventDispatcher (new KeyEventDispatcher () {
			@Override
			public boolean dispatchKeyEvent (KeyEvent e) {
				if (e.getID () == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode () <= 255) {
						keysPressed [e.getKeyCode ()] = true;
						keysPressedOnFrame [e.getKeyCode ()] = true;
					}
				}
				if (e.getID () == KeyEvent.KEY_RELEASED) {
					keysPressed [e.getKeyCode ()] = false;
					keysReleasedOnFrame [e.getKeyCode ()] = true;
				}
				return false;
			}
		});
	}
	public void updateClick () {
		//This method updates click information
		mouseCoords = mouseListener.getClick ();
		if (motionListener.getClicked ()) {
			mouseCoords = motionListener.getMouseCoords ();
		}
	}
	public int[] getClick () {
		//Returns the coordinates of the last click, or null if there wasn't a click this frame
		return mouseCoords;
	}
	public void doPaint () {
		//Refreshes the screen
		Graphics g = this.getGraphics ();
		if (bufferImage != null) {
			if (bufferGraphics != null) {
				bufferRaster = bufferImage.getRaster ();
				bufferRaster.setDataElements (0, 0, 256, 144, imageData);
				bufferImage.setData (bufferRaster);
				g.drawImage (bufferImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
			}
		}
		int[] usedResolution = this.getResolution ();
		bufferGraphics.setColor (new Color (0xC0C0C0));
		//Fills the screen with grey
		bufferGraphics.fillRect (0, 0, bufferImage.getWidth () - 1, bufferImage.getHeight () - 1);
		/*for (int i = 0; i < usedResolution [0] * usedResolution [1]; i ++) {
			imageData [i] = 0xC0C0C0;
		}*/
		/*bufferImage = new BufferedImage (resolution [0], resolution [1], BufferedImage.TYPE_INT_ARGB);
		if (bufferImage != null) {
			bufferGraphics = bufferImage.getGraphics ();
		}*/
		//bufferImage.flush ();
	}
	public void clearKeyArrays () {
		for (int i = 0; i < 256; i ++) {
			keysPressedOnFrame [i] = false;
			keysReleasedOnFrame [i] = false;
		}
	}
	public boolean keyCheck (int keyCode) {
		if (keyCode > 0 && keyCode <= 255) {
			return keysPressed [keyCode];
		} else {
			return false;
		}
	}
	public boolean keyPressed (int keyCode) {
		if (keyCode > 0 && keyCode <= 255) {
			return keysPressedOnFrame [keyCode];
		} else {
			return false;
		}
	}
	public boolean keyReleased (int keyCode) {
		if (keyCode > 0 && keyCode <= 255) {
			return keysReleasedOnFrame [keyCode];
		} else {
			return false;
		}
	}
	public Graphics getBuffer () {
		return bufferGraphics;
	}
	public int[] getResolution () {
		return resolution;
	}
	public int getMouseX () {
		return (int) (motionListener.getMouseCoords ()[0] * ((double) this.getResolution ()[0] / (this.getWidth () - insets.left - insets.right)));
	}
	public int getMouseY () {
		return (int) (motionListener.getMouseCoords ()[1] * ((double) this.getResolution ()[1] / (this.getHeight () - insets.top - insets.bottom)));
	}
	public void setResolution (int width, int height) {
		int[] usedResolution = {width, height};
		resolution = usedResolution;
		bufferImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
	}
	public void drawSprite (short x, short y, short width, short height, short[] data) {
		for (int i = 0; i < height; i ++) {
			for (int j = 0; j < width; j ++) {
				if ((data [i * width + j] & 0x8000) == 0) {
					imageData [(i + y) * bufferImage.getWidth () + j + x] = rgb15To24 (data [i * width + j]);
				}
			}
		}
	}
	private int rgb15To24 (short color) {
		int blue = (int)(((float)255 / 31) * (color & 0x1F));
		int green = (int)(((float)255 / 31) * ((color & 0x3E0) >> 5));
		int red = (int)(((float)255 / 31) * ((color & 0x7C00) >> 10));
		return blue + (green << 8) + (red << 16);
	}
	private int rgb7To24 (short color) {
		int blue = (int)(((float)255 / 3) * (color & 0x3));
		int green = (int)(((float)255 / 7) * ((color & 0x1C) >> 2));
		int red = (int)(((float)255 / 3) * ((color & 0x60) >> 5));
		return blue + (green << 8) + (red << 16);
	}
}