package org.activityinfo.client.page.report;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.grid.AbstractEditorGridView;
import org.activityinfo.client.page.common.widget.MappingComboBox;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.report.model.ReportFrequency;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ReportGrid extends AbstractEditorGridView<ReportTemplateDTO, ReportHomePresenter>
        implements ReportHomePresenter.View {

    private MappingComboBox<Integer> dayCombo;
    private String[] weekdays;
    private NumberFormat numberFormat;

    @Inject
    public ReportGrid() {

        setLayout(new FitLayout());
        setHeaderVisible(false);
        weekdays = LocaleInfo.getCurrentLocale().getDateTimeConstants().weekdays();
        numberFormat = NumberFormat.getFormat("0");
    }

    public void init(ReportHomePresenter presenter, ListStore<ReportTemplateDTO> store) {
        super.init(presenter, store);
    }

    @Override
    protected Grid<ReportTemplateDTO> createGridAndAddToContainer(Store store) {

        EditorGrid<ReportTemplateDTO> grid = new EditorGrid<ReportTemplateDTO>((ListStore) store, createColumnModel());
        grid.addListener(Events.BeforeEdit, new Listener<GridEvent<ReportTemplateDTO>>() {
            public void handleEvent(GridEvent<ReportTemplateDTO> be) {
                ReportTemplateDTO report = be.getModel();
                be.setCancelled( ! (report.getFrequency() == ReportFrequency.MONTHLY ||
                                    report.getFrequency() == ReportFrequency.WEEKLY ||
                                    report.getFrequency() == ReportFrequency.DAILY) );
            }
        });

        GroupingView view = new GroupingView();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        view.setGroupRenderer(new GridGroupRenderer() {
            public String render(GroupColumnData data) {
                return data.group;
            }
        });
        grid.setView(view);

        grid.setAutoExpandColumn("title");
        grid.setAutoExpandMin(250);

        grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<ReportTemplateDTO>>() {
            public void handleEvent(GridEvent<ReportTemplateDTO> event) {
                if(event.getColIndex() == 1)
                    presenter.onTemplateSelected(event.getModel());
            }
        });

        add(grid);

        return grid;
    }

    private ColumnModel createColumnModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(new ColumnConfig("databaseName", Application.CONSTANTS.database(), 100));

        ColumnConfig name = new ColumnConfig("title", Application.CONSTANTS.name(), 250);
        name.setRenderer(new GridCellRenderer<ReportTemplateDTO>() {
            public Object render(ReportTemplateDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                return "<b>" + model.getTitle() + "</b><br>" + model.getDescription();
            }
        });
        columns.add(name);

        ColumnConfig frequency = new ColumnConfig("frequency", Application.CONSTANTS.reportingFrequency(), 100);
        frequency.setRenderer(new GridCellRenderer<ReportTemplateDTO>() {
            public Object render(ReportTemplateDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                if(model.getFrequency() == ReportFrequency.MONTHLY) {
                    return Application.CONSTANTS.monthly();
                } else if(model.getFrequency() == ReportFrequency.WEEKLY) {
                    return Application.CONSTANTS.weekly();
                } else if(model.getFrequency() == ReportFrequency.DAILY) {
                    return Application.CONSTANTS.daily();
                } else if(model.getFrequency() == ReportFrequency.ADHOC) {
                    return "ad hoc";
                }
                return "-";
            }
        });
        columns.add(frequency);

        ColumnConfig day = new ColumnConfig("day", "Jour", 100);
        day.setRenderer(new GridCellRenderer<ReportTemplateDTO>() {
            public Object render(ReportTemplateDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ReportTemplateDTO> store, Grid<ReportTemplateDTO> grid) {
                if(model.getFrequency() == ReportFrequency.WEEKLY)
                    return model.getDay() < 7 ? weekdays[model.getDay()] :
                            weekdays[6];
                else if(model.getFrequency() == ReportFrequency.MONTHLY)
                    return Integer.toString(model.getDay());
                else
                    return "";
            }
        });
        columns.add(day);

        ColumnConfig subscribed = new ColumnConfig("subscribed", "Abonnement Email", 100);
        subscribed.setRenderer(new GridCellRenderer<ReportTemplateDTO>() {
            @Override
            public Object render(ReportTemplateDTO model, String property, ColumnData columnData, int rowIndex, int colIndex, ListStore<ReportTemplateDTO> store, Grid<ReportTemplateDTO> grid) {
                if(model.isSubscribed()) {
                    return Application.CONSTANTS.yes();
                } else {
                    return "";
                }
            }
        });

        final MappingComboBox<Boolean> subCombo = new MappingComboBox();
        subCombo.add(true, Application.CONSTANTS.yes());
        subCombo.add(false, Application.CONSTANTS.no());

        CellEditor subEditor = new CellEditor(subCombo) {
            @Override
            public Object preProcessValue(Object o) {
                return subCombo.wrap((Boolean)o);
            }

            @Override
            public Object postProcessValue(Object o) {
                return ((MappingComboBox.Wrapper)o).getWrappedValue();
            }
        };
        subscribed.setEditor(subEditor);
        columns.add(subscribed);

        return new ColumnModel(columns);
    }


    @Override
    protected void initToolBar() {
        toolBar.addSaveSplitButton();
        toolBar.add(new SeparatorToolItem());
        toolBar.addButton(UIActions.add, Application.CONSTANTS.newText(), Application.ICONS.add());
    }


}
