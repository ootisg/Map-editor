package main;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	public boolean[] keysPressed;
	boolean[] keysPressedOnFrame;
	boolean[] keysReleasedOnFrame;
	BufferedImage bufferImage;
	BufferedImage rasterImage;
	WritableRaster rasterBuffer;
	Graphics bufferGraphics;
	Insets insets;
	Gui gui;
	JFileChooser chooser;
	ExtendedMouseListener mouseListener;
	ExtendedMouseWheelListener mouseWheelListener;
	int numtest = 0;
	int[] imageData;
	int[] resolution = {640, 480};
	public GameWindow () {
		bufferImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_ARGB);
		rasterImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_RGB);
		rasterBuffer = rasterImage.getRaster ();
		keysPressed = new boolean[256];
		keysPressedOnFrame = new boolean[256];
		keysReleasedOnFrame = new boolean[256];
		imageData = new int [307200];
		bufferGraphics = bufferImage.getGraphics ();
		gui = new Gui (this);
		for (int i = 0; i < 307200; i ++) {
			imageData [i] = 0xC0C0C0;
		}
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit (0);
			}
		});
		this.pack ();
		this.setSize (640, 480);
		this.setVisible (true);
		this.insets = this.getInsets();
		this.setSize (640 + insets.left + insets.right, 480 + insets.top + insets.bottom);
		this.mouseListener = new ExtendedMouseListener (this, gui);
		this.mouseWheelListener = new ExtendedMouseWheelListener (this, gui);
		this.addMouseListener (mouseListener);
		this.addMouseMotionListener (mouseListener);
		this.addMouseWheelListener (mouseWheelListener);
		KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager ();
		keyboardFocusManager.addKeyEventDispatcher (new KeyEventDispatcher () {
			@Override
			public boolean dispatchKeyEvent (KeyEvent e) {
				gui.doKeyEvent (e);
				return false;
			}
		});
	}
	public void frameEvent () {
		gui.frameEvent ();
	}
	public int[] getViewportCoords (int windowX, int windowY) {
		int viewWidth = this.getWidth () - insets.left - insets.right;
		int viewHeight = this.getHeight () - insets.top - insets.bottom;
		int projX = (int)(((float)(windowX - insets.left) / viewWidth) * 640);
		int projY = (int)(((float)(windowY - insets.top) / viewHeight) * 480);
		return new int[] {projX, projY};
	}
	public void doPaint () throws IOException {
		gui.render ();
		Graphics g = this.getGraphics ();
		if (bufferImage != null) {
			if (bufferGraphics != null) {
				rasterBuffer.setDataElements (0, 0, resolution [0], resolution [1], imageData);
				rasterImage.getGraphics ().drawImage (bufferImage, 0, 0, null);
				g.drawImage (rasterImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
			}
		}
		int[] usedResolution = this.getResolution ();
		for (int i = 0; i < usedResolution [0] * usedResolution [1]; i ++) {
			imageData [i] = 0xC0C0C0;
		}
		/*bufferImage = new BufferedImage (resolution [0], resolution [1], BufferedImage.TYPE_INT_ARGB);
		if (bufferImage != null) {
			bufferGraphics = bufferImage.getGraphics ();
		}*/
		bufferGraphics.setColor(new Color(0xC0C0C0));
		bufferGraphics.fillRect (0, 0, 640, 480);
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
	public int[] getImageData () {
		return imageData;
	}
	public int[] getResolution () {
		return resolution;
	}
	public int getMouseX () {
		return mouseListener.mouseX;
	}
	public int getMouseY () {
		return mouseListener.mouseY;
	}
	public void setResolution (int width, int height) {
		int[] usedResolution = {width, height};
		resolution = usedResolution;
		imageData = new int[width * height];
		bufferImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
		rasterImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
		rasterBuffer = rasterImage.getRaster ();
		for (int i = 0; i < width * height; i ++) {
			imageData [i] = 0xC0C0C0;
		}
	}
}