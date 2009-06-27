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
	private final static Logger LOGGER = Logger.getLogger(ViewImpl.class);
	
	private final class MyPanel extends JPanel {
		
		private final class MyListener implements ActionListener {
			private final Component component;
			private final Controller controller;

			MyListener(Component component, Controller controller) {
				this.component = component;
				this.controller = controller;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				LOGGER.debug("event received: " + e);
				if (e.getSource() == buttonSelect) {
					final int returnVal = fileChooserinputFiles.showOpenDialog(component);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						areaFiles.setText("");
						files.clear();
						final File[] inputFiles = fileChooserinputFiles.getSelectedFiles();
						
						// TODO that should do the controller
						for (File f : inputFiles) {
							areaFiles.append(f.getName() + GUIPrefs.NEW_LINE);
							files.add(f);
						}
						
						// TODO that should do the controller
						inputFilesSelected = true;
						if (inputFilesSelectedOutputDirSelected()) {
							setInputFilesSelectedOutputDirSelected();
						}
						controller.setSelectedFiles(inputFiles);
					}
				}

				else if (e.getSource() == buttonSave) {
					final int returnVal = fileChooseroutDir.showSaveDialog(component);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						System.err.println(fileChooseroutDir.getSelectedFile());
						controller.setOutDir(fileChooseroutDir.getSelectedFile());
						
						// TODO that should do the model / controller
						outputDirSelected = true;
						if (inputFilesSelectedOutputDirSelected())
							setInputFilesSelectedOutputDirSelected();
					}
				}

				else if (e.getSource() == menuSettings) {
					showSettingsView();
				}
				
				else if (e.getSource() == menuAbout) {
					showAbout();
				}

				else if (e.getSource() == buttonStart) {
					controller.start();
					
					
					// TODO take a look at that...
//					if(controller.settingsValid()){
//						setWorking();
//						exe.submit(new Worker());
//					} else {
//						controller.showError("Invalid settings");
//					}
				}
			}

			private void showAbout() {
				// take shortcut and directly show about, without notifying controller or model
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(component, GUIPrefs.NAME + " Version " + GUIPrefs.VERSION+"\n"
								+ "Markus Westphal: \tmarkus.c.westphal@tg.fh-giessen.de\n"
								+ "Alexander Kerner: \tphilip.a.kerner@tg.fh-giessen.de",
								"About", JOptionPane.INFORMATION_MESSAGE);	
					}
				});	
			}
		}

		private static final long serialVersionUID = 5815887454332977224L;
		private final ActionListener myListener;
		private final Collection<File> files = new Vector<File>();
		private final JTextArea areaFiles = new JTextArea();
		private final JFileChooser fileChooserinputFiles  = new JFileChooser();
		private final JFileChooser 		fileChooseroutDir = new JFileChooser();
		private final JButton buttonStart = new JButton(BUTTON_START);
		private final JMenu menu = new JMenu(MENU_TITLE);
		private final JMenuItem menuAbout = new JMenuItem(MENU_ABOUT);
		private final JMenuItem menuSettings = new JMenuItem(MENU_SETTINGS);
		private final JProgressBar progressBar = new JProgressBar(0, 100);
		private final JButton buttonSelect = new JButton(BUTTON_SELECT);
		private final JButton buttonSave = new JButton(BUTTON_SAVE);
		private final JButton buttonCancel = new JButton(BUTTON_CANCEL);
		private final JTextArea areaLog = new JTextArea();
		private boolean inputFilesSelected = false;
		private boolean outputDirSelected = false;
		private int progress = 0;
		
		MyPanel(Controller controller) {
			this.myListener = new MyListener(this, controller);
			init();
		}
		
		void printMessage(String message, boolean isError){
			areaLog.append(message + GUIPrefs.NEW_LINE);
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
			final TitledBorder b1 = new TitledBorder(PROGRESS_AND_BUTTONS_TITLE);
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
		
		private void showSettingsView() {
			// TODO Auto-generated method stub
			
		}

	}

	private final MyPanel panel;
	public ViewImpl(final Controller  controller) {
		// Create and set up the content pane.
		// TODO must it be initialized in EventThread?
		panel = new MyPanel(controller);
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
