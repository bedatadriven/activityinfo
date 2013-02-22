package org.activityinfo.server.endpoint.export;

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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.UserPermissionDTO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class DbUserExport extends Exporter {

    private final List<UserPermissionDTO> users;

    public DbUserExport(List<UserPermissionDTO> users) {
        super();
        this.users = users;

        declareStyles();
    }

    public void createSheet() {
        HSSFSheet sheet = book
            .createSheet(composeUniqueSheetName("db-users-list"));
        sheet.createFreezePane(4, 2);

        // initConditionalFormatting(sheet);
        createHeaders(sheet);
        createDataRows(sheet);
    }

    private String composeUniqueSheetName(String name) {
        String sheetName = name;
        // replace invalid chars: / \ [ ] * ?
        sheetName = sheetName.replaceAll("[\\Q/\\*?[]\\E]", " ");

        // sheet names can only be 31 characters long, plus we need about 4-6
        // chars for disambiguation
        String shortenedName = sheetName.substring(0,
            Math.min(25, sheetName.length()));

        // assure that the sheet name is unique
        if (!sheetNames.containsKey(shortenedName)) {
            sheetNames.put(shortenedName, 1);
            return sheetName;
        } else {
            int index = sheetNames.get(shortenedName);
            sheetNames.put(shortenedName, index + 1);
            return shortenedName + " (" + index + ")";
        }

    }

    private void createHeaders(HSSFSheet sheet) {
        // / The HEADER rows
        Row headerRow = sheet.createRow(0);
        int column = 0;
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.name(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.email(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.partner(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.allowView(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.allowViewAll(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.allowDesign(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.allowEdit(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++, I18N.CONSTANTS.allowEditAll(),
            CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++,
            I18N.CONSTANTS.allowManageUsers(), CellStyle.ALIGN_RIGHT);
        createHeaderCell(headerRow, column++,
            I18N.CONSTANTS.allowManageAllUsers(), CellStyle.ALIGN_RIGHT);

        sheet.setColumnWidth(column, 12 * 256);
        sheet.setColumnWidth(column + 1, 12 * 256);
    }

    private void createDataRows(Sheet sheet) {

        ((HSSFSheet) sheet).createDrawingPatriarch();

        int rowIndex = 2;
        for (UserPermissionDTO user : users) {

            Row row = sheet.createRow(rowIndex++);
            int column = 0;
            createCell(row, column++, user.getName());
            createCell(row, column++, user.getEmail());
            createCell(row, column++, String.valueOf(user.getPartner()));
            createCell(row, column++, String.valueOf(user.getAllowView()));
            createCell(row, column++, String.valueOf(user.getAllowViewAll()));
            createCell(row, column++, String.valueOf(user.getAllowDesign()));
            createCell(row, column++, String.valueOf(user.getAllowEdit()));
            createCell(row, column++, String.valueOf(user.getAllowEditAll()));
            createCell(row, column++,
                String.valueOf(user.getAllowManageUsers()));
            createCell(row, column++,
                String.valueOf(user.getAllowManageAllUsers()));
        }
    }

    @Override
    protected void declareStyles() {
        super.declareStyles();
    }
}
