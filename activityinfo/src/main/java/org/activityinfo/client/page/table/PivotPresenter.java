package org.activityinfo.client.page.table;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.callback.DownloadCallback;
import org.activityinfo.client.dispatch.callback.Got;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.*;

import java.util.Date;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPresenter implements PagePresenter {


    @ImplementedBy(PivotPage.class)
    public interface View {

        public void bindPresenter(PivotPresenter presenter);

        public void enableUIAction(String actionId, boolean enabled);

        public ListStore<Dimension> getRowStore();

        public ListStore<Dimension> getColStore();

        public ListStore<Dimension> getUnsusedStore();

        public void setContent(PivotTableElement element);

        public AsyncMonitor getMonitor();

        void setSchema(Schema schema);

        List<IndicatorModel> getSelectedIndicators();

        List<AdminEntityModel> getAdminRestrictions();

        Date getMinDate();

        Date getMaxDate();
    }


    private final Dispatcher service;
    private final View view;
    private final EventBus eventBus;

    @Inject
    public PivotPresenter(Dispatcher service, EventBus eventBus, final View view) {
        this.service = service;
        this.view = view;
        this.eventBus = eventBus;
        this.view.bindPresenter(this);

        ListStore<Dimension> store = view.getRowStore();
        store.add(createDimension(DimensionType.Database, Application.CONSTANTS.database()));
        store.add(createDimension(DimensionType.Activity, Application.CONSTANTS.activity()));
        store.add(createDimension(DimensionType.Indicator, Application.CONSTANTS.indicators()));

        store = view.getColStore();
        store.add(createDimension(DimensionType.Partner, Application.CONSTANTS.partner()));

        store = view.getUnsusedStore();
        store.add(createDimension(DateUnit.YEAR, Application.CONSTANTS.year()));
        store.add(createDimension(DateUnit.QUARTER, Application.CONSTANTS.quarter()));
        store.add(createDimension(DateUnit.MONTH, Application.CONSTANTS.month()));

        service.execute(new GetSchema(), view.getMonitor(), new Got<Schema>() {
            @Override
            public void got(Schema result) {

                view.setSchema(result);

                ListStore<Dimension> store = view.getUnsusedStore();

                for (CountryModel country : result.getCountries()) {
                    for (AdminLevelModel level : country.getAdminLevels()) {
                        AdminDimension dimension = new AdminDimension(level.getId());
                        dimension.set("caption", level.getName());
                        store.add(dimension);
                    }
                }
            }
        });

    }

    public void shutdown() {

    }

    private Dimension createDimension(DimensionType type, String caption) {
        Dimension dim = new Dimension(type);
        dim.set("caption", caption);
        return dim;
    }

    private Dimension createDimension(DateUnit unit, String caption) {
        Dimension dim = new DateDimension(unit);
        dim.set("caption", caption);
        return dim;
    }

    public void onDimensionsChanged() {

    }

    protected PivotTableElement createElement() {
        PivotTableElement table = new PivotTableElement();
        table.setRowDimensions(view.getRowStore().getModels());
        table.setColumnDimensions(view.getColStore().getModels());

        List<IndicatorModel> selectedIndicators = view.getSelectedIndicators();
        for (IndicatorModel indicator : selectedIndicators) {
            table.getFilter().addRestriction(DimensionType.Indicator, indicator.getId());
        }

        List<AdminEntityModel> entities = view.getAdminRestrictions();
        for (AdminEntityModel entity : entities) {
            table.getFilter().addRestriction(DimensionType.AdminLevel, entity.getId());
        }

        if (view.getMinDate() != null)
            table.getFilter().setMinDate(view.getMinDate());
        if (view.getMaxDate() != null)
            table.getFilter().setMaxDate(view.getMaxDate());

        return table;
    }

    public void onUIAction(String itemId) {
        if (UIActions.refresh.equals(itemId)) {
            final PivotTableElement element = createElement();
            service.execute(new GenerateElement(element), view.getMonitor(), new AsyncCallback<Content>() {
                public void onFailure(Throwable throwable) {
                    MessageBox.alert(Application.CONSTANTS.error(), Application.CONSTANTS.errorOnServer(), null);
                }

                public void onSuccess(Content content) {
                    element.setContent((PivotContent) content);
                    view.setContent(element);
                }
            });

        } else if (UIActions.export.equals(itemId)) {
            service.execute(new RenderElement(createElement(), RenderElement.Format.Excel), view.getMonitor(),
                    new DownloadCallback(eventBus, "pivotTable"));

//             service.export(createElement(), RenderElement.Format.Excel, view.getMonitor(),
//                     new AsyncCallback<Void>() {
//                         @Override
//                         public void onFailure(Throwable caught) {
//
//                         }
//
//                         @Override
//                         public void onSuccess(Void result) {
//
//                         }
//                     });
        }
    }


    @Override
    public PageId getPageId() {
        return Pages.Pivot;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public boolean navigate(Place place) {
        return true;
    }
}
