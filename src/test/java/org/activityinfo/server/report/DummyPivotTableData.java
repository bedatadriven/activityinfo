

package org.activityinfo.server.report;

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

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.content.YearCategory;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DummyPivotTableData {



    public Dimension partnerDim = new Dimension(DimensionType.Partner);
    public Dimension provinceDim = new AdminDimension(1);
    public List<Dimension> rowDims = new ArrayList<Dimension>();

    public Dimension yearDim = new DateDimension(DateUnit.YEAR);
    public Dimension indicatorDim = new DateDimension(DateUnit.YEAR);
    public List<Dimension> colDims = new ArrayList<Dimension>();


    public PivotTableData.Axis[] leafRows = new PivotTableData.Axis[4];
    public PivotTableData.Axis[] leafCols = new PivotTableData.Axis[5];
    public PivotTableData table = new PivotTableData();
    public PivotTableData.Axis row1 = table.getRootRow().addChild(partnerDim, new EntityCategory(1, "AVSI"), "AVSI", null);
    public PivotTableData.Axis row2 = table.getRootRow().addChild(partnerDim, new EntityCategory(1, "NRC"), "NRC", null);
    public PivotTableData.Axis col1 = table.getRootColumn().addChild(yearDim, new YearCategory(2007), "2007", null );
    public PivotTableData.Axis col2 = table.getRootColumn().addChild(yearDim, new YearCategory(2009), "2009", null );

    public DummyPivotTableData() {

        rowDims.add(partnerDim);
        rowDims.add(provinceDim);

        colDims.add(yearDim);
        colDims.add(indicatorDim);

        leafRows[0] = row1.addChild(provinceDim, new EntityCategory(61, "Nord Kivu"), "Nord", null);
        leafRows[1] = row1.addChild(provinceDim, new EntityCategory(62, "Sud Kivu"), "Sud Kivu", null);

        leafRows[2] = row2.addChild(provinceDim, new EntityCategory(61, "Nord Kivu"), "Nord", null);
        leafRows[3] = row2.addChild(provinceDim, new EntityCategory(62, "Sud Kivu"), "Sud Kivu", null);

        leafCols[0] = col1.addChild(indicatorDim, new EntityCategory(201, "NFI"), "NFI", null );
        leafCols[1] = col1.addChild(indicatorDim, new EntityCategory(202, "Bache"), "Bache", null );

        leafCols[2] = col2.addChild(indicatorDim, new EntityCategory(201, "NFI"), "NFI", null );
        leafCols[3] = col2.addChild(indicatorDim, new EntityCategory(202, "Bache"), "Bache", null );
        leafCols[4] = col2.addChild(indicatorDim, new EntityCategory(203, "Abri"), "Abri", null );

        for(int i=0; i!= leafRows.length; ++i) {
            for(int j=0; j!= leafCols.length; ++j) {
                leafRows[i].setValue(leafCols[j], (double)(i * (j+9) * 100));
            }
        }

    }


    public PivotTableReportElement Foobar1612Element() {
        PivotTableReportElement element = new PivotTableReportElement();
        element.setTitle("Foobar 1612");
        element.setRowDimensions(rowDims);
        element.setColumnDimensions(colDims);
        element.setContent(new PivotContent(table, new ArrayList<FilterDescription>()));
       
        return element;
    }


}
