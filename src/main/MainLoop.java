package main;

import javax.swing.JOptionPane;



public class MainLoop {
	private static GameWindow gameWindow;
	private static MainCode mainCode;
	private static int delay;
	private static long globalStartTime;
	public static void main (String[] args) {
		double framerate = 30;
		boolean running = true;
		long startTime;
		long delay = 0;
		gameWindow = new GameWindow ();
		mainCode = new MainCode ();
		mainCode.initialize ();
		while (running) {
			try {
				startTime = System.currentTimeMillis();
				mainCode.frameEvent ();
				gameWindow.clearKeyArrays ();
				gameWindow.doPaint ();
				delay = System.currentTimeMillis () - startTime;
				//System.out.println (System.currentTimeMillis() - startTime);
				if (delay < 33) {
					try {
						Thread.sleep (1000 / (long)framerate - delay - 1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				while (System.currentTimeMillis() - startTime < 1000 / (long)framerate) {
				}
			} catch (Exception e) {
				e.printStackTrace ();
				System.exit (0);
			}
		}
	}
	public static GameWindow getWindow () {
		return gameWindow;
	}
	public static long getDelay () {
		return delay;
	}
	public static void resetTimer () {
		globalStartTime = System.nanoTime ();
	}
	public static void printTime () {
		System.out.println (System.nanoTime () - globalStartTime);
	}
}