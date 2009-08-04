package hssf.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class HSSFUtils {

	private HSSFUtils() {
	}

	public static Collection<HSSFCell> getCellsAboveCell(HSSFSheet sheet,
			HSSFCell cell, final HSSFCellFilter filter) {
		return getCellsAboveCell(sheet, cell, new ArrayList<HSSFCellFilter>() {
			{
				add(filter);
			}
		});
	}
	
	public static Collection<HSSFCell> getCellsAboveCell(HSSFSheet sheet,
			HSSFCell cell) {
		final Collection<HSSFCell> result = new ArrayList<HSSFCell>();
		final AbstractHSSFSheetWalker walker = new AbstractHSSFSheetWalker(sheet) {
			@Override
			public void handleCell(HSSFCell cell) {
				result.add(cell);
			}
		};
		final int rowIndex = cell.getRowIndex();
		final int colIndex = cell.getColumnIndex();
		walker.addHSSFCellFilter(new HSSFCellColumnFilter(colIndex,
				HSSFCellColumnFilter.Index.EQUAL));
		walker.addHSSFCellFilter(new HSSFCellRowFilter(rowIndex,
				HSSFCellRowFilter.Index.BELOW));
			walker.walk();
		return result;
	}

	public static Collection<HSSFCell> getCellsAboveCell(HSSFSheet sheet,
			HSSFCell cell, Collection<HSSFCellFilter> filters) {
		final Collection<HSSFCell> result = new ArrayList<HSSFCell>();
		final AbstractHSSFSheetWalker walker = new AbstractHSSFSheetWalker(sheet) {
			@Override
			public void handleCell(HSSFCell cell) {
				result.add(cell);
			}
		};
		final int rowIndex = cell.getRowIndex();
		final int colIndex = cell.getColumnIndex();
		if (filters != null && !filters.isEmpty())
			walker.addAllHSSFCellFilters(filters);
		walker.addHSSFCellFilter(new HSSFCellColumnFilter(colIndex,
				HSSFCellColumnFilter.Index.EQUAL));
		walker.addHSSFCellFilter(new HSSFCellRowFilter(rowIndex,
				HSSFCellRowFilter.Index.BELOW));
			walker.walk();
		return result;
	}
	
	public static Collection<HSSFCell> getCellsBelowCell(HSSFSheet sheet,
			HSSFCell cell, final HSSFCellFilter filter) {
		return getCellsBelowCell(sheet, cell, new ArrayList<HSSFCellFilter>() {
			{
				add(filter);
			}
		});
	}
	
	public static Collection<HSSFCell> getCellsBelowCell(HSSFSheet sheet,
			HSSFCell cell) {
		final Collection<HSSFCell> result = new CopyOnWriteArrayList<HSSFCell>();
		final AbstractHSSFSheetWalker walker = new AbstractHSSFSheetWalker(sheet) {
			@Override
			public void handleCell(HSSFCell cell) {
				result.add(cell);
			}
		};
		final int rowIndex = cell.getRowIndex();
		final int colIndex = cell.getColumnIndex();
		walker.addHSSFCellFilter(new HSSFCellColumnFilter(colIndex,
				HSSFCellColumnFilter.Index.EQUAL));
		walker.addHSSFCellFilter(new HSSFCellRowFilter(rowIndex,
				HSSFCellRowFilter.Index.ABOVE));
			walker.walk();
		return result;
	}

	public static Collection<HSSFCell> getCellsBelowCell(HSSFSheet sheet,
			HSSFCell cell, Collection<HSSFCellFilter> filters) {
		final Collection<HSSFCell> result = new ArrayList<HSSFCell>();
		final AbstractHSSFSheetWalker walker = new AbstractHSSFSheetWalker(sheet) {
			@Override
			public void handleCell(HSSFCell cell) {
				result.add(cell);
			}
		};
		final int rowIndex = cell.getRowIndex();
		final int colIndex = cell.getColumnIndex();
		if (filters != null && !filters.isEmpty())
			walker.addAllHSSFCellFilters(filters);
		walker.addHSSFCellFilter(new HSSFCellColumnFilter(colIndex,
				HSSFCellColumnFilter.Index.EQUAL));
		walker.addHSSFCellFilter(new HSSFCellRowFilter(rowIndex,
				HSSFCellRowFilter.Index.ABOVE));
			walker.walk();
		return result;
	}

	public static HSSFCell getNextStringCellInRow(HSSFSheet sheet,
			final HSSFCell cellOrig) {
		HSSFCell result = null;
		final class Walker extends AbstractHSSFSheetWalker {
			HSSFCell result;

			Walker(HSSFSheet sheet) {
				super(sheet);
			}

			@Override
			public void handleCell(HSSFCell cell) {
				this.result = cell;
			}
		}
		final Walker walker = new Walker(sheet);
		final HSSFCellFilter f1 = new HSSFCellTypeStringFilter();
		final HSSFCellFilter f2 = new HSSFCellFilter() {
			@Override
			public boolean accept(HSSFCell cell) {
				return (cell.getRowIndex() == cellOrig.getRowIndex() && cell
						.getColumnIndex() > cellOrig.getColumnIndex());
			}
		};
		walker.addHSSFCellFilter(f1);
		walker.addHSSFCellFilter(f2);
		synchronized (walker) {
			walker.walk();
			result = walker.result;
		}
		if (result == null)
			throw new NullPointerException("could not get cell");
		return result;
	}

	public static HSSFCell getNextNumericCellInRow(HSSFSheet sheet, final HSSFCell cellOrig) {
		HSSFCell result = null;
		final class Walker extends AbstractHSSFSheetWalker {
			HSSFCell result;

			Walker(HSSFSheet sheet) {
				super(sheet);
			}

			@Override
			public void handleCell(HSSFCell cell) {
				this.result = cell;
			}
		}
		final Walker walker = new Walker(sheet);
		final HSSFCellFilter f1 = new HSSFCellTypeNumericFilter();
		final HSSFCellFilter f2 = new HSSFCellFilter() {
			@Override
			public boolean accept(HSSFCell cell) {
				return (cell.getRowIndex() == cellOrig.getRowIndex() && cell
						.getColumnIndex() > cellOrig.getColumnIndex());
			}
		};
		walker.addHSSFCellFilter(f1);
		walker.addHSSFCellFilter(f2);
		synchronized (walker) {
			walker.walk();
			result = walker.result;
		}
		if (result == null)
			throw new NullPointerException("could not get cell");
		return result;
	
	}
}
