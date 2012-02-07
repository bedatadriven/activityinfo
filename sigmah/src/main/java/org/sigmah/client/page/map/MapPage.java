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
import org.sigmah.client.page.report.SubsciberUtil;
import org.sigmah.client.page.report.SubsciberUtil.Callback;
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
			
			SubsciberUtil subscriberUtil = new SubsciberUtil(dispatcher);
	    	subscriberUtil.showForm(aiMapWidget.getValue(), new Callback() {
				
				@Override
				public void onSuccess() {
				
				}
				
				@Override
				public void onFailure() {
					
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

