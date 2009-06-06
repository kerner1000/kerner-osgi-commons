package gui;

import interfaces.RingversuchGUI;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
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
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.Read;

@SuppressWarnings("serial")
public class GUIMain extends JPanel implements ActionListener, RingversuchGUI {

	private JMenu menu;

	private JButton buttonStart, buttonSelect, buttonSave, buttonCancel;

	private JFileChooser fc, fc2;

	JTextArea areaFiles, areaLog;

	private Vector<File> dateien;

	private boolean selected, out;

	private JMenuItem menuAbout, menuSettings;

	private File outfile;

	private Properties settings;

	private JProgressBar bar;

	private int pro;

	private GUISettings panelSettings;

	private JFrame frameSettings;

	private final boolean debug = false;

	private class Worker extends Thread {

		RingversuchGUI gui;

		JPanel panel;

		public Worker(RingversuchGUI gui, JPanel panel) {
			this.gui = gui;
			this.panel = panel;

		}

		public void run() {

			try {

				setWorking();

				Read read = new Read(gui);

				read.setSheetNo(Integer.parseInt(settings
						.getProperty("SHEET_NO")) - 1);

				read
						.setLaboratoryColumn(settings.getProperty(
								"LABOR_NO_COLUMN").toUpperCase().charAt(0) - 64 - 1);
				read.setLaboratoryRow(Integer.parseInt(settings
						.getProperty("LABOR_NO_ROW")) - 1);

				read.setProbeNo(Integer.parseInt(settings
						.getProperty("PROBE_USE")));

				read.setProbeNoColum(settings.getProperty("PROBE_NO_COLUMN")
						.toUpperCase().charAt(0) - 64 - 1);
				read.setProbeNoRow(Integer.parseInt(settings
						.getProperty("PROBE_NO_ROW")) - 1);

				read.setValuesStartRow(Integer.parseInt(settings
						.getProperty("MAIN_START_ROW")) - 1);
				read.setValuesStartColumn(settings.getProperty(
						"MAIN_START_COLUMN").toUpperCase().charAt(0) - 64 - 1);
				read.setValuesEndRow(Integer.parseInt(settings
						.getProperty("MAIN_END_ROW")) - 1);
				read.setValuesEndColumn(settings.getProperty("MAIN_END_COLUMN")
						.toUpperCase().charAt(0) - 64 - 1);

				read.setColumnOfSubstances(settings.getProperty(
						"SUBSTANCES_COLUMN").toUpperCase().charAt(0) - 64 - 1);

				int skippedFiles = 0;

				for (int i = 0; i < dateien.size(); i++) {
					if (!isInterrupted()) {
						bar.setString("processing " + dateien.get(i).getName());
						skippedFiles += read.readFile(dateien.elementAt(i));
					} else {
						appendLog("stopped\n", false);
						break;
					}
					pro++;
					bar.setValue(pro);
				}

				if (!isInterrupted()) {
					read.writeFiles(outfile);
					if (debug)
						System.out
								.println("Worker.run: \"writeFiles()\" called.");
				}

				if (!isInterrupted()) {
					appendLog("done! (skipped " + skippedFiles + " files)\n",
							false);
					bar.setString("done");
				}

				else
					appendLog("stopped\n", false);

				setReady();

			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} catch (Throwable e) {
				e.printStackTrace();
				gui.appendLog(e.getMessage() + "\n", true);

				JOptionPane.showMessageDialog(panel,
						"An error occured. Please see log for futher details.",
						"Error", JOptionPane.ERROR_MESSAGE);

				try {
					e.printStackTrace(new PrintStream("RingversuchError.txt"));
					gui.appendLog("Error log written to "
							+ System.getProperty("user.dir")
							+ "/RingversuchError.txt\n", true);
				} catch (Exception e1) {
					gui.appendLog("Could not Write Error Log\n", true);
					e1.printStackTrace();
				}

				gui.setReady();
			}
		}
	}

	public GUIMain() {
		init();
		setOffline();
		settings = createDefaultSettings();

		appendLog("Looking for settings in " + "\""
				+ System.getProperty("user.dir") + "\" ...\n", false);

		try {
			settings.load(loadSettings());

		} catch (FileNotFoundException e) {
			appendLog("das sollte nicht passieren...\n", true);
		} catch (ArrayIndexOutOfBoundsException e) {
			appendLog("No settings file found, using default\n", false);
			appendLog(
					"Attention: default settings are not valid and cannot be used!\n",
					true);
		} catch (IOException e) {
			appendLog("Could not read settings file\n", true);
			// e.printStackTrace();
		}

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUISettings();
			}
		});

		setOnline();

	}

	public void appendLog(String s, boolean error) {

		// if (error) {
		// areaLog.setForeground(Color.red);
		// areaLog.append("An error has occured: ");
		// } else
		// areaLog.setForeground(Color.black);

		areaLog.append(s);
		areaLog.setCaretPosition(areaLog.getDocument().getLength());

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

	private void init() {

		dateien = new Vector<File>();

		menuAbout = new JMenuItem("About");
		menuSettings = new JMenuItem("Settings");

		menu = new JMenu("Menu");
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		menu.add(menuSettings);
		menu.add(menuAbout);

		menuAbout.addActionListener(this);
		menuSettings.addActionListener(this);

		GridLayout layoutSechs = new GridLayout(3, 3);
		layoutSechs.setHgap(4);
		layoutSechs.setVgap(4);

		GridLayout vier = new GridLayout(2, 2);
		vier.setHgap(4);
		vier.setVgap(4);

		JPanel panelFiles = new JPanel();
		TitledBorder borderFiles = new TitledBorder("Files");
		panelFiles.setBorder(borderFiles);
		panelFiles.setLayout(new BorderLayout());

		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(vier);

		JPanel panel_log = new JPanel();
		TitledBorder border_log = new TitledBorder("Log");
		panel_log.setBorder(border_log);
		panel_log.setLayout(new BorderLayout());

		JPanel panel_progress = new JPanel();
		panel_progress.setLayout(new BorderLayout());
		TitledBorder border_progress = new TitledBorder("Progress");
		panel_progress.setBorder(border_progress);

		JPanel panelSettings = new JPanel();
		TitledBorder borderSettings = new TitledBorder("Settings");
		panelSettings.setBorder(borderSettings);
		panelSettings.setLayout(layoutSechs);

		buttonSelect = new JButton("Select files");
		buttonSelect.addActionListener(this);

		buttonStart = new JButton("Start");
		buttonStart.addActionListener(this);
		buttonStart.setEnabled(false);

		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(this);
		buttonCancel.setEnabled(false);

		buttonSave = new JButton("Save in...");
		buttonSave.addActionListener(this);

		TextField field_probe = new TextField("cell with probe nr.");
		TextField field_labor = new TextField("cell with labor nr.");
		TextField field_sheet = new TextField("cell with sheet nr.");
		TextField field_main_start = new TextField(
				"cell with first value (upper left)");
		TextField field_main_end = new TextField(
				"cell with last value (lower right)");
		TextField field_subs = new TextField("column with names of substances");

		panelSettings.add(field_probe);
		panelSettings.add(field_labor);
		panelSettings.add(field_sheet);
		panelSettings.add(field_main_start);
		panelSettings.add(field_main_end);
		panelSettings.add(field_subs);

		areaFiles = new JTextArea();
		areaFiles.setEditable(false);
		JScrollPane scroll_files = new JScrollPane(areaFiles);

		areaLog = new JTextArea();
		areaLog.setEditable(false);
		JScrollPane scroll_log = new JScrollPane(areaLog);

		fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Excel file", "xls", "XLS");
		fc.addChoosableFileFilter(filter);

		fc2 = new JFileChooser();
		fc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		panelButtons.add(buttonSelect);
		panelButtons.add(buttonSave);
		panelButtons.add(buttonStart);
		panelButtons.add(buttonCancel);

		panelFiles.add(scroll_files);

		panel_log.add(scroll_log, BorderLayout.CENTER);

		bar = new JProgressBar(0, 100);
		bar.setValue(0);
		bar.setStringPainted(true);
		panel_progress.add(panelButtons, BorderLayout.WEST);
		panel_progress.add(bar, BorderLayout.CENTER);

		Dimension minimumSize = new Dimension(150, 50);
		panelFiles.setMinimumSize(minimumSize);
		panel_log.setMinimumSize(minimumSize);

		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.BEFORE_FIRST_LINE);
		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelFiles, panel_log),
				BorderLayout.CENTER);
		add(panel_progress, BorderLayout.AFTER_LAST_LINE);

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

	public void setReady() {

		menu.setEnabled(true);
		buttonCancel.setEnabled(false);
		buttonStart.setEnabled(true);
		buttonSelect.setEnabled(true);
		buttonSave.setEnabled(true);
		setCursor(null);
	}

	private void setWorking() {

		menu.setEnabled(true);
		buttonCancel.setEnabled(true);
		buttonStart.setEnabled(false);
		buttonSelect.setEnabled(false);
		buttonSave.setEnabled(false);

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		bar.setMaximum(dateien.size());
		pro = 0;
		bar.setValue(pro);
	}

	boolean ready() {
		if (selected == true && out == true)
			return true;
		else
			return false;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == buttonSelect) {
			int returnVal = fc.showOpenDialog(GUIMain.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				areaFiles.setText("");
				dateien.clear();
				File[] files = fc.getSelectedFiles();
				for (int i = 0; i < files.length; i++) {
					areaFiles.append(files[i].getName() + "\n");
					dateien.add(files[i]);
				}
				selected = true;
				if (ready()) {
					setReady();
				}

				areaLog.append("selected " + dateien.size() + " file(s)\n");
			}
		}

		else if (e.getSource() == menuAbout) {
			JOptionPane.showMessageDialog(this, GUIPrefs.NAME + " Version " + GUIPrefs.VERSION+"\n"
					+ "Markus Westphal: \tmarkus.c.westphal@tg.fh-giessen.de\n"
					+ "Alexander Kerner: \tphilip.a.kerner@tg.fh-giessen.de",
					"About", JOptionPane.INFORMATION_MESSAGE);
		}

		else if (e.getSource() == buttonSave) {

			int returnVal = fc2.showSaveDialog(GUIMain.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				outfile = fc2.getSelectedFile();
				areaLog.append("files will be written to " + outfile + "\n");

				out = true;
				if (ready())
					setReady();

			}

		}

		else if (e.getSource() == menuSettings) {

			frameSettings.setVisible(true);
		}

		else if (e.getSource() == buttonStart) {

			try {
				panelSettings.settingsValid();

				setWorking();

				Thread thread = new Thread(new Worker(this, this));

				thread.start();

			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this,
						"Invalid settings. Please check again", "Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (Throwable t) {
				throw new UnknownError();
			}

		}

	}

}
