package de.fh.giessen.ringversuch.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.controller.Controller;
import de.fh.giessen.ringversuch.model.InvalidSettingsException;

class ViewImplSettings {

	private final class MyPanel extends JPanel {

		private final class MyListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == buttonUse) {
					try {
						controller.setSettings(settings);
						setVisible(false);
					} catch (InvalidSettingsException e1) {
						LOGGER.error("settings invalid", e1);
						controller.showError(e1.getLocalizedMessage());
					}

				} else if (e.getSource() == buttonSave) {
					try {
						controller.setSettings(settings);
						controller.saveSettings();
						setVisible(false);
					} catch (InvalidSettingsException e1) {
						LOGGER.error("settings invalid", e1);
						controller.showError(e1.getLocalizedMessage());
					}

				} else if (e.getSource() == buttonLoad) {
					loadSettings();
				} else {
					LOGGER.error("unrecognized action performed: "
							+ e.getSource());
				}
			}
		}

		private static final long serialVersionUID = -1252068391141111780L;
		
		// TODO to GuiPrefs
		private final static String SHEET_NO_TITLE = "Sheet No";
		private final static String COLUMN_OF_SUBSTANCE_TITLE = "Column of Subst.";
		private final static String VALUES_BEGIN_TITLE = "Values begin";
		private final static String VALUES_END_TITLE = "Values end";
		private final JButton buttonLoad = new JButton("Load");
		private final JButton buttonSave = new JButton("Save");
		private final JButton buttonUse = new JButton("Use");
		
		private final ActionListener listener = new MyListener();
		private final JFileChooser fileChooser = new JFileChooser();
		private final FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Settings file", "ini", "INI");
		private final JTextField fieldSheetNo = new JTextField(controller.getSettings()
				.getProperty("SHEET_NO"));
		private final JTextField fieldColumnSubstance = new JTextField(controller.getSettings()
				.getProperty("SUBSTANCES_COLUMN"));
		private final JTextField fieldProbeNo = new JTextField(controller.getSettings()
				.getProperty("PROBE_USE"));
		private final JTextField fieldProbeRow = new JTextField(controller.getSettings()
				.getProperty("PROBE_NO_ROW"));
		private final JTextField fieldProbeColumn = new JTextField(controller.getSettings()
				.getProperty("PROBE_NO_COLUMN"));
		private final JTextField fieldLaborRow = new JTextField(controller.getSettings()
				.getProperty("LABOR_NO_ROW"));
		private final JTextField fieldLaborColumn = new JTextField(controller.getSettings()
				.getProperty("LABOR_NO_COLUMN"));
		private final JTextField fieldValuesBeginRow = new JTextField(controller.getSettings()
				.getProperty("MAIN_START_ROW"));
		private final JTextField fieldValuesBeginColumn = new JTextField(
				controller.getSettings().getProperty("MAIN_START_COLUMN"));
		private final JTextField fieldValuesEndRow = new JTextField(controller.getSettings()
				.getProperty("MAIN_END_ROW"));
		private final JTextField fieldValuesEndColumn = new JTextField(controller.getSettings()
				.getProperty("MAIN_END_COLUMN"));

		MyPanel() {
			initButtons();
			final JPanel panelSheetNo = initSheetNoPanel(getSheetNoPanelLayout());
			final JPanel panelColumnOfSubstance = initColumnOfSubstancePanel(getColumnOfSubstancePanelLayout());
			final JPanel panelProbeNo = initProbeNoPanel(getProbeNoPanelLayout());
			final JPanel panelValuesBegin = initValuesBeginPanel(getValuesBeginPanelLayout());
			final JPanel panelValuesEnd = initValuesEndPanel(getValuesEndPanelLayout());
			final JPanel panelLaborID = initLaborIDPanel(getLaborIDPanelLayout());
			initPanels(panelSheetNo, panelColumnOfSubstance, panelProbeNo,
					panelValuesBegin, panelValuesEnd, panelLaborID);

			fileChooser.setCurrentDirectory(new File(System
					.getProperty("user.dir")));
			fileChooser.addChoosableFileFilter(filter);
		}

		private void initPanels(JPanel panelSheetNo,
				JPanel panelColumnOfSubstance, JPanel panelProbeNo,
				JPanel panelValuesBegin, JPanel panelValuesEnd,
				JPanel panelLaborID) {
			final JPanel center = new JPanel(getTwoByTowGridLayout());
			center.add(panelProbeNo);
			center.add(panelLaborID);
			center.add(panelValuesBegin);
			center.add(panelValuesEnd);

			final JPanel north = new JPanel(getTwoColumnsGridLayout());
			north.add(panelSheetNo);
			north.add(panelColumnOfSubstance);

			final JPanel south = new JPanel(getThreeInARowGridLayout());
			south.add(buttonSave);
			south.add(buttonUse);
			south.add(buttonLoad);
			setLayout(new BorderLayout());
			add(center, BorderLayout.CENTER);
			add(north, BorderLayout.NORTH);
			add(south, BorderLayout.SOUTH);
		}

		private JPanel initLaborIDPanel(LayoutManager layout) {
			final JPanel p1 = new JPanel(layout);
			
			// TODO move to GuiPrefs
			p1.setBorder(new TitledBorder("Labor ID"));
			final JLabel row = new JLabel("Row");
			final JLabel column = new JLabel("Column");
			p1.add(row);
			p1.add(column);
			p1.add(fieldLaborRow);
			p1.add(fieldLaborColumn);
			return p1;
		}

		private JPanel initValuesEndPanel(LayoutManager layout) {
			final JPanel p1 = new JPanel(layout);
			p1.setBorder(new TitledBorder(VALUES_END_TITLE));
			
			//TODO move to GuiPrefs
			final JLabel row = new JLabel("Row");
			final JLabel column = new JLabel("Column");
			p1.add(row);
			p1.add(column);
			p1.add(fieldValuesEndRow);
			p1.add(fieldValuesEndColumn);
			return p1;

		}

		private JPanel initValuesBeginPanel(LayoutManager layout) {
			final JPanel p1 = new JPanel(layout);
			p1.setBorder(new TitledBorder(VALUES_BEGIN_TITLE));
			final JLabel row = new JLabel("Row");
			final JLabel column = new JLabel("Column");
			p1.add(row);
			p1.add(column);
			p1.add(fieldValuesBeginRow);
			p1.add(fieldValuesBeginColumn);
			return p1;
		}

		private JPanel initProbeNoPanel(LayoutManager layout) {
			final JPanel p1 = new JPanel(layout);
			p1.setBorder(new TitledBorder("Probe No"));
			final JLabel row = new JLabel("Row");
			final JLabel column = new JLabel("Column");
			final JLabel value = new JLabel("Value");
			p1.add(value);
			p1.add(fieldProbeNo);
			p1.add(row);
			p1.add(column);
			p1.add(fieldProbeRow);
			p1.add(fieldProbeColumn);
			return p1;
		}

		private JPanel initColumnOfSubstancePanel(LayoutManager layout) {
			final JPanel p1 = new JPanel(layout);
			p1.setBorder(new TitledBorder(COLUMN_OF_SUBSTANCE_TITLE));
			p1.add(fieldColumnSubstance);
			return p1;
		}

		private JPanel initSheetNoPanel(LayoutManager layout) {
			final JPanel p1 = new JPanel(layout);
			p1.setBorder(new TitledBorder(SHEET_NO_TITLE));
			p1.add(fieldSheetNo);
			return p1;
		}

		private LayoutManager getLaborIDPanelLayout() {
			return getThreeByThreeGridLayout();
		}

		private LayoutManager getValuesBeginPanelLayout() {
			return getThreeByThreeGridLayout();
		}

		private LayoutManager getValuesEndPanelLayout() {
			return getThreeByThreeGridLayout();
		}

		private LayoutManager getProbeNoPanelLayout() {
			return getThreeByThreeGridLayout();
		}

		private LayoutManager getColumnOfSubstancePanelLayout() {
			return getOneByOneGridLayout();
		}

		private LayoutManager getSheetNoPanelLayout() {
			return getOneByOneGridLayout();
		}

		private LayoutManager getOneByOneGridLayout() {
			return new GridLayout(1, 1);
		}

		private LayoutManager getThreeByThreeGridLayout() {
			final GridLayout sechs = new GridLayout(3, 3);
			sechs.setHgap(4);
			sechs.setVgap(0);
			return sechs;
		}

		private LayoutManager getTwoByTowGridLayout() {
			final GridLayout vier = new GridLayout(2, 2);
			vier.setHgap(4);
			vier.setVgap(0);
			return vier;
		}

		private LayoutManager getThreeInARowGridLayout() {
			final GridLayout drei = new GridLayout(0, 3);
			drei.setHgap(4);
			drei.setVgap(4);
			return drei;
		}

		private LayoutManager getTwoColumnsGridLayout() {
			final GridLayout zweih = new GridLayout(1, 2);
			zweih.setHgap(4);
			zweih.setVgap(4);
			return zweih;
		}

		private void initButtons() {
			buttonLoad.addActionListener(listener);
			buttonSave.addActionListener(listener);
			buttonUse.addActionListener(listener);
		}

		private void saveSettings() {
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				controller.saveSettings(fileChooser.getSelectedFile());
			}
		}

		private void loadSettings() {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				final File file = fileChooser.getSelectedFile();
				controller.loadSettings(file);
				assignSettings();
			}
		}

		private void assignSettings() {
			final Properties settings = controller.getSettings();
			
			// TODO move to Gneral Prog Prefs
			fieldLaborRow.setText(settings.getProperty("LABOR_NO_ROW"));
			fieldLaborColumn.setText(settings.getProperty("LABOR_NO_COLUMN"));
			fieldProbeRow.setText(settings.getProperty("PROBE_NO_ROW"));
			fieldProbeColumn.setText(settings.getProperty("PROBE_NO_COLUMN"));
			fieldProbeNo.setText(settings.getProperty("PROBE_USE"));
			fieldSheetNo.setText(settings.getProperty("SHEET_NO"));
			fieldColumnSubstance.setText(settings
					.getProperty("SUBSTANCES_COLUMN"));
			fieldValuesEndRow.setText(settings.getProperty("MAIN_END_ROW"));
			fieldValuesEndColumn.setText(settings
					.getProperty("MAIN_END_COLUMN"));
			fieldValuesBeginRow.setText(settings.getProperty("MAIN_START_ROW"));
			fieldValuesBeginColumn.setText(settings
					.getProperty("MAIN_START_COLUMN"));
		}
	}

	private final static Logger LOGGER = Logger
			.getLogger(ViewImplSettings.class);
	private final Controller controller;

	ViewImplSettings(Controller controller) {
		this.controller = controller;
		
		// TODO move to GuiPrefs
		final JFrame frame = new JFrame("Settings");

		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		final JPanel myPanel = new MyPanel();
		myPanel.setOpaque(true);
		frame.setContentPane(myPanel);
		frame.pack();
		frame.setResizable(false);
	}
}
