package de.fh.giessen.ringversuch.model;

import hssf.utils.AbstractHSSFSheetWalker;
import hssf.utils.HSSFCellNeighbour;
import hssf.utils.HSSFCellNotBlankFilter;
import hssf.utils.HSSFCellRowComparator;
import hssf.utils.HSSFUtils;

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

/**
 * 
 * @ThreadSave
 * @author Alexander Kerner
 *
 */
class ValuesBeginDetector {

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

	private final static Logger LOGGER = Logger
			.getLogger(ValuesBeginDetector.class);
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
