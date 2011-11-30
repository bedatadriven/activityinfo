package org.sigmah.server.endpoint.export;

import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

public abstract class Exporter {

	public final HSSFWorkbook book;
	public final CreationHelper creationHelper;
	public HashMap<String, Integer> sheetNames;

	private CellStyle dateStyle;
	private CellStyle coordStyle;
	private CellStyle indicatorValueStyle;

	private CellStyle headerStyle;
	private CellStyle headerStyleCenter;
	private CellStyle headerStyleRight;

	public Exporter() {
		book = new HSSFWorkbook();
		creationHelper = book.getCreationHelper();
		sheetNames = new HashMap<String, Integer>();
	}

	public HSSFWorkbook getBook() {
		return book;
	}

	protected void declareStyles() {
		dateStyle = book.createCellStyle();
		dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("m/d/yy"));

		coordStyle = book.createCellStyle();
		coordStyle.setDataFormat(creationHelper.createDataFormat().getFormat("0.000000"));

		indicatorValueStyle = book.createCellStyle();
		indicatorValueStyle.setDataFormat(creationHelper.createDataFormat().getFormat("#,##0"));

		Font headerFont = book.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

		Font smallFont = book.createFont();
		smallFont.setFontHeightInPoints((short) 8);

		headerStyle = book.createCellStyle();
		headerStyle.setFont(headerFont);

		headerStyleCenter = book.createCellStyle();
		headerStyleCenter.setFont(headerFont);
		headerStyleCenter.setAlignment(CellStyle.ALIGN_CENTER);

		headerStyleRight = book.createCellStyle();
		headerStyleRight.setFont(headerFont);
		headerStyleRight.setAlignment(CellStyle.ALIGN_RIGHT);

	}

	protected Cell createHeaderCell(Row headerRow, int columnIndex, String text, CellStyle style) {
		Cell cell = headerRow.createCell(columnIndex);
		cell.setCellValue(creationHelper.createRichTextString(text));
		cell.setCellStyle(style);

		return cell;
	}

	protected Cell createHeaderCell(Row headerRow, int columnIndex, String text) {
		return createHeaderCell(headerRow, columnIndex, text, CellStyle.ALIGN_LEFT);
	}

	protected Cell createHeaderCell(Row headerRow, int columnIndex, String text, int align) {
		Cell cell = headerRow.createCell(columnIndex);
		cell.setCellValue(creationHelper.createRichTextString(text));

		switch (align) {
		case CellStyle.ALIGN_LEFT:
			cell.setCellStyle(headerStyle);
			break;
		case CellStyle.ALIGN_CENTER:
			cell.setCellStyle(headerStyleCenter);
			break;
		case CellStyle.ALIGN_RIGHT:
			cell.setCellStyle(headerStyleRight);
			break;
		}

		return cell;
	}

	protected Cell createCell(Row row, int columnIndex, String text) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellValue(creationHelper.createRichTextString(text));
		return cell;
	}

	protected Cell createCell(Row row, int columnIndex, String text, CellStyle style) {
		Cell cell = createCell(row, columnIndex, text);
		cell.setCellStyle(style);
		return cell;
	}

	protected void createCell(Row row, int columnIndex, Date date) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellValue(date);
		cell.setCellStyle(dateStyle);
	}
}
