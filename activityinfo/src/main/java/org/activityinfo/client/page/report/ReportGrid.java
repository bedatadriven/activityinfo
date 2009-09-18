package org.activityinfo.client.page.report;

import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridSelectionModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.google.inject.Inject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.core.client.GWT;

import org.activityinfo.client.common.grid.AbstractEditorGridView;
import org.activityinfo.client.common.widget.MappingComboBox;
import org.activityinfo.client.Application;
import org.activityinfo.shared.dto.ReportTemplateDTO;
import org.activityinfo.shared.domain.Subscription;

import java.util.List;
import java.util.ArrayList;

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
                if("subscriptionFrequency".equals(be.getProperty())) {
                    if(report.getSubscriptionFrequency() == Subscription.MONTHLY) {
                        if(report.getSubscriptionFrequency() < 1)
                            report.setSubscriptionFrequency(1);
                        else if(report.getSubscriptionFrequency() > Subscription.LAST_DAY_OF_MONTH)
                            report.setSubscriptionFrequency(28);
                    } else if(report.getSubscriptionFrequency() == Subscription.WEEKLY) {
                        if(report.getSubscriptionDay() > 6)
                            report.setSubscriptionFrequency(6);
                    }

                } else if("subscriptionDay".equals(be.getProperty())) {
                    if(report.getSubscriptionFrequency() <= Subscription.DAILY ) {
                        be.setCancelled(true);
                    } else {
                        prepareDayCombo(report.getSubscriptionFrequency());
                    }
                }
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

        columns.add(createSubscriptionColumn());
        columns.add(createDayColumn());

        return new ColumnModel(columns);
    }

    private ColumnConfig createSubscriptionColumn() {
        final MappingComboBox<Integer> subscriptionCombo = new MappingComboBox<Integer>();
        subscriptionCombo.add(Subscription.NONE, "Aucune");    // TODO i18n
        subscriptionCombo.add(Subscription.DAILY, "Journalière");
        subscriptionCombo.add(Subscription.WEEKLY, "Hebdodomaire");
        subscriptionCombo.add(Subscription.MONTHLY, "Mensuelle");

        ColumnConfig subscription = new ColumnConfig("subscriptionFrequency", "Livraison par email", 100);
        subscription.setRenderer(new GridCellRenderer<ReportTemplateDTO>() {
            public Object render(ReportTemplateDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore listStore, Grid grid) {
                return subscriptionCombo.getValueLabel(model.getSubscriptionFrequency());
            }
        });
        subscription.setEditor(new CellEditor(subscriptionCombo) {
            @Override
            public Object preProcessValue(Object frequency) {
                return subscriptionCombo.wrap((Integer) frequency);
            }

            @Override
            public Object postProcessValue(Object wrapper) {
                return ((ModelData)wrapper).get("value");
            }
        });
        return subscription;
    }

    private ColumnConfig createDayColumn() {

        dayCombo = new MappingComboBox<Integer>();

        ColumnConfig day = new ColumnConfig("subscriptionDay", "Jour", 100);
        day.setRenderer(new GridCellRenderer<ReportTemplateDTO>() {
            public Object render(ReportTemplateDTO model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<ReportTemplateDTO> reportTemplateDTOListStore, Grid<ReportTemplateDTO> reportTemplateDTOGrid) {
                if(model.getSubscriptionFrequency() == 0)
                    return "";
                else if(model.getSubscriptionFrequency() == 1)
                    return "-";
                else if(model.getSubscriptionFrequency() == 2)
                    return model.getSubscriptionDay() < 7 ? weekdays[model.getSubscriptionDay()] :
                            weekdays[6];
                else
                    return Integer.toString(model.getSubscriptionDay());
            }
        });
        day.setEditor(new CellEditor(dayCombo) {
            @Override
            public Object preProcessValue(Object value) {
                return dayCombo.wrap((Integer) value);
            }

            @Override
            public Object postProcessValue(Object value) {
                return ((ModelData)value).get("value");
            }
        });
        return day;
    }

    private void prepareDayCombo(int subscriptionFrequency) {
        if(subscriptionFrequency == Subscription.WEEKLY) {
            if(dayCombo.getStore().getCount() != 7) {
                dayCombo.getStore().removeAll();
                for(int i=0; i!=7;++i) {
                    dayCombo.add(i, weekdays[i]);
                }
            }
        } else {
            if(dayCombo.getStore().getCount() != 29) {
                dayCombo.getStore().removeAll();
                dayCombo.add(1, "Première jour de mois");
                dayCombo.add(Subscription.LAST_DAY_OF_MONTH, "Dernière jour de mois");
                for(int i=2;i!=Subscription.LAST_DAY_OF_MONTH;++i) {
                    dayCombo.add(i, numberFormat.format(i));
                }
            }
        }
    }

    @Override
    protected void initToolBar() {
        toolBar.addSaveSplitButton();
    }


}
