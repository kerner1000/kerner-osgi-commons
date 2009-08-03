package hssf.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public abstract class AbstractHSSFSheetWalker {

	private final HSSFSheet sheet;
	private final Collection<HSSFCellTypeFilter> filters = new ArrayList<HSSFCellTypeFilter>();

	public AbstractHSSFSheetWalker(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public synchronized void addHSSFCellFilter(HSSFCellTypeFilter filter) {
		this.filters.add(filter);
	}

	@SuppressWarnings("unchecked")
	public synchronized void walk() {
		final Iterator<HSSFRow> i = sheet.rowIterator();
		while (i.hasNext()) {
			final HSSFRow r = (HSSFRow) i.next();
			final Iterator<HSSFCell> i2 = r.cellIterator();
			while (i2.hasNext()) {
				final HSSFCell c = (HSSFCell) i2.next();
				if (accepted(c))
					handleCell(c);
			}
		}
	}

	private boolean accepted(HSSFCell c) {
		if (filters.size() == 0)
			return true;
		for (HSSFCellTypeFilter f : filters) {
			if (!f.accept(c))
				return false;
		}
		return true;
	}

	public abstract void handleCell(HSSFCell cell);
}
