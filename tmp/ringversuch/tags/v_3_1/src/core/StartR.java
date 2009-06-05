package core;

import gui.GUIMain;
import gui.GUIPrefs;

import java.awt.Dimension;


import javax.swing.JFrame;

public class StartR {

	public static void main(String[] args) {

		/**
		 * Schedule a job for the event-dispatching thread: creating and showing
		 * this application's GUI.
		 */
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				createAndShowGUIMain();
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUIMain() {

		// Create and set up the window.
		JFrame frame = new JFrame(GUIPrefs.NAME + " " + GUIPrefs.VERSION);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		GUIMain newContentPane = new GUIMain();

		// content panes must be opaque
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setMinimumSize(new Dimension(400, 200));
		frame.setVisible(true);
	}
}
