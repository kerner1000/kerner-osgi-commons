package de.fh.giessen.ringversuch.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;
import de.fh.giessen.ringversuch.view.settings.ViewSettingsImpl;
import de.fh.giessen.ringversuch.view.settings.ViewSettings;

class ViewImplSettings extends JPanel {

	private final class MyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonUse) {
				if(controller.setSettingsOut(getSettings()))
				controller.hideSettingsView();

			} else if (e.getSource() == buttonSave) {
				if(controller.saveSettingsOut(getSettings()))controller.hideSettingsView();

			} else if (e.getSource() == buttonLoad) {
				final int returnVal = fileChooser.showOpenDialog(component);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if(controller.loadSettings(file))
						controller.hideSettingsView();
				}
			} else {
				LOGGER.error("unrecognized action performed: " + e.getSource());
			}
		}
	}

	private static final long serialVersionUID = -1252068391141111780L;
	private final static Logger LOGGER = Logger
			.getLogger(ViewImplSettings.class);
	private final ViewController controller;

	// TODO to Prefs
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
	private final JTextField fieldSheetNo;
	private final JTextField fieldColumnSubstance;
	private final JTextField fieldProbeNo;
	private final JTextField fieldProbeRow;
	private final JTextField fieldProbeColumn;
	private final JTextField fieldLaborRow;
	private final JTextField fieldLaborColumn;
	private final JTextField fieldValuesBeginRow;
	private final JTextField fieldValuesBeginColumn;
	private final JTextField fieldValuesEndRow;
	private final JTextField fieldValuesEndColumn;
	
	private final Component component = this;

	ViewImplSettings(ViewController controller) {
		this.controller = controller;

		// TODO identifier
		fieldColumnSubstance = new JTextField();
		fieldSheetNo = new JTextField();
		fieldProbeNo = new JTextField();
		fieldProbeRow = new JTextField();
		fieldProbeColumn = new JTextField();
		fieldLaborRow = new JTextField();
		fieldLaborColumn = new JTextField();
		fieldValuesBeginRow = new JTextField();
		fieldValuesBeginColumn = new JTextField();
		fieldValuesEndRow = new JTextField();
		fieldValuesEndColumn = new JTextField();

		initButtons();
		final JPanel panelSheetNo = initSheetNoPanel(getSheetNoPanelLayout());
		final JPanel panelColumnOfSubstance = initColumnOfSubstancePanel(getColumnOfSubstancePanelLayout());
		final JPanel panelProbeNo = initProbeNoPanel(getProbeNoPanelLayout());
		final JPanel panelValuesBegin = initValuesBeginPanel(getValuesBeginPanelLayout());
		final JPanel panelValuesEnd = initValuesEndPanel(getValuesEndPanelLayout());
		final JPanel panelLaborID = initLaborIDPanel(getLaborIDPanelLayout());
		initPanels(panelSheetNo, panelColumnOfSubstance, panelProbeNo,
				panelValuesBegin, panelValuesEnd, panelLaborID);
		fileChooser
				.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.addChoosableFileFilter(filter);
	}

	private void initPanels(JPanel panelSheetNo, JPanel panelColumnOfSubstance,
			JPanel panelProbeNo, JPanel panelValuesBegin,
			JPanel panelValuesEnd, JPanel panelLaborID) {
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

		// TODO move to GuiPrefs
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

	void setSettings(ViewSettings settings) {
		fieldLaborRow.setText(settings.getLaborIdentRow());
		fieldLaborColumn.setText(settings.getLaborIdentColumn());
		fieldProbeRow.setText(settings.getProbeIdentRow());
		fieldProbeColumn.setText(settings.getProbeIdentColumn());
		fieldProbeNo.setText(settings.getProbeIdent());
		fieldSheetNo.setText(settings.getSheetNo());
		fieldColumnSubstance.setText(settings.getSubstancesColumn());
		fieldValuesEndRow.setText(settings.getValuesEndRow());
		fieldValuesEndColumn.setText(settings.getValuesEndColumn());
		fieldValuesBeginRow.setText(settings.getValuesStartRow());
		fieldValuesBeginColumn.setText(settings.getValuesStartColumn());
	}

	ViewSettings getSettings() {
		final ViewSettings settings = new ViewSettingsImpl();
		settings.setLaborIdentColumn(fieldLaborColumn.getText());
		settings.setLaborIdentRow(fieldLaborRow.getText());
		settings.setProbeIdent(fieldProbeNo.getText());
		settings.setProbeIdentColumn(fieldProbeColumn.getText());
		settings.setProbeIdentRow(fieldProbeRow.getText());
		settings.setSheetNo(fieldSheetNo.getText());
		settings.setSubstancesColumn(fieldColumnSubstance.getText());
		settings.setValuesEndColumn(fieldValuesEndColumn.getText());
		settings.setValuesEndRow(fieldValuesEndRow.getText());
		settings.setValuesStartColumn(fieldValuesBeginColumn.getText());
		settings.setValuesStartRow(fieldValuesBeginRow.getText());
		return settings;
	}
}
