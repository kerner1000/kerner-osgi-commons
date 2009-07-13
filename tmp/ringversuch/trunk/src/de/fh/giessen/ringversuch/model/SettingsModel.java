package de.fh.giessen.ringversuch.model;

public interface SettingsModel {

	void setLaborIdentColumn(int laborIdentColumn);

	void setLaborIdentRow(int laborIdentRow);

	void setProbeIdent(String probeIdent);

	void setProbeIdentColumn(int probeIdentColumn);

	void setProbeIdentRow(int probeIdentRow);

	void setSubstancesColumn(int substancesColumn);

	void setSheetNo(int sheetNo);

	void setValuesEndRow(int valuesEndRow);

	void setValuesEndColumn(int valuesEndColumn);

	void setValuesStartColumn(int valuesStartColumn);

	void setValuesStartRow(int valuesStartRow);
	
	
	int getLaborIdentColumn();

	int getLaborIdentRow();

	String getProbeIdent();

	int getProbeIdentColumn();

	int getProbeIdentRow();

	int getSubstancesColumn();

	int getSheetNo();

	int getValuesEndRow();

	int getValuesEndColumn();

	int getValuesStartColumn();

	int getValuesStartRow();

}
