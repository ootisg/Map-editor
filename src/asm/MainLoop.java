package asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*public class MainLoop {
	public static Engine engine;
	public static GameWindow gameWindow;
	public static long delay;
	public static void main (String[] args) {
		engine = new Engine (64);
		System.out.println(engine.getLargest(new int[] {1, 9, 5, 4, 8, 3, 2}));
		//engine.runInstruction ();
		File inFile = new File ("c.txt");
		FileInputStream inStream = null;
		byte[] fileData = new byte[(int)inFile.length ()];
		try {
			inStream = new FileInputStream (inFile);
			try {
				inStream.read (fileData);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String cString = new String (fileData);
		inFile = new File ("asm.txt");
		inStream = null;
		fileData = new byte[(int)inFile.length ()];
		try {
			inStream = new FileInputStream (inFile);
			try {
				inStream.read (fileData);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] compiledData = Assembler.Compile (new String (fileData));
		engine.loadData (compiledData, 0);
		while (engine.running) {
			engine.runInstruction ();
		}
		gameWindow = new GameWindow ();
		double framerate = 30; //The framerate; pretty self-explanitory
		boolean running = true; //Currently unused, but setting this to false would stop the game
		long startTime; //Used for loop timing
		delay = 0; //Used for loop timing
		gameWindow = new GameWindow (); //Create the window
		while (running) {
			//Everything in here is run once per frame
			startTime = System.currentTimeMillis(); //Used for loop timing
			try {
				//This try block catches any errors while the game is running
				gameWindow.updateClick (); //Updates mouse input information
				gameWindow.clearKeyArrays (); //Resets keystroke events that only fire for 1 frame
				gameWindow.doPaint (); //Refreshes the game window
			}
			catch (Throwable e) {
				//Displays the console if an error occurs, and print out information usable for debugging
				e.printStackTrace ();
				System.out.println ("A runtime error has occured: " + e.getClass ());
			}
			delay = System.currentTimeMillis () - startTime; //Used for timing of the loop
			//System.out.println (System.currentTimeMillis() - startTime);
			//The following is used for delaying the loop to a rate equal to the framerate
			if (delay < (1000 / framerate)) {
				try {
					if (1000 / (long) framerate - delay - 1 > 0) {
						Thread.sleep (1000 / (long) framerate - delay - 1);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while (System.currentTimeMillis() - startTime < 1000 / (long) framerate) {
			}
		}
	}
}*/