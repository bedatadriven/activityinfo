package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.grid.AbstractEditorGridView;
import org.activityinfo.client.page.common.widget.MappingComboBox;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorRowDTO;

import java.util.ArrayList;
import java.util.List;

public class MonthlyGrid extends AbstractEditorGridView<IndicatorRowDTO, MonthlyPresenter>
                        implements MonthlyPresenter.View {

    private MonthlyPresenter presenter;
    private EditorGrid<IndicatorRowDTO> grid;

    private ActivityDTO activity;

    public MonthlyGrid(ActivityDTO activity) {
        this.activity = activity;
        this.setHeading(Application.CONSTANTS.monthlyReports());
        this.setLayout(new FitLayout());
        this.setBorders(false);
        this.setFrame(false);

    }

    public void init(MonthlyPresenter presenter, ListStore<IndicatorRowDTO> store) {
        super.init(presenter, store);
        this.presenter = presenter;
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
    }

    @Override
    protected Grid<IndicatorRowDTO> createGridAndAddToContainer(Store store) {

        grid = new EditorGrid<IndicatorRowDTO>((ListStore)store, createColumnModel());
        grid.setAutoExpandColumn("indicatorName");
        grid.setLoadMask(true);

        add(grid);

        return grid;
    }

    public void setStartMonth(Month startMonth) {

        DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM yy");

        Month month = startMonth;
        for(int i=0; i!=7; ++i) {
            DateWrapper date = new DateWrapper(month.getYear(), month.getMonth()-1, 1);
        
            grid.getColumnModel().setColumnHeader(i+1, monthFormat.format(date.asDate()));
            grid.getColumnModel().setDataIndex(i+1, IndicatorRowDTO.propertyName(month));
            month = month.next();
        }
    }

    public ColumnModel createColumnModel() {

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig indicator = new ColumnConfig("indicatorName", Application.CONSTANTS.indicators(), 150);
        indicator.setSortable(false);
        indicator.setMenuDisabled(true);
        columns.add(indicator);

        NumberFormat indicatorFormat = NumberFormat.getFormat("0");

        for(int i = 0; i!=7; ++i) {

            NumberField indicatorField = new NumberField();
            indicatorField.getPropertyEditor().setFormat(indicatorFormat);

            ColumnConfig valueColumn = new ColumnConfig("month" + i, "", 75);
            valueColumn.setNumberFormat(indicatorFormat);
            valueColumn.setEditor(new CellEditor(indicatorField));
            valueColumn.setSortable(false);
            valueColumn.setMenuDisabled(true);

            columns.add(valueColumn);
        }

        return new ColumnModel(columns);
    }

    @Override
    protected void initToolBar() {

        toolBar.addSaveSplitButton();
        toolBar.add(new LabelToolItem(Application.CONSTANTS.month() + ": "));

        final MappingComboBox<Month> monthCombo = new MappingComboBox<Month>();
        monthCombo.setEditable(false);
        monthCombo.addListener(Events.Select, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                presenter.onMonthSelected(monthCombo.getMappedValue());
            }
        });
           
        DateWrapper today = new DateWrapper();
        DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM yyyy");
        for(int year = today.getFullYear(); year != today.getFullYear()-3; --year) {

            for(int month = 12; month != 0; --month) {

                DateWrapper d= new DateWrapper(year, month, 1);

                Month m = new Month(year, month);
                monthCombo.add(m, monthFormat.format(d.asDate()));
            }
        }



        toolBar.add(monthCombo);
        
    }
}
