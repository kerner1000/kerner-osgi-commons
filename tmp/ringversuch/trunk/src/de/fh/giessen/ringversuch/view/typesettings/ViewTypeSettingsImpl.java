package de.fh.giessen.ringversuch.view.typesettings;

/**
 * Simple implementation for {@link ViewTypeSettings} 
 * @ThreadSave volatile fields
 * @lastVisit 2009-08-11
 * @author Alexander Kerner
 * 
 */
public class ViewTypeSettingsImpl implements ViewTypeSettings {
	
	private volatile String laborIdentColumn = "A";
	private volatile String laborIdentRow = "1";
	private volatile String probeIdent = "1";
	private volatile String probeIdentColumn = "A";
	private volatile String probeIdentRow = "1";
	private volatile String sheetNo = "1";
	private volatile String substancesColumn = "A";
	private volatile String valuesStartColumn = "A";
	private volatile String valuesEndColumn = "A";
	private volatile String valuesStartRow = "1";
	private volatile String valuesEndRow = "1";
	
	@Override
	public String toString(){
		return new StringBuilder().append("labIdentRow=").append(laborIdentRow)
		.append(", ").append("labNoCol=").append(laborIdentColumn).append(
				", ").append("probeIdent=").append(probeIdent).append(
				", ").append("probeRow=").append(probeIdentRow).append(
				", ").append("probeCol=").append(probeIdentColumn).append(", ")
				.append("sheetNo=").append(sheetNo).append(", ")
				.append("subCol=").append(substancesColumn).append(", ")
				.append("valuesStartRow=").append(valuesStartRow).append(", ")
				.append("valuesStartCol=").append(valuesStartColumn).append(", ")
				.append("valuesEndRow=").append(valuesEndRow).append(", ")
				.append("valuesEndCol=").append(valuesEndColumn)
		.toString();
	}

	@Override
	public void setLaborIdentColumn(String laborIdentColumn) {this.laborIdentColumn = laborIdentColumn;}

	@Override
	public void setLaborIdentRow(String laborIdentRow) {this.laborIdentRow = laborIdentRow;}

	@Override
	public void setProbeIdent(String probeIdent) {this.probeIdent = probeIdent;}

	@Override
	public void setProbeIdentColumn(String probeIdentColumn) {this.probeIdentColumn = probeIdentColumn;}

	@Override
	public void setProbeIdentRow(String probeIdentRow) {this.probeIdentRow = probeIdentRow;}

	@Override
	public void setSheetNo(String sheetNo) {this.sheetNo = sheetNo;}

	@Override
	public void setSubstancesColumn(String substancesColumn) {this.substancesColumn = substancesColumn;}

	@Override
	public void setValuesEndColumn(String valuesEndColumn) {this.valuesEndColumn = valuesEndColumn;}

	@Override
	public void setValuesEndRow(String valuesEndRow) {this.valuesEndRow = valuesEndRow;}

	@Override
	public void setValuesStartColumn(String valuesStartColumn) {this.valuesStartColumn = valuesStartColumn;}

	@Override
	public void setValuesStartRow(String valuesStartRow) {this.valuesStartRow = valuesStartRow;}

	@Override
	public String getLaborIdentColumn() {return laborIdentColumn;}

	@Override
	public String getLaborIdentRow() {return laborIdentRow;}

	@Override
	public String getProbeIdent() {return probeIdent;}

	@Override
	public String getProbeIdentColumn() {return probeIdentColumn;}

	@Override
	public String getProbeIdentRow() {return probeIdentRow;}

	@Override
	public String getSheetNo() {return sheetNo;}

	@Override
	public String getSubstancesColumn() {return substancesColumn;}

	@Override
	public String getValuesEndColumn() {return valuesEndColumn;}

	@Override
	public String getValuesEndRow() {return valuesEndRow;}

	@Override
	public String getValuesStartColumn() {return valuesStartColumn;}

	@Override
	public String getValuesStartRow() {return valuesStartRow;}

}
