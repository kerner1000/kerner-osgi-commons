package de.fh.giessen.ringversuch.view2;

import java.awt.BorderLayout;
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

import org.apache.log4j.Logger;

import de.fh.giessen.ringversuch.common.Preferences;

public class MainContentImpl implements MainContent {
	
	private final class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonSelect) {
				final int returnVal = fileChooserinputFiles.showOpenDialog(mainView.getContainer());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					areaFiles.setText("");
					files.clear();
					final File[] inputFiles = fileChooserinputFiles.getSelectedFiles();
					if(mainView.setSelectedFiles(inputFiles)){
						for (File f : inputFiles) {
							areaFiles.append(f.getName() + Preferences.NEW_LINE);
							files.add(f);
						}
						
						// TODO that should do the model / controller
						inputFilesSelected = true;
						if (inputFilesSelectedOutputDirSelected()) {
							setInputFilesSelectedOutputDirSelected();
						}
					}
				}
			}

			else if (e.getSource() == buttonSave) {
				final int returnVal = fileChooseroutDir.showSaveDialog(mainView.getContainer());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					mainView.setOutDir(fileChooseroutDir.getSelectedFile());
					
					// TODO that should do the model / controller
					outputDirSelected = true;
					if (inputFilesSelectedOutputDirSelected())
						setInputFilesSelectedOutputDirSelected();
				}
			}

			else if (e.getSource() == menuSettings) {
				mainView.getSwingViewManager().switchView(ViewState.SETTINGS_ACTIVE);
			}
			
			else if (e.getSource() == menuAbout) {
				showAbout();
			}
			
			else if (e.getSource() == buttonCancel) {
				mainView.cancel();
			}

			else if (e.getSource() == buttonStart) {
				mainView.start();
			}
			
			else {
				LOGGER.error("unrecognized action performed: "
						+ e.getSource());
			}
		}
		
		private void showAbout() {
			// take shortcut and directly show about, without notifying controller or model
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(mainView.getContainer(), Preferences.NAME + " Version " + Preferences.VERSION+"\n"
							+ "Markus Westphal: \tmarkus.c.westphal@tg.fh-giessen.de\n"
							+ "Alexander Kerner: \tphilip.a.kerner@tg.fh-giessen.de",
							"About", JOptionPane.INFORMATION_MESSAGE);	
				}
			});	
		}
	}
	
	private static final long serialVersionUID = 5815887454332977224L;
	private final Collection<File> files = new Vector<File>();
	private final JTextArea areaFiles = new JTextArea();
	private final JFileChooser fileChooserinputFiles  = new JFileChooser();
	private final JFileChooser 		fileChooseroutDir = new JFileChooser();
	private final JButton buttonStart = new JButton(Preferences.View.BUTTON_START);
	private final JMenu menu = new JMenu(Preferences.View.MENU_TITLE);
	private final JMenuItem menuAbout = new JMenuItem(Preferences.View.MENU_ABOUT);
	private final JMenuItem menuSettings = new JMenuItem(Preferences.View.MENU_SETTINGS);
	private final JProgressBar progressBar = new JProgressBar(0, 100);
	private final JButton buttonSelect = new JButton(Preferences.View.BUTTON_SELECT);
	private final JButton buttonSave = new JButton(Preferences.View.BUTTON_SAVE);
	private final JButton buttonCancel = new JButton(Preferences.View.BUTTON_CANCEL);
	private final JTextArea areaLog = new JTextArea();
	private boolean inputFilesSelected = false;
	private boolean outputDirSelected = false;
	private int progress = 0;
	final static Logger LOGGER = Logger.getLogger(MainContentImpl.class);
	private final ActionListener myListener = new MyListener();

	private final MainView mainView;

	public MainContentImpl(MainView mainView) {
		this.mainView = mainView;
		init();
	}

	private void init() {
		initButtons(myListener);
		initInputFilesChooser();
		initOutputDirFileChooser();
		mainView.getContainer().setLayout(new BorderLayout());
		mainView.getContainer().add(initMenubar(myListener), BorderLayout.BEFORE_FIRST_LINE);
		mainView.getContainer().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, initFilesPanel(),
				initLogPanel()), BorderLayout.CENTER);
		mainView.getContainer().add(initProgressAndButtonsPanel(), BorderLayout.AFTER_LAST_LINE);
	}
	
	private void initButtons(ActionListener listener) {
		buttonSelect.addActionListener(listener);
		buttonStart.addActionListener(listener);
		buttonCancel.addActionListener(listener);
		buttonSave.addActionListener(listener);
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
		final TitledBorder b1 = new TitledBorder(Preferences.View.PROGRESS_AND_BUTTONS_TITLE);
		p1.setBorder(b1);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		p1.add(initButtonsPanel(), BorderLayout.WEST);
		p1.add(progressBar, BorderLayout.CENTER);
		return p1;
	}

	private JPanel initLogPanel() {
		areaLog.setEditable(false);
		final JScrollPane s = new JScrollPane(areaLog);
		final JPanel p1 = new JPanel();
		final TitledBorder b1 = new TitledBorder(Preferences.View.LOG_TITLE);
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
		final TitledBorder b1 = new TitledBorder(Preferences.View.FILES_TITLE);
		p1.setBorder(b1);
		p1.setLayout(new BorderLayout());
		final Dimension minimumSize = new Dimension(150, 50);
		p1.setMinimumSize(minimumSize);
		p1.add(s);
		return p1;
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

	/**
	private LayoutManager getThreeByThreeGridLayout() {
		final GridLayout sechs = new GridLayout(3, 3);
		sechs.setHgap(4);
		sechs.setVgap(4);
		return sechs;
	}
	*/

	private LayoutManager getTwoByTowGridLayout() {
		final GridLayout vier = new GridLayout(2, 2);
		vier.setHgap(4);
		vier.setVgap(4);
		return vier;
	}

	@Override
	public
	void printMessage(String message, boolean isError){
		areaLog.append(message + Preferences.NEW_LINE);
	}
	
	@Override
	public void setOnline() {
		menu.setEnabled(true);
		buttonCancel.setEnabled(false);
		buttonStart.setEnabled(false);
		buttonSelect.setEnabled(true);
		buttonSave.setEnabled(true);
		mainView.getContainer().setCursor(null);
		progressBar.setValue(0);
		progressBar.setMaximum(100);
	}
	
	@Override
	public void setReady() {
		menu.setEnabled(true);
		buttonCancel.setEnabled(false);
		buttonStart.setEnabled(true);
		buttonSelect.setEnabled(true);
		buttonSave.setEnabled(true);
		mainView.getContainer().setCursor(null);
		progressBar.setValue(0);
		progressBar.setMaximum(100);
	}
	
	@Override
	public void setWorking() {
		menu.setEnabled(true);
		buttonCancel.setEnabled(true);
		buttonStart.setEnabled(false);
		buttonSelect.setEnabled(false);
		buttonSave.setEnabled(false);
		mainView.getContainer().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		progressBar.setMaximum(files.size());
		progress = 0;
		progressBar.setValue(progress);
	}

	@Override
	public void setProgress(int current, int max) {
		progressBar.setMaximum(max);
		progressBar.setValue(current);
	}
	
	boolean inputFilesSelectedOutputDirSelected() {
		if (inputFilesSelected == true && outputDirSelected == true)
			return true;
		else
			return false;
	}
	
	void setInputFilesSelectedOutputDirSelected() {
		menu.setEnabled(true);
		buttonCancel.setEnabled(false);
		buttonStart.setEnabled(true);
		buttonSelect.setEnabled(true);
		buttonSave.setEnabled(true);
		mainView.getContainer().setCursor(null);
	}

	@Override
	public void showError(final String message) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LOGGER.debug("show error=" + message);
				JOptionPane.showMessageDialog(mainView.getContainer(), message,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

}
