package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.PropertyConfigurator;

public class ModelImpl implements Model {

	private static String LOG_PROPERTIES = "/home/pcb/kerner/Dropbox/log.properties";

	public static void main(String args[]) {
		try {

			PropertyConfigurator.configure(LOG_PROPERTIES);

			SettingsManager settings = SettingsManagerImpl.INSTANCE;
			File file = new File(new File(System.getProperty("user.dir")),
					"settings.ini");
			settings.loadSettings(file);
			File file0 = new File(
					"/home/pcb/kerner/Desktop/Ergebnisse_RV_DIN38407-41/L01_RV_DIN38407-41.xls");
			File file1 = new File(
					"/home/pcb/kerner/Desktop/Ergebnisse_RV_DIN38407-41/L05_RV_DIN38407-41.xls");
			File outFile = new File("/home/pcb/kerner/Desktop/ringversuch2/");
			Collection<File> files = new ArrayList<File>();
			files.add(file0);
			files.add(file1);

			final Collection<Labor> labors = new ArrayList<Labor>();
			for (File f : files) {
				labors.add(Core.readLaborFile(f, settings));
			}

			Collection<OutSubstance> outSubstances = Core
					.getOutSubstancesFromLabors(labors, settings);
			
			for(OutSubstance s : outSubstances){
				final String fileName = Core.getFileNameForOutSubstance(s);
				Core.writeOutSubstance(s, new File(outFile, fileName + ".xls"));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidSettingsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
