package de.fh.giessen.ringversuch.model;

import hssf.utils.AbstractHSSFSheetWalker;
import hssf.utils.HSSFCellFilter;
import hssf.utils.HSSFCellNeighbour;
import hssf.utils.HSSFCellTypeNumericFilter;
import hssf.utils.HSSFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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
			final Collection<HSSFCellFilter> col = new ArrayList<HSSFCellFilter>();
			col.add(new HSSFCellTypeNumericFilter());
			final Collection<HSSFCell> cells = HSSFUtils.getCellsBelowCell(
					sheet, cell, col);
			synchronized (mapToCellsBelow) {
				mapToCellsBelow.put(cell, cells.size());
			}
			LOGGER.debug("cell " + cell.getRowIndex() + ","
					+ cell.getColumnIndex() + " has " + cells.size()
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
//		w.addHSSFCellFilter(new HSSFCellTypeNumericFilter());
			w.walk();
			synchronized (mapToCellsBelow) {
			for (Entry<HSSFCell, Integer> e : mapToCellsBelow.entrySet()) {
				final HSSFCell c = e.getKey();
				final int i = e.getValue();
				if (neighbourOk(c) && columnOk(result, c) && i > currentMax) {
					if(result != null)
					LOGGER.debug("new best candidate: old="+result.getRowIndex() + ","+result.getColumnIndex() + ",new="+c.getRowIndex()+","+c.getColumnIndex());
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
		return (c.getColumnIndex() >= result.getColumnIndex());
	}

	private boolean neighbourOk(HSSFCell c) {
		final HSSFCellNeighbour n = new HSSFCellNeighbour(sheet, c);
		final HSSFCell nc = n.get(HSSFCellNeighbour.Orientation.NORTH);
		if(nc == null)
			return true;
		return (nc.getCellType() != HSSFCell.CELL_TYPE_NUMERIC);
	}
}
