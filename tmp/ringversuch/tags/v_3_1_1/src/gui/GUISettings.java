package gui;

import interfaces.RingversuchGUI;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class GUISettings extends JPanel implements ActionListener {

	private JButton button_load, buttonUse, buttonSave;

	private Properties settings;

	private JTextField field_labor1, field_labor2, field_probe1, field_probe2,
			field_sheet, field_main_start1, field_main_start2, field_main_end1,
			field_main_end2, field_subs, fieldProbeUse;

	private JFileChooser fc;

	private RingversuchGUI guiMain;

	private Frame frame;

	public GUISettings(Properties settings, RingversuchGUI guiMain, Frame frame) {

		this.guiMain = guiMain;
		this.settings = settings;
		this.frame = frame;
		init();
	}

	private void init() {

		fc = new JFileChooser();
		File pfad = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(pfad);

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Settings file", "ini", "INI");
		fc.addChoosableFileFilter(filter);

		JLabel row0 = new JLabel("Row");
		JLabel column0 = new JLabel("Column");
		JLabel row1 = new JLabel("Row");
		JLabel column1 = new JLabel("Column");
		JLabel row2 = new JLabel("Row");
		JLabel column2 = new JLabel("Column");
		JLabel row3 = new JLabel("Row");
		JLabel column3 = new JLabel("Column");
		JLabel hans = new JLabel("Value");
		// JLabel leer = new JLabel();

		GridLayout eins = new GridLayout(1, 1);

		GridLayout vier = new GridLayout(2, 2);
		vier.setHgap(4);
		vier.setVgap(0);

		GridLayout sechs = new GridLayout(3, 3);
		sechs.setHgap(4);
		sechs.setVgap(0);

		GridLayout zweih = new GridLayout(1, 2);
		zweih.setHgap(4);
		zweih.setVgap(4);

		GridLayout drei = new GridLayout(0, 3);
		drei.setHgap(4);
		drei.setVgap(4);

		/**
		 * Probennummer
		 */
		field_probe1 = new JTextField(settings.getProperty("PROBE_NO_ROW"));
		field_probe2 = new JTextField(settings.getProperty("PROBE_NO_COLUMN"));
		fieldProbeUse = new JTextField(settings.getProperty("PROBE_USE"));
		JPanel panel_field_probe = new JPanel(sechs);
		panel_field_probe.setBorder(new TitledBorder("Probe No"));
		panel_field_probe.add(hans);
		panel_field_probe.add(fieldProbeUse);
		panel_field_probe.add(row0);
		panel_field_probe.add(column0);
		panel_field_probe.add(field_probe1);
		panel_field_probe.add(field_probe2);

		/**
		 * Labornummer
		 */
		field_labor1 = new JTextField(settings.getProperty("LABOR_NO_ROW"));
		field_labor2 = new JTextField(settings.getProperty("LABOR_NO_COLUMN"));
		JPanel panel_field_labor = new JPanel(sechs);
		panel_field_labor.setBorder(new TitledBorder("Labor ID"));
		panel_field_labor.add(row1);
		panel_field_labor.add(column1);
		panel_field_labor.add(field_labor1);
		panel_field_labor.add(field_labor2);

		/**
		 * Sheetnummer
		 */
		field_sheet = new JTextField(settings.getProperty("SHEET_NO"));
		JPanel panel_field_sheet = new JPanel(eins);
		panel_field_sheet.setBorder(new TitledBorder("Sheet No"));
		panel_field_sheet.add(field_sheet);

		/**
		 * Substanznummer
		 */
		field_subs = new JTextField(settings.getProperty("SUBSTANCES_COLUMN"));
		JPanel panel_field_subs = new JPanel(eins);
		panel_field_subs.setBorder(new TitledBorder("Column of Subst."));
		// panel_field_subs.add(row4);
		panel_field_subs.add(field_subs);

		/**
		 * Hauptbereich Start
		 */
		field_main_start1 = new JTextField(settings
				.getProperty("MAIN_START_ROW"));
		field_main_start2 = new JTextField(settings
				.getProperty("MAIN_START_COLUMN"));
		JPanel panel_main_start = new JPanel(sechs);
		panel_main_start.setBorder(new TitledBorder("Values begin"));
		panel_main_start.add(row2);
		panel_main_start.add(column2);
		panel_main_start.add(field_main_start1);
		panel_main_start.add(field_main_start2);

		/**
		 * Hauptbereich Ende
		 */
		field_main_end1 = new JTextField(settings.getProperty("MAIN_END_ROW"));
		field_main_end2 = new JTextField(settings
				.getProperty("MAIN_END_COLUMN"));
		JPanel panel_main_end = new JPanel(sechs);
		panel_main_end.setBorder(new TitledBorder("Values end"));
		panel_main_end.add(row3);
		panel_main_end.add(column3);
		panel_main_end.add(field_main_end1);
		panel_main_end.add(field_main_end2);

		button_load = new JButton("Load");
		button_load.addActionListener(this);
		buttonSave = new JButton("Save");
		buttonSave.addActionListener(this);
		buttonUse = new JButton("Use");
		buttonUse.addActionListener(this);

		JPanel panel_oben = new JPanel(vier);
		JPanel panel_mitte = new JPanel(zweih);
		JPanel panel_unten = new JPanel(drei);

		panel_oben.add(panel_field_probe);
		panel_oben.add(panel_field_labor);
		panel_oben.add(panel_main_start);
		panel_oben.add(panel_main_end);

		panel_mitte.add(panel_field_sheet);
		panel_mitte.add(panel_field_subs);

		panel_unten.add(buttonSave);
		panel_unten.add(buttonUse);
		panel_unten.add(button_load);

		setLayout(new BorderLayout());

		add(panel_oben, BorderLayout.CENTER);
		add(panel_mitte, BorderLayout.NORTH);
		add(panel_unten, BorderLayout.SOUTH);

	}

	boolean settingsValid() throws NumberFormatException {

		if (intValid(field_labor1.getText()) == true
				|| valid_char(convert(field_labor2.getText())) == true
				|| intValid(field_probe1.getText()) == true
				|| valid_char(convert(field_probe2.getText())) == true
				|| intValid(field_sheet.getText()) == true
				|| valid_char(convert(field_subs.getText())) == true
				|| intValid(field_main_end1.getText()) == true
				|| valid_char(convert(field_main_end2.getText())) == true
				|| intValid(field_main_start1.getText()) == true
				|| valid_char(convert(field_main_start2.getText())) == true
				|| intValid(fieldProbeUse.getText()) == true)

			return true;

		else
			return false;

	}

	private boolean valid_char(int i) {
		boolean b = false;

		if (i > 0 && i < 27)
			b = true;
		else
			guiMain.appendLog("Use charakters between A and Z \n" + i, true);

		return b;
	}

	int convert(String text) throws NumberFormatException {
		int i = -1;
		char c = 'a';
		if (text.length() > 1) {
			// custom title, warning icon
			JOptionPane.showMessageDialog(this,
					"There may be problems with your settings", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
		c = text.toUpperCase().charAt(0);
		i = (int) c;
		i = i - 64;
		return i;
	}

	private boolean intValid(String text) throws NumberFormatException {
		boolean b = false;
		int i = Integer.parseInt(text);
		if (i > 0)
			b = true;
		else
			guiMain.appendLog("Use values above zero \n", true);

		return b;
	}

	private void setSettings() {
		settings.setProperty("LABOR_NO_ROW", field_labor1.getText());
		settings.setProperty("LABOR_NO_COLUMN", field_labor2.getText());
		settings.setProperty("PROBE_NO_ROW", field_probe1.getText());
		settings.setProperty("PROBE_NO_COLUMN", field_probe2.getText());
		settings.setProperty("PROBE_USE", fieldProbeUse.getText());
		settings.setProperty("SHEET_NO", field_sheet.getText());
		settings.setProperty("SUBSTANCES_COLUMN", field_subs.getText());
		settings.setProperty("MAIN_START_ROW", field_main_start1.getText());
		settings.setProperty("MAIN_START_COLUMN", field_main_start2.getText());
		settings.setProperty("MAIN_END_ROW", field_main_end1.getText());
		settings.setProperty("MAIN_END_COLUMN", field_main_end2.getText());
	}

	private void saveSettings() {

		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String name = fc.getSelectedFile().getAbsolutePath();

			try {

				if (name.contains(".")) {
					name = name.substring(0, name.lastIndexOf('.'));
				}
				name = name + ".ini";
				FileOutputStream out = new FileOutputStream(name);
				settings.store(out,
						"This is a settings file for \"Ringversuch.vx.x.jar\"");
				out.close();
				guiMain.appendLog("Settings successfully written to " + name
						+ "\n", false);

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException ex) {
				guiMain.appendLog("Could not write settings to " + name + "\n",
						true);
			}
		}
	}

	private void loadSettings() {

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				FileInputStream in = new FileInputStream(file);
				settings.load(in);
				in.close();
				guiMain.appendLog("Settings successfully loaded from "
						+ file.getAbsolutePath() + "\n", false);
			} catch (FileNotFoundException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} catch (IOException ex) {
				guiMain.appendLog(
						"Could not read settings from " + file + "\n", true);
				ex.printStackTrace();
			}

			field_labor1.setText(settings.getProperty("LABOR_NO_ROW"));
			field_labor2.setText(settings.getProperty("LABOR_NO_COLUMN"));
			field_probe1.setText(settings.getProperty("PROBE_NO_ROW"));
			field_probe2.setText(settings.getProperty("PROBE_NO_COLUMN"));
			fieldProbeUse.setText(settings.getProperty("PROBE_USE"));
			field_sheet.setText(settings.getProperty("SHEET_NO"));
			field_subs.setText(settings.getProperty("SUBSTANCES_COLUMN"));
			field_main_end1.setText(settings.getProperty("MAIN_END_ROW"));
			field_main_end2.setText(settings.getProperty("MAIN_END_COLUMN"));
			field_main_start1.setText(settings.getProperty("MAIN_START_ROW"));
			field_main_start2
					.setText(settings.getProperty("MAIN_START_COLUMN"));

		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == buttonUse) {

			try {
				if (settingsValid()) {
					setSettings();
					// guiMain.appendLog("Settings sucessfully loaded\n",
					// false);
					frame.setVisible(false);
				} else {
					System.err.println("Hier sollten wir nicht landen...");
				}
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this,
						"Invalid settings. Please check again", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		} else if (e.getSource() == buttonSave) {
			try {
				if (settingsValid()) {
					setSettings();
					saveSettings();
					// frame.setVisible(false);
				} else {
					System.err.println("Hier sollten wir nicht landen...");
				}
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this,
						"Invalid settings. Please check again", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		} else if (e.getSource() == button_load) {

			loadSettings();
		}
	}
}
