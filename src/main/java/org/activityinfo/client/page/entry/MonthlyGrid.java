/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.entry;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.util.IndicatorNumberFormat;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.dto.IndicatorRowDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;

/**
 * Grid for use in the MonthlyTab
 */
class MonthlyGrid extends EditorGrid<IndicatorRowDTO> {

	private static final int MONTHS_TO_SHOW = 7;

	private static final int ROW_HEADER_WIDTH = 150;
	private static final int MONTH_COLUMN_WIDTH = 75;

    public MonthlyGrid(ListStore<IndicatorRowDTO> store) {
        super(store, createColumnModel());

        setAutoExpandColumn("indicatorName");
        setLoadMask(true);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
    }

    /**
     * Updates the month headers based on the given start month
     */
    public void updateMonthColumns(Month startMonth) {
        DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM yy");

        Month month = startMonth;
        for(int i=0; i!=MONTHS_TO_SHOW; ++i) {
            DateWrapper date = new DateWrapper(month.getYear(), month.getMonth()-1, 1);
        
            getColumnModel().setColumnHeader(i+1, monthFormat.format(date.asDate()));
            getColumnModel().setDataIndex(i+1, IndicatorRowDTO.propertyName(month));
            month = month.next();
        }
    }

    private static ColumnModel createColumnModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig indicator = new ColumnConfig("indicatorName", I18N.CONSTANTS.indicators(), ROW_HEADER_WIDTH);
        indicator.setSortable(false);
        indicator.setMenuDisabled(true);
        columns.add(indicator);


        for(int i = 0; i!=MONTHS_TO_SHOW; ++i) {
            NumberField indicatorField = new NumberField();
            indicatorField.getPropertyEditor().setFormat(IndicatorNumberFormat.INSTANCE);

            ColumnConfig valueColumn = new ColumnConfig("month" + i, "", MONTH_COLUMN_WIDTH);
            valueColumn.setNumberFormat(IndicatorNumberFormat.INSTANCE);
            valueColumn.setEditor(new CellEditor(indicatorField));
            valueColumn.setSortable(false);
            valueColumn.setMenuDisabled(true);

            columns.add(valueColumn);
        }

        return new ColumnModel(columns);
    }
}
