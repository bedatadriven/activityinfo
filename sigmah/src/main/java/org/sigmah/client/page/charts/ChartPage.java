/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;
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
import org.sigmah.client.page.report.editor.PivotEditor;
import org.sigmah.shared.command.CreateReportDef;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.report.model.Report;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 
 * Page that allows the user to build and view a PivotChartElement
 * 
 * @author Alex Bertram
 */
public class ChartPage extends AbstractChart implements Page {

	public static final PageId PAGE_ID = new PageId("charts");

	@Inject
	public ChartPage(EventBus eventBus, Dispatcher service) {
		super(eventBus, service);
		
		addToolBarButtons();
	}

	private void addToolBarButtons() {
		toolBar.add(new ExportMenuButton(RenderElement.Format.PowerPoint)
				.withPowerPoint().withWord().withPdf().withPng()
				.callbackTo(new ExportCallback() {
					@Override
					public void export(RenderElement.Format format) {
						ChartPage.this.export(format);
					}
				}));

		toolBar.addButton(UIActions.SUBSCRIBE,
				I18N.CONSTANTS.emailSubscription(),
				IconImageBundle.ICONS.email());
	}

	@Override
	public void onUIAction(String actionId) {
		super.onUIAction(actionId);
		
		if (UIActions.SUBSCRIBE.equals(actionId)) {
			createSubscribeDialog();
		}
	}

	public void createSubscribeDialog() {

		SubsciberUtil subscriberUtil = new SubsciberUtil(service);
    	subscriberUtil.showForm(getChartElement(), new Callback() {
			
			@Override
			public void onSuccess() {
			
			}
			
			@Override
			public void onFailure() {
				
			}
		});
    	
	}

	protected void export(RenderElement.Format format) {
		service.execute(new RenderElement(getChartElement(), format), null,
				new DownloadCallback(eventBus));
	}
	
	@Override
	public void shutdown() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return true;
	}

}
