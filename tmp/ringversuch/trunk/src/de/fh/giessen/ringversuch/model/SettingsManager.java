package de.fh.giessen.ringversuch.model;

import java.io.File;
import java.io.IOException;

public interface SettingsManager {

	int getProbeRowIndex();

	int getProbeCellNum();

	void loadSettings(File file) throws IOException, InvalidSettingsException;

	int getLaborRowIndex();

	int getLaborCellNum();

	int getValuesStartRow();

	int getValuesStartColumn();

	int getValuesEndRow();

	int getValuesEndColumn();

}
