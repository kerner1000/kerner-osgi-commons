package de.fh.giessen.ringversuch.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.controller.Controller;

public class ViewImpl implements View {

	final static String MENU_TITLE = "Menu";
	final static String MENU_ABOUT = "About";
	final static String MENU_SETTINGS = "Settings";
	final static String BUTTON_START = "Start";
	final static String BUTTON_SELECT = "Select files";
	final static String BUTTON_SAVE = "Save in...";
	final static String BUTTON_CANCEL = "Cancel";
	final static String FILES_TITLE = "Files";
	final static String LOG_TITLE = "Log";
	final static String PROGRESS_AND_BUTTONS_TITLE = "Progress";
	private final static Logger LOGGER = Logger.getLogger(ViewImpl.class);

	private final ViewImplMain panel;
	private final ViewImplSettings panelSettings;
	public ViewImpl(final Controller  controller) {
		// Create and set up the content pane.
		// TODO must it be initialized in EventThread?
		panel = new ViewImplMain(controller);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(GUIPrefs.NATIVE_LAF){
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						LOGGER.warn("could not init native look and feel", e);
					}
				}
				final JFrame frame = new JFrame(GUIPrefs.NAME + " " + GUIPrefs.VERSION);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// content panes must be opaque
				panel.setOpaque(true);
				frame.setContentPane(panel);
				// Display the window.
				frame.pack();
				frame.setMinimumSize(new Dimension(400, 200));
				
				// TODO maybe move to "setOnline()"
				frame.setVisible(true);
			}
		});
	}

	@Override
	public void printMessage(final String message, final boolean isError) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.printMessage(message, isError);
			}
		});
	}

	@Override
	public void setOnline() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.setOnline();
			}
		});
	}

	@Override
	public void setWorking() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.setWorking();
			}
		});	
	}
}
