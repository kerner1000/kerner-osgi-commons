package interfaces;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import Exceptions.EmptySubstanceNameException;
import Exceptions.LaboratoryNotFoundException;
import Exceptions.ProbeNoNotFoundException;

public interface ReadWritePOI {
	/**
	 * Reads in a single xls-file
	 * @return numer of files that could not be processed
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws EmptySubstanceNameException 
	 */
	int readFile(File file) throws FileNotFoundException, IOException, ProbeNoNotFoundException, LaboratoryNotFoundException, EmptySubstanceNameException;
	
	/**
	 * Writes all output files in one process
	 * @param path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void writeFiles(File path) throws FileNotFoundException, IOException;
	
	/** 
	 * Sets row, that contains the laboratory information
	 * @param laboratoryRow
	 */
	void setLaboratoryRow(int laboratoryRow);
	
	/**
	 * Sets column, that contains the laboratory information
	 * @param laboratoryColumn
	 */
	void setLaboratoryColumn(int laboratoryColumn);
	
	/**
	 * Sets row, that contains the first value 
	 * @param valuesStartRow
	 */
	void setValuesStartRow(int valuesStartRow);
	
	/**
	 * Sets column, that contains the first value
	 * @param valuesStartColumn
	 */
	void setValuesStartColumn(int valuesStartColumn);
	
	/**
	 * Sets column, that contains the last value
	 * @param valuesEndColumn
	 */
	void setValuesEndColumn(int valuesEndColumn);
	
	/**
	 * Sets Row, that contains the last value
	 * @param valuesEndRow
	 */
	void setValuesEndRow(int valuesEndRow);
	
	/**
	 * Sets probe-No.
	 * @param probeNo
	 */
	void setProbeNo(int probeNo);
	
	/**
	 * Sets Row, that contains the probe-No.
	 * @param probeNoRow
	 */
	void setProbeNoRow(int probeNoRow);
	
	/**
	 * Sets Column, that contains the probe-No.
	 * @param probeNoColumn
	 */
	void setProbeNoColum(int probeNoColumn);
	
	/**
	 * Sets sheet No.
	 * @param sheetNo
	 */
	void setSheetNo(int sheetNo);
}
