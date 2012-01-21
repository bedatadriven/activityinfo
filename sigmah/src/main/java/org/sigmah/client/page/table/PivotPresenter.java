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
import org.sigmah.client.page.common.SubscribeForm;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
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
    public static final PageId PAGE_ID = new PageId("pivot");

    @ImplementedBy(PivotPage.class)
    public interface View {

        void bindPresenter(PivotPresenter presenter);

        void enableUIAction(String actionId, boolean enabled);

        ListStore<Dimension> getRowStore();

        ListStore<Dimension> getColStore();
        
        TreeStore<ModelData> getDimensionStore();

        void setContent(PivotTableReportElement element);

        AsyncMonitor getMonitor();

        void setSchema(SchemaDTO schema);

        List<IndicatorDTO> getSelectedIndicators();

        List<AdminEntityDTO> getAdminRestrictions();

        Date getMinDate();

        Date getMaxDate();
        
        List<PartnerDTO> getPartnerRestrictions();
        
        void setDimensionChecked(ModelData d, boolean checked);
        PivotTableReportElement createElement();    
        void onRefresh();
    }

    private final Dispatcher service;
    private final View view;
    private final EventBus eventBus;
    private SubscribeForm form;
    private FormDialogImpl dialog;

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
        
    public void onUIAction(String itemId) {
       if (UIActions.EXPORT.equals(itemId)) {
            service.execute(new RenderElement(view.createElement(), RenderElement.Format.Excel), view.getMonitor(),
                    new DownloadCallback(eventBus, "pivotTable"));
        } else if (UIActions.SUBSCRIBE.equals(itemId)) {
        	
        	form = new SubscribeForm();
        	
			dialog = new FormDialogImpl(form);
			dialog.setWidth(450);
			dialog.setHeight(400);
			dialog.setHeading(I18N.CONSTANTS.SubscribeTitle());
			
			dialog.show(new FormDialogCallback() {
				@Override
				public void onValidated() {
					if(form.validListField()){
						createReport();	
					} else{
						MessageBox.alert(I18N.CONSTANTS.error(), I18N.MESSAGES.noEmailAddress(), null);
					}
	            }
			});
        }
        
    }
    

    
    public void createReport(){
    	
    	final Report report = new Report();
    	report.addElement(view.createElement());
    	report.setDay(form.getDay());
    	report.setTitle(form.getTitle());
    	report.setFrequency(form.getReportFrequency());
		
		service.execute(new CreateReportDef(report), null, new AsyncCallback<CreateResult>() {
            public void onFailure(Throwable caught) {
            	dialog.onServerError();
            }

            public void onSuccess(CreateResult result) {
            	subscribeEmail(result.getNewId());
            }
        });
    	
    }

    public void subscribeEmail(int templateId){
    	
    	CreateSubscribe sub = new CreateSubscribe();
		sub.setReportTemplateId(templateId);
		sub.setEmailsList(form.getEmailList());
        
		service.execute(sub, null, new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {
            	dialog.onServerError();
            }

            public void onSuccess(VoidResult result) {
            	dialog.hide();
            }
        });
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
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
