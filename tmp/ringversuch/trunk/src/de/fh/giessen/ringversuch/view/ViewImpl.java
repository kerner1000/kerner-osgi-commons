package de.fh.giessen.ringversuch.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.fh.giessen.ringversuch.controller.Controller;

public class ViewImpl implements View {

	private final class MyPanel extends JPanel {

		private final class MyListener implements ActionListener {
			private final Component component;
			private final Controller controller;

			MyListener(Component component, Controller controller) {
				this.component = component;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonSelect) {
					final int returnVal = fileChooserinputFiles.showOpenDialog(component);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						areaFiles.setText("");
						files.clear();
						File[] inputFiles = fileChooserinputFiles.getSelectedFiles();
						for (File f : inputFiles) {
							areaFiles.append(f.getName() + "\n");
							files.add(f);
						}
						inputFilesSelected = true;
						if (inputFilesSelectedOutputDirSelected()) {
							setInputFilesSelectedOutputDirSelected();
						}
						controller.printMessage("selected " + files.size() + " file(s)", false);
					}
				}

				else if (e.getSource() == buttonSave) {
					final int returnVal = fileChooseroutDir.showSaveDialog(component);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						controller.setOutFile(fileChooseroutDir.getSelectedFile())
						outputDirSelected = true;
						if (inputFilesSelectedOutputDirSelected())
							setInputFilesSelectedOutputDirSelected();
					}
				}

				else if (e.getSource() == menuSettings) {
					showSettingsView();
				}

				else if (e.getSource() == buttonStart) {
					if(controller.settingsValid()){
						setWorking();
						exe.submit(new Worker());
					} else {
						controller.showError("Invalid settings");
					}
				}
			}
		}

		private final static String MENU_TITLE = "Menu";
		private final static String MENU_ABOUT = "About";
		private final static String MENU_SETTINGS = "Settings";
		private final static String BUTTON_START = "Start";
		private final static String BUTTON_SELECT = "Select files";
		private final static String BUTTON_SAVE = "Save in...";
		private final static String BUTTON_CANCEL = "Cancel";
		private final static String FILES_TITLE = "Files";
		private final static String LOG_TITLE = "Log";
		private final static String PROGRESS_AND_BUTTONS_TITLE = "Progress";
		private final ActionListener myListener = new MyListener(this);
		private final Collection<File> files = new Vector<File>();
		private final JTextArea areaFiles = new JTextArea();
		private final JFileChooser fileChooserinputFiles,
				fileChooseroutDir = new JFileChooser();
		private final JButton buttonStart = new JButton(BUTTON_START);
		private final JMenu menu = new JMenu(MENU_TITLE);
		private final JMenuItem menuAbout = new JMenuItem(MENU_ABOUT);
		private final JMenuItem menuSettings = new JMenuItem(MENU_SETTINGS);
		private final JProgressBar progressBar = new JProgressBar(0, 100);
		private JButton buttonSelect, buttonSave, buttonCancel;
		private boolean inputFilesSelected = false;
		private boolean outputDirSelected = false;
		private int progress = 0;

		MyPanel() {
			init();
		}

		private void init() {
			initButtons(myListener);
			initInputFilesChooser();
			initOutputDirFileChooser();
			setLayout(new BorderLayout());
			add(initMenubar(myListener), BorderLayout.BEFORE_FIRST_LINE);
			add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, initFilesPanel(),
					initLogPanel()), BorderLayout.CENTER);
			add(initProgressAndButtonsPanel(), BorderLayout.AFTER_LAST_LINE);
		}

		private void initOutputDirFileChooser() {
			fileChooseroutDir
					.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		private void initInputFilesChooser() {
			fileChooserinputFiles.setMultiSelectionEnabled(true);
			final FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Excel file", "xls", "XLS");
			fileChooserinputFiles.addChoosableFileFilter(filter);
		}

		private JPanel initButtonsPanel() {
			final JPanel p1 = new JPanel();
			p1.setLayout(getTwoByTowGridLayout());
			p1.add(buttonSelect);
			p1.add(buttonSave);
			p1.add(buttonStart);
			p1.add(buttonCancel);
			return p1;
		}

		private JPanel initProgressAndButtonsPanel() {
			final JPanel p1 = new JPanel();
			p1.setLayout(new BorderLayout());
			final TitledBorder b1 = new TitledBorder(PROGRESS_AND_BUTTONS_TITLE);
			p1.setBorder(b1);
			progressBar.setValue(0);
			progressBar.setStringPainted(true);
			p1.add(initButtonsPanel(), BorderLayout.WEST);
			p1.add(progressBar, BorderLayout.CENTER);
			return p1;
		}

		private JPanel initLogPanel() {
			final JTextArea areaLog = new JTextArea();
			areaLog.setEditable(false);
			final JScrollPane s = new JScrollPane(areaLog);
			final JPanel p1 = new JPanel();
			final TitledBorder b1 = new TitledBorder(LOG_TITLE);
			p1.setBorder(b1);
			p1.setLayout(new BorderLayout());
			final Dimension minimumSize = new Dimension(150, 50);
			p1.setMinimumSize(minimumSize);
			p1.add(s, BorderLayout.CENTER);
			return p1;
		}

		private JPanel initFilesPanel() {
			areaFiles.setEditable(false);
			final JScrollPane s = new JScrollPane(areaFiles);
			final JPanel p1 = new JPanel();
			final TitledBorder b1 = new TitledBorder(FILES_TITLE);
			p1.setBorder(b1);
			p1.setLayout(new BorderLayout());
			final Dimension minimumSize = new Dimension(150, 50);
			p1.setMinimumSize(minimumSize);
			p1.add(s);
			return p1;
		}

		private void initButtons(ActionListener listener) {
			buttonSelect.addActionListener(listener);
			buttonStart.addActionListener(listener);
			buttonCancel.addActionListener(listener);
			buttonSave.addActionListener(listener);
		}

		private JMenuBar initMenubar(ActionListener listener) {
			final JMenuBar menuBar = new JMenuBar();
			menuBar.add(menu);
			menu.add(menuSettings);
			menu.add(menuAbout);
			menuAbout.addActionListener(listener);
			menuSettings.addActionListener(listener);
			return menuBar;
		}

		private LayoutManager getThreeByThreeGridLayout() {
			final GridLayout sechs = new GridLayout(3, 3);
			sechs.setHgap(4);
			sechs.setVgap(4);
			return sechs;
		}

		private LayoutManager getTwoByTowGridLayout() {
			final GridLayout vier = new GridLayout(2, 2);
			vier.setHgap(4);
			vier.setVgap(4);
			return vier;
		}

		private void setOffline() {
			menu.setEnabled(false);
			buttonCancel.setEnabled(false);
			buttonSave.setEnabled(false);
			buttonSelect.setEnabled(false);
			buttonStart.setEnabled(false);
		}

		private void setOnline() {
			menu.setEnabled(true);
			buttonCancel.setEnabled(false);
			buttonStart.setEnabled(false);
			buttonSelect.setEnabled(true);
			buttonSave.setEnabled(true);
		}

		private void setWorking() {
			menu.setEnabled(true);
			buttonCancel.setEnabled(true);
			buttonStart.setEnabled(false);
			buttonSelect.setEnabled(false);
			buttonSave.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			progressBar.setMaximum(files.size());
			progress = 0;
			progressBar.setValue(progress);
		}

		public void setInputFilesSelectedOutputDirSelected() {
			menu.setEnabled(true);
			buttonCancel.setEnabled(false);
			buttonStart.setEnabled(true);
			buttonSelect.setEnabled(true);
			buttonSave.setEnabled(true);
			setCursor(null);
		}

		boolean inputFilesSelectedOutputDirSelected() {
			if (inputFilesSelected == true && outputDirSelected == true)
				return true;
			else
				return false;
		}

	}

	private final ExecutorService exe = Executors.newSingleThreadExecutor();

	private ViewImpl() {
		

		

		


	}
	
	public static void createAndShowMainView(){
		
	}

	

	private void createGUISettings() {
		frameSettings = new JFrame("Settings");
		frameSettings.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		panelSettings = new GUISettings(settings, this, frameSettings);

		panelSettings.setOpaque(true);
		frameSettings.setContentPane(panelSettings);

		frameSettings.pack();
		frameSettings.setResizable(false);

	}

	private FileInputStream loadSettings() throws FileNotFoundException,
			ArrayIndexOutOfBoundsException {

		class Filter implements FilenameFilter {
			public boolean accept(File f, String s) {
				return s.toLowerCase().endsWith(".ini");
			}
		}

		Filter filter = new Filter();
		File pfad = new File(System.getProperty("user.dir"));
		String[] dateien = pfad.list(filter);

		areaLog.append("Loaded settings from " + pfad + File.separator
				+ dateien[0] + "\n");

		return new FileInputStream(dateien[0]);
	}

	private Properties createDefaultSettings() {

		/**
		 * Create and laod default properties
		 */
		Properties defaultsettings = new Properties();
		defaultsettings.setProperty("LABOR_NO_ROW", "e.g. 1");
		defaultsettings.setProperty("LABOR_NO_COLUMN", "e.g. A");
		defaultsettings.setProperty("PROBE_NO_ROW", "e.g. 1");
		defaultsettings.setProperty("PROBE_NO_COLUMN", "e.g. A");
		defaultsettings.setProperty("PROBE_USE", "e.g. 1");
		defaultsettings.setProperty("SHEET_NO", "e.g. 1");
		defaultsettings.setProperty("SUBSTANCES_COLUMN", "e.g. A");
		defaultsettings.setProperty("MAIN_START_ROW", "e.g. 1");
		defaultsettings.setProperty("MAIN_START_COLUMN", "e.g A");
		defaultsettings.setProperty("MAIN_END_ROW", "e.g. 1");
		defaultsettings.setProperty("MAIN_END_COLUMN", "e.g A");

		/**
		 * Versuche Einstellungen vom letzen Mal zu laden. Falls das scheitert,
		 * wird die Gui mit unbrauchbaren Einstellungen / default Einstellungen
		 * (Tipps zur Eingabe) geladen.
		 */
		/**
		 * create application properties with default
		 */
		return new Properties(defaultsettings);
	}

}
