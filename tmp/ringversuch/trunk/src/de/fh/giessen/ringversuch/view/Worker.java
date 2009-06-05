package de.fh.giessen.ringversuch.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Worker implements Callable<Boolean> {

	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}




private class Worker2 extends Thread {

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

}