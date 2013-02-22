package org.activityinfo.server.report.renderer.excel;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.model.ReportElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class BaseExcelRenderer<ElementT extends ReportElement> {

    protected final ElementT element;
    protected final Workbook book;
    protected final Sheet sheet;
    protected final CreationHelper factory;

    protected int rowIndex;

    public BaseExcelRenderer(Workbook book, ElementT element) {

        this.element = element;
        this.book = book;
        this.factory = book.getCreationHelper();
        this.sheet = book.createSheet(composeSheetName());

        /* Create title line */

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell
            .setCellValue(factory.createRichTextString(element.getTitle()));

        /* Create filter descriptors */

        List<FilterDescription> descs = generateFilterDescriptions();

        rowIndex = 2;

        for (FilterDescription desc : descs) {

            Row filterRow = sheet.createRow(rowIndex++);
            Cell filterCell = filterRow.createCell(0);

            filterCell.setCellValue(factory.createRichTextString(desc
                .joinLabels(", ")));
        }

        rowIndex++;

        generate();
    }

    public abstract List<FilterDescription> generateFilterDescriptions();

    public String composeSheetName() {
        if (element.getSheetTitle() != null) {
            return element.getSheetTitle();
        } else if (element.getTitle() != null) {
            return element.getTitle();
        } else {
            return "Sheet" + (book.getNumberOfSheets() + 1);
        }
    }

    public void generate() {

    }

    protected Font createBaseFont() {
        Font font = book.createFont();

        return font;
    }

}
