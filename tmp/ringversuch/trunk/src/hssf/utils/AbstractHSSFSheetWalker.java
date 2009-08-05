package hssf.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public abstract class AbstractHSSFSheetWalker {

	protected final HSSFSheet sheet;
	private final Collection<HSSFCellFilter> filters = new ArrayList<HSSFCellFilter>();

	public AbstractHSSFSheetWalker(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public synchronized void addHSSFCellFilter(HSSFCellFilter filter) {
		this.filters.add(filter);
	}
	
	public synchronized void addAllHSSFCellFilters(Collection<HSSFCellFilter> filter) {
		this.filters.addAll(filter);
	}

	@SuppressWarnings("unchecked")
	public synchronized void walk() {
		final Iterator<HSSFRow> i = sheet.rowIterator();
		while (i.hasNext()) {
			final HSSFRow r = (HSSFRow) i.next();
			final Iterator<HSSFCell> i2 = r.cellIterator();
			while (i2.hasNext()) {
				final HSSFCell c = (HSSFCell) i2.next();
//				LOGGER.debug("current cell="+c.getRowIndex()+","+c.getColumnIndex());
				if (accepted(c))
					handleCell(c);
			}
		}
	}

	private boolean accepted(HSSFCell c) {
		if (filters.size() == 0)
			return true;
		if(c == null)
			return false;
		for (HSSFCellFilter f : filters) {
			if (!f.accept(c))
				return false;
		}
		return true;
	}

	public abstract void handleCell(HSSFCell cell);
}
