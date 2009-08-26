package de.fh.giessen.ringversuch.model;

import hssf.utils.AbstractHSSFSheetWalker;
import hssf.utils.HSSFCellNeighbour;
import hssf.utils.HSSFCellNotBlankFilter;
import hssf.utils.HSSFCellRowComparator;
import hssf.utils.HSSFUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import de.fh.giessen.ringversuch.exception.FailedToDetectException;
import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class ValuesBeginCellDetector extends AbstractDetector {
	
	private static class ValuesBeginDetector {

		private final class Walker extends AbstractHSSFSheetWalker {
			Walker(HSSFSheet sheet) {
				super(sheet);
			}
			@Override
			public void handleCell(HSSFCell cell) {
				final Collection<HSSFCell> cells = HSSFUtils.getCellsBelowCell(
						sheet, cell);
				final List<HSSFCell> cellList = new ArrayList<HSSFCell>(cells);
				Collections.sort(cellList, new HSSFCellRowComparator());
				final List<HSSFCell> cellsWithValue = new ArrayList<HSSFCell>();
				for(HSSFCell c : cellList){
					if(c.getCellType() == HSSFCell.CELL_TYPE_BLANK){
						break;
					}
					cellsWithValue.add(c);
				}
				synchronized (mapToCellsBelow) {
					mapToCellsBelow.put(cell, cellsWithValue.size());
				}
				LOGGER.debug("cell " + cell.getRowIndex() + ","
						+ cell.getColumnIndex() + " has " + cellsWithValue.size()
						+ " cells below (numeric)");
			}
		}

		private final Map<HSSFCell, Integer> mapToCellsBelow = new HashMap<HSSFCell, Integer>();
		private final HSSFSheet sheet;

		ValuesBeginDetector(HSSFSheet sheet) {
			this.sheet = sheet;
		}

		public HSSFCell getValuesBeginCell() throws FailedToDetectException {
			int currentMax = 0;
			HSSFCell result = null;
			final Walker w = new Walker(sheet);
			w.addHSSFCellFilter(new HSSFCellNotBlankFilter());
				w.walk();
				synchronized (mapToCellsBelow) {
				for (Entry<HSSFCell, Integer> e : mapToCellsBelow.entrySet()) {
					final HSSFCell c = e.getKey();
					final int i = e.getValue();
					if (neighbourOk(c) && columnOk(result, c) && i > currentMax) {
						LOGGER.debug("new best candidate: " + c.getRowIndex()+","+c.getColumnIndex() + ": "+c);
						currentMax = i;
						result = c;
					}
				}
			}
			if (result == null)
				throw new FailedToDetectException("could not find suitable cell");
			LOGGER.info("found valuesBeginCell=" + result);
			return result;
		}

		private boolean columnOk(HSSFCell result, HSSFCell c) {
			if (result == null)
				return true;
			return (c.getColumnIndex() <= result.getColumnIndex());
		}

		private boolean neighbourOk(HSSFCell c) {
			final HSSFCellNeighbour n = new HSSFCellNeighbour(sheet, c);
			final HSSFCell ne = n.get(HSSFCellNeighbour.Orientation.EAST);
			final HSSFCell nse = n.get(HSSFCellNeighbour.Orientation.SOUTH_EAST);
			final HSSFCell ns = n.get(HSSFCellNeighbour.Orientation.SOUTH);
			return (ne != null && ne.getCellType() != HSSFCell.CELL_TYPE_BLANK
					&& nse != null && nse.getCellType() != HSSFCell.CELL_TYPE_BLANK
					&& ns != null && ns.getCellType() != HSSFCell.CELL_TYPE_BLANK);
		}
	}
	
	private final static Logger LOGGER = Logger.getLogger(ValuesBeginCellDetector.class);
	private final WorkMonitor monitor;

	ValuesBeginCellDetector(File[] files, ModelSettings settings, WorkMonitor monitor) {
		super(files, settings);
		this.monitor = monitor;
	}

	@Override
	public ModelSettings detect(ModelSettings settings, File[] files)
			throws Exception {
		int cellRow;
		int cellCol;
		for(File f : files){
			final HSSFCell cell = detectCell(f);
			cellRow = cell.getRowIndex();
			cellCol = cell.getColumnIndex();
			final String s = "assuming cell containing first value: " + cellRow + ","+ cellCol;
			LOGGER.info(s);
			settings.setValuesStartColumn(cellCol);
			settings.setValuesStartRow(cellRow);
			monitor.printMessage(s);
			// TODO considering only first file
			break;
		}
		return settings;
	}

	private HSSFCell detectCell(File file) throws FileNotFoundException, IOException, FailedToDetectException {
		LOGGER.debug("detecting cell containing first value");
		final HSSFWorkbook wb = HSSFUtils.getWorkbookFromFile(file);
		// TODO for now, we only look at sheet 0
		final HSSFSheet sheet = wb.getSheetAt(0);
		final ValuesBeginDetector walker = new ValuesBeginDetector(sheet);
		return walker.getValuesBeginCell();
	}

}
