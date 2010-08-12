/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import java.util.Date;
import java.util.List;

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
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotTableElement;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

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
        
        public TreeStore<ModelData> getDimensionStore();

        public void setContent(PivotTableElement element);

        public AsyncMonitor getMonitor();

        public void setSchema(SchemaDTO schema);

        public List<IndicatorDTO> getSelectedIndicators();

        public List<AdminEntityDTO> getAdminRestrictions();

        public Date getMinDate();

        public Date getMaxDate();
        
        public List<PartnerDTO> getPartnerRestrictions();
        
        public void setDimensionChecked(ModelData d, boolean checked);
        
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
        
        /*
		final ArrayList <ModelData> list = new ArrayList <ModelData> (4);        
		list.add(new Dimension(I18N.CONSTANTS.database(), DimensionType.Database));
		list.add(new Dimension(I18N.CONSTANTS.activity(), DimensionType.Activity));
		list.add(new Dimension(I18N.CONSTANTS.indicators(), DimensionType.Indicator));
		list.add(new Dimension(I18N.CONSTANTS.partner(), DimensionType.Partner));

		list.add(new DimensionFolder(I18N.CONSTANTS.geography(), DimensionGroup.Geography,0,0));
		list.add(new DimensionFolder(I18N.CONSTANTS.time(), DimensionGroup.Time,0,0));
		list.add(new DimensionFolder(I18N.CONSTANTS.attributes(), DimensionGroup.Attribute,0,0));
		
		TreeStore<ModelData> treeStore = view.getDimensionStore();
		treeStore.add(list, false);  

		view.setDimensionChecked(list.get(0), true);
		view.setDimensionChecked(list.get(1), true);
		view.setDimensionChecked(list.get(2), true);
		view.setDimensionChecked(list.get(3), true);
		*/
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
  
}
