package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ModelImpl implements Model {

	public static void main(String args[]){
		
		SettingsManager settings = SettingsManager.INSTANCE;
		File file0 = new File("C:\\Dokumente und Einstellungen\\Renate\\Desktop\\Ergebnisse_RV_DIN38407-41\\L01_RV_DIN38407-41.xls");
		File file1 = new File("C:\\Dokumente und Einstellungen\\Renate\\Desktop\\Ergebnisse_RV_DIN38407-41\\L05_RV_DIN38407-41.xls");
		Collection<File> files = new ArrayList<File>();
		files.add(file0);
		files.add(file1);
		
		for(File f : files){
			try {
				Core.readLaborFile(f, settings.getSettings());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
