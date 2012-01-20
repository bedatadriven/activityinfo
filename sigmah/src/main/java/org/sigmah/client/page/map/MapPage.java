/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.DownloadCallback;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.SubscribeForm;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.common.toolbar.ExportMenuButton;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.RenderElement.Format;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.Report;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * Displays a map where the user can interactively create a map
 */
public class MapPage extends AbstractMap implements Page, ExportCallback {

	public static final PageId PAGE_ID = new PageId("maps");

    private SubscribeForm form;
    private FormDialogImpl dialog;

    @Inject
    public MapPage(Dispatcher dispatcher, EventBus eventBus) {
    	super(dispatcher, eventBus)	;

    	createToolBarButtons();
    }

 
    private void createToolBarButtons() {
         exportMenu = new ExportMenuButton(RenderElement.Format.PowerPoint)
        	.withPowerPoint()
        	.withWord()
        	.withPdf()
        	.withPng()
        	.callbackTo(this);
        exportMenu.setEnabled(false);
		toolbarMapActions.add(exportMenu);
        toolbarMapActions.addButton(UIActions.EXPORT_DATA, I18N.CONSTANTS.exportData(),
                IconImageBundle.ICONS.excel());
        toolbarMapActions.setActionEnabled(UIActions.EXPORT_DATA, false);
        toolbarMapActions.addButton(UIActions.SUBSCRIBE, I18N.CONSTANTS.emailSubscription(), IconImageBundle.ICONS.email());
        
      
    }


	@Override
	public void shutdown() {		
		layersWidget.shutdown();
	}

	@Override
	public void onUIAction(String actionId) {
		super.onUIAction(actionId);
		
		if (actionId.equals(UIActions.EXPORT_DATA)) {
			export(Format.Excel_Data);
		} else if (actionId.equals(UIActions.SUBSCRIBE)) {
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

	@Override
	public void export(Format format) {
		RenderElement renderElement = new RenderElement();
		renderElement.setElement(aiMapWidget.getValue());
		renderElement.setFormat(format);
		dispatcher.execute(renderElement, null, new DownloadCallback(eventBus));
	}

	public void createReport(){
    	
    	final Report report = new Report();
    	report.addElement(aiMapWidget.getValue());
    	report.setDay(form.getDay());
    	report.setTitle(form.getTitle());
    	report.setFrequency(form.getReportFrequency());
		
    	dispatcher.execute(new CreateReportDef(report), null, new AsyncCallback<CreateResult>() {
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
        
		dispatcher.execute(sub, null, new AsyncCallback<VoidResult>() {
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
		return this;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return true;
	}

}

