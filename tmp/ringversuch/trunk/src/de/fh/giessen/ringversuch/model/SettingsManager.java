package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.IOException;

public interface SettingsManager {

	int getProbeRowIndex();

	int getProbeColumnIndex();

	void loadSettings(File file) throws IOException, InvalidSettingsException;

	int getLaborRowIndex();

	int getLaborColumnIndex();

	int getValuesStartRowIndex();

	int getValuesStartColumnIndex();

	int getValuesEndRowIndex();

	int getValuesEndColumnIndex();

	int getSubstancesColumnIndex();

	String getProbeIdent();

}
