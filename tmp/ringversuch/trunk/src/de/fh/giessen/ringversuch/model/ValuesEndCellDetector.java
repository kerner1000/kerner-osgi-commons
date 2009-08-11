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

import de.fh.giessen.ringversuch.model.settings.ModelSettings;

class ValuesEndCellDetector extends AbstractDetector {
	
	private class ValuesEndDetector {
		
		private final class Walker extends AbstractHSSFSheetWalker {
			Walker(HSSFSheet sheet) {
				super(sheet);
			}
			@Override
			public void handleCell(HSSFCell cell) {
				final Collection<HSSFCell> cells = HSSFUtils.getCellsAboveCell(
						sheet, cell);
				final List<HSSFCell> cellList = new ArrayList<HSSFCell>(cells);
//				Collections.sort(cellList, new HSSFCellRowComparator());
				Collections.sort(cellList, Collections.reverseOrder(new HSSFCellRowComparator()));
				final List<HSSFCell> cellsWithValue = new ArrayList<HSSFCell>();
				System.out.println(cells);
				System.out.println(cellList);
				System.out.println(cellsWithValue);
				for(HSSFCell c : cellList){
					if(c.getCellType() == HSSFCell.CELL_TYPE_BLANK){
						break;
					}
					cellsWithValue.add(c);
				}
				synchronized (mapToCellsAbove) {
					mapToCellsAbove.put(cell, cellsWithValue.size());
				}
				LOGGER.debug("cell " + cell.getRowIndex() + ","
						+ cell.getColumnIndex() + " has " + cellsWithValue.size()
						+ " cells above (numeric)");
			}
		}

		private final Map<HSSFCell, Integer> mapToCellsAbove = new HashMap<HSSFCell, Integer>();
		private final HSSFSheet sheet;

		ValuesEndDetector(HSSFSheet sheet) {
			this.sheet = sheet;
		}

		public HSSFCell getValuesEndCell() throws FailedToDetectException {
			int currentMax = 0;
			HSSFCell result = null;
			final Walker w = new Walker(sheet);
			w.addHSSFCellFilter(new HSSFCellNotBlankFilter());
				w.walk();
				synchronized (mapToCellsAbove) {
				for (Entry<HSSFCell, Integer> e : mapToCellsAbove.entrySet()) {
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
			LOGGER.info("found valuesEndCell=" + result);
			return result;
		}

		private boolean columnOk(HSSFCell result, HSSFCell c) {
			if (result == null)
				return true;
			return (c.getColumnIndex() >= result.getColumnIndex());
		}

		private boolean neighbourOk(HSSFCell c) {
			final HSSFCellNeighbour n = new HSSFCellNeighbour(sheet, c);
			final HSSFCell nn = n.get(HSSFCellNeighbour.Orientation.NORTH);
			final HSSFCell nnw = n.get(HSSFCellNeighbour.Orientation.NORH_WEST);
			final HSSFCell nw = n.get(HSSFCellNeighbour.Orientation.WEST);
			return (nn != null && nn.getCellType() != HSSFCell.CELL_TYPE_BLANK
					&& nnw != null && nnw.getCellType() != HSSFCell.CELL_TYPE_BLANK
					&& nw != null && nw.getCellType() != HSSFCell.CELL_TYPE_BLANK);
		}

	}
	
	private final static Logger LOGGER = Logger.getLogger(ValuesEndCellDetector.class);
	private final WorkMonitor monitor;

	ValuesEndCellDetector(File[] files, ModelSettings settings, WorkMonitor monitor) {
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
			final String s = "assuming cell containing last value: " + cellRow + ","+ cellCol;
			LOGGER.info(s);
			settings.setValuesEndColumn(cellCol);
			settings.setValuesEndRow(cellRow);
			monitor.printMessage(s);
			// TODO considering only first file
			break;
		}
		return settings;
	}

	private HSSFCell detectCell(File file) throws FileNotFoundException, IOException, FailedToDetectException {
		LOGGER.debug("detecting cell containing last value");
		final HSSFWorkbook wb = HSSFUtils.getWorkbookFromFile(file);
		// TODO for now, we only look at sheet 0
		final HSSFSheet sheet = wb.getSheetAt(0);
		final ValuesEndDetector walker = new ValuesEndDetector(sheet);
		return walker.getValuesEndCell();
	}

}
