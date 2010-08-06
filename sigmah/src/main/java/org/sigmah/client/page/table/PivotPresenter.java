/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.DownloadCallback;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.dto.*;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.*;

import java.util.Date;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPresenter implements Page {
    public static final PageId Pivot = new PageId("pivot");

    @ImplementedBy(PivotPage.class)
    public interface View {

        public void bindPresenter(PivotPresenter presenter);

        public void enableUIAction(String actionId, boolean enabled);

        public ListStore<Dimension> getRowStore();

        public ListStore<Dimension> getColStore();

        public void setContent(PivotTableElement element);

        public AsyncMonitor getMonitor();

        void setSchema(SchemaDTO schema);

        List<IndicatorDTO> getSelectedIndicators();

        List<AdminEntityDTO> getAdminRestrictions();

        Date getMinDate();

        Date getMaxDate();
        
        List<PartnerDTO> getPartnerRestrictions();
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
        store.add(createDimension(DimensionType.Database, I18N.CONSTANTS.database()));
        store.add(createDimension(DimensionType.Activity, I18N.CONSTANTS.activity()));
        store.add(createDimension(DimensionType.Indicator, I18N.CONSTANTS.indicators()));
        
        store = view.getColStore();
        store.add(createDimension(DimensionType.Partner, I18N.CONSTANTS.partner()));
    }

    public void shutdown() {

    }

    public void onDimensionsChanged() {

    }

    protected PivotTableElement createElement() {
        PivotTableElement table = new PivotTableElement();
        table.setRowDimensions(view.getRowStore().getModels());
        table.setColumnDimensions(view.getColStore().getModels());

        List<IndicatorDTO> selectedIndicators = view.getSelectedIndicators();
        for (IndicatorDTO indicator : selectedIndicators) {
            table.getFilter().addRestriction(DimensionType.Indicator, indicator.getId());
        }

        List<AdminEntityDTO> entities = view.getAdminRestrictions();
        for (AdminEntityDTO entity : entities) {
            table.getFilter().addRestriction(DimensionType.AdminLevel, entity.getId());
        }

        List<PartnerDTO> partners = view.getPartnerRestrictions();
        for (PartnerDTO entity : partners) {
            table.getFilter().addRestriction(DimensionType.Partner, entity.getId());
        }
        
        if (view.getMinDate() != null) {	
            table.getFilter().setMinDate(view.getMinDate());
        }
        
        if (view.getMaxDate() != null) {
            table.getFilter().setMaxDate(view.getMaxDate());
        }
        Log.debug("sending element");
        return table;
    }

    public void onUIAction(String itemId) {
        if (UIActions.refresh.equals(itemId)) {
            final PivotTableElement element = createElement();
            service.execute(new GenerateElement(element), view.getMonitor(), new AsyncCallback<Content>() {
                public void onFailure(Throwable throwable) {
                    MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer(), null);
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
        return Pivot;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public boolean navigate(PageState place) {
        return true;
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
}
