package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ModelImpl implements Model {

	public static void main(String args[]) {
		try {
			SettingsManager settings = SettingsManagerImpl.INSTANCE;
			File file = new File(new File(System.getProperty("user.dir")), "settings.ini");
			settings.loadSettings(file);
			File file0 = new File(
					"D:\\Ergebnisse_RV_DIN38407-41\\L01_RV_DIN38407-41.xls");
			File file1 = new File(
					"D:\\Ergebnisse_RV_DIN38407-41\\L05_RV_DIN38407-41.xls");
			Collection<File> files = new ArrayList<File>();
			files.add(file0);
			files.add(file1);

			for (File f : files) {

				Core.readLaborFile(f, settings);

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
		}

	}

}
