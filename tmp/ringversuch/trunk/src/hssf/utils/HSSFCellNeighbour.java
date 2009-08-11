package hssf.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class HSSFCellNeighbour {

	public enum Orientation {
		NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORH_WEST
	}

	final private HSSFCell cell;
	private final HSSFSheet sheet;

	public HSSFCellNeighbour(HSSFSheet sheet, HSSFCell cell) {
		this.cell = cell;
		this.sheet = sheet;
	}

	public HSSFCell get(Orientation o) {
		switch (o) {
		case NORTH:
			return getNorth();
		case NORTH_EAST:
			return getNorthEast();
		case EAST:
			return getEast();
		case SOUTH_EAST:
			return getSouthEast();
		case SOUTH:
			return getSouth();
		case SOUTH_WEST:
			return getSouthWest();
		case WEST:
			return getWest();
		case NORH_WEST:
			return getNorthWest();
		default:
			System.err.println("warning: " + o + " is unknown orientation");
			return null;
		}
	}

	private HSSFCell getNorth() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex() - 1);
		if (row == null){
			System.err.println("returning null row");
			return null;
		}
		final HSSFCell result = row.getCell(cell.getColumnIndex());
		if (result == null){
			System.err.println("returning null cell");
			return null;
		}
		else
			return result;
	}

	private HSSFCell getNorthEast() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex() - 1);
		if (row == null)
			return null;
		final HSSFCell result = row.getCell(cell.getColumnIndex() + 1);
		if (result == null)
			return null;
		else
			return result;
	}

	private HSSFCell getEast() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex());
		if (row == null)
			return null;
		final HSSFCell result = row.getCell(cell.getColumnIndex() + 1);
		if (result == null)
			return null;
		else
			return result;
	}

	private HSSFCell getSouthEast() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex() + 1);
		if (row == null)
			return null;
		final HSSFCell result = row.getCell(cell.getColumnIndex() + 1);
		if (result == null)
			return null;
		else
			return result;
	}

	private HSSFCell getSouth() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex() + 1);
		if (row == null)
			return null;
		final HSSFCell result = row.getCell(cell.getColumnIndex());
		if (result == null)
			return null;
		else
			return result;
	}

	private HSSFCell getSouthWest() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex() + 1);
		if (row == null)
			return null;
		final HSSFCell result = row.getCell(cell.getColumnIndex() - 1);
		if (result == null)
			return null;
		else
			return result;
	}

	private HSSFCell getWest() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex());
		if (row == null)
			return null;
		final HSSFCell result = row.getCell(cell.getColumnIndex() - 1);
		if (result == null)
			return null;
		else
			return result;
	}

	private HSSFCell getNorthWest() {
		final HSSFRow row = sheet.getRow(cell.getRowIndex() - 1);
		if (row == null)
			return null;
		final HSSFCell result = row.getCell(cell.getColumnIndex() - 1);
		if (result == null)
			return null;
		else
			return result;
	}

	public static void main(String[] args){
		try {
			HSSFWorkbook wb = HSSFUtils.getWorkbookFromFile(new File("/home/pcb/kerner/Dropbox/Ergebnisse_RV_DIN38407-41/L01_RV_DIN38407-41.xls"));
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = sheet.getRow(12);
			HSSFCell cell = row.getCell(6);
			System.out.println(cell.getNumericCellValue());
			System.out.println("-------------------");
			HSSFCellNeighbour n = new HSSFCellNeighbour(sheet, cell);
			System.out.println(n.get(HSSFCellNeighbour.Orientation.NORTH).getNumericCellValue());
			System.out.println(n.get(HSSFCellNeighbour.Orientation.NORTH_EAST).getNumericCellValue());
			System.out.println(n.get(HSSFCellNeighbour.Orientation.EAST).getNumericCellValue());
			System.out.println(n.get(HSSFCellNeighbour.Orientation.SOUTH_EAST).getNumericCellValue());
			System.out.println(n.get(HSSFCellNeighbour.Orientation.SOUTH).getNumericCellValue());
			System.out.println(n.get(HSSFCellNeighbour.Orientation.SOUTH_WEST).getNumericCellValue());
			System.out.println(n.get(HSSFCellNeighbour.Orientation.WEST).getNumericCellValue());
			System.out.println(n.get(HSSFCellNeighbour.Orientation.NORH_WEST).getNumericCellValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
