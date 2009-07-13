package de.fh.giessen.ringversuch.view;

public interface SettingsView {

	void setLaborIdentColumn(String laborIdentColumn);

	void setLaborIdentRow(String laborIdentRow);

	void setProbeIdent(String probeIdent);

	void setProbeIdentColumn(String probeIdentColumn);

	void setProbeIdentRow(String probeIdentRow);

	void setSubstancesColumn(String substancesColumn);

	void setSheetNo(String sheetNo);

	void setValuesEndRow(String valuesEndRow);

	void setValuesEndColumn(String valuesEndColumn);

	void setValuesStartColumn(String valuesStartColumn);

	void setValuesStartRow(String valuesStartRow);
	
	
	String getLaborIdentColumn();

	String getLaborIdentRow();

	String getProbeIdent();

	String getProbeIdentColumn();

	String getProbeIdentRow();

	String getSubstancesColumn();

	String getSheetNo();

	String getValuesEndRow();

	String getValuesEndColumn();

	String getValuesStartColumn();

	String getValuesStartRow();

}
