package de.fh.giessen.ringversuch.model.settings;

import java.util.Properties;

public class ModelSettingsImpl implements ModelSettings {

	private int laborNoRow = -1;
	private int laborNoColumn = -1;
	private int probeNoRow = -1;
	private int probeNoCOlumn = -1;
	private String probeValue = null;
	private int sheetNo = -1;
	private int substancesColumn = -1;
	private int valuesStartRow = -1;
	private int valuesStartColumn = -1;
	private int valuesEndRow = -1;
	private int valuesEndColumn = -1;

	@Override
	public String toString() {
		return new StringBuilder().append("labIdentRow=").append(laborNoRow)
				.append(", ").append("labNoCol=").append(laborNoColumn).append(
						", ").append("probeIdent=").append(probeValue).append(
						", ").append("probeRow=").append(probeNoRow).append(
						", ").append("probeCol=").append(probeNoCOlumn).append(", ")
						.append("sheetNo=").append(sheetNo).append(", ")
						.append("subCol=").append(substancesColumn).append(", ")
						.append("valuesStartRow=").append(valuesStartRow).append(", ")
						.append("valuesStartCol=").append(valuesStartColumn).append(", ")
						.append("valuesEndRow=").append(valuesEndRow).append(", ")
						.append("valuesEndCol=").append(valuesEndColumn)
				.toString();
	}

	@Override
	public int getLaborIdentColumn() {
		final int r = laborNoColumn;
		return r;
	}

	@Override
	public int getLaborIdentRow() {
		final int r = laborNoRow;
		return r;
	}

	@Override
	public String getProbeIdent() {
		return probeValue;
	}

	@Override
	public int getProbeIdentColumn() {
		final int r = probeNoCOlumn;
		return r;
	}

	@Override
	public int getProbeIdentRow() {
		final int r = probeNoRow;
		return r;
	}

	@Override
	public int getSheetNo() {
		final int r = sheetNo;
		return r;
	}

	@Override
	public int getSubstancesColumn() {
		final int r = substancesColumn;
		return r;
	}

	@Override
	public int getValuesEndColumn() {
		final int r = valuesEndColumn;
		return r;
	}

	@Override
	public int getValuesEndRow() {
		final int r = valuesEndRow;
		return r;
	}

	@Override
	public int getValuesStartColumn() {
		final int r = valuesStartColumn;
		return r;
	}

	@Override
	public int getValuesStartRow() {
		final int r = valuesStartRow;
		return r;
	}

	@Override
	public void setLaborIdentColumn(int laborIdentColumn) {
		final int r = laborIdentColumn;
		this.laborNoColumn = r;
	}

	@Override
	public void setLaborIdentRow(int laborIdentRow) {
		final int r = laborIdentRow;
		this.laborNoRow = r;
	}

	@Override
	public void setProbeIdent(String probeIdent) {
		this.probeValue = probeIdent;
	}

	@Override
	public void setProbeIdentColumn(int probeIdentColumn) {
		final int r = probeIdentColumn;
		this.probeNoCOlumn = r;
	}

	@Override
	public void setProbeIdentRow(int probeIdentRow) {
		final int r = probeIdentRow;
		this.probeNoRow = r;
	}

	@Override
	public void setSheetNo(int sheetNo) {
		final int r = sheetNo;
		this.sheetNo = r;
	}

	@Override
	public void setSubstancesColumn(int substancesColumn) {
		final int r = substancesColumn;
		this.substancesColumn = r;
	}

	@Override
	public void setValuesEndColumn(int valuesEndColumn) {
		final int r = valuesEndColumn;
		this.valuesEndColumn = r;
	}

	@Override
	public void setValuesEndRow(int valuesEndRow) {
		final int r = valuesEndRow;
		this.valuesEndRow = r;
	}

	@Override
	public void setValuesStartColumn(int valuesStartColumn) {
		final int r = valuesStartColumn;
		this.valuesStartColumn = r;
	}

	@Override
	public void setValuesStartRow(int valuesStartRow) {
		final int r = valuesStartRow;
		this.valuesStartRow = r;
	}

}
