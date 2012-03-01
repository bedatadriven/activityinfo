package org.sigmah.client.page.dashboard.portlets;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.report.ReportDesignPageState;
import org.sigmah.client.report.view.ChartOFCView;
import org.sigmah.client.report.view.PivotGridPanel;
import org.sigmah.client.report.view.ReportView;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.GetReportModel;
import org.sigmah.shared.command.UpdateReportSubscription;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ReportMetadataDTO;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotTableReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReportPortlet extends Portlet {

	private final Dispatcher dispatcher;
	private final ReportMetadataDTO metadata;
	private EventBus eventBus;

	public ReportPortlet(Dispatcher dispatcher, EventBus eventBus, ReportMetadataDTO report) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		this.metadata = report;
		
		setHeading(report.getTitle());
		setHeight(275);
		setLayout(new FitLayout());
		
		addOptionsMenu();  
		addCloseButton();  
  
	
		add(new Label(I18N.CONSTANTS.loading()));
		
		loadModel();
	}

	private void addOptionsMenu() {
		final Menu optionsMenu = new Menu();
		
		optionsMenu.add(new MenuItem(I18N.CONSTANTS.edit(), IconImageBundle.ICONS.edit(), new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				edit();
			}
		}));
		
		optionsMenu.add(new MenuItem(I18N.CONSTANTS.removeFromDashboard(), IconImageBundle.ICONS.remove(), new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				removeFromDashboard();
			}
		}));
		
		final ToolButton gear = new ToolButton("x-tool-gear", new SelectionListener<IconButtonEvent>() {

			@Override
			public void componentSelected(IconButtonEvent ce) {
				optionsMenu.show(ce.getComponent());
			}
		});
		getHeader().addTool(gear);
	}
	
	private void addCloseButton() {
		getHeader().addTool(new ToolButton("x-tool-close", new SelectionListener<IconButtonEvent>() {  
			  
			@Override  
			public void componentSelected(IconButtonEvent ce) {  
				removeFromDashboard();
			}  
        }));
		
		getHeader().addTool(new ToolButton("x-tool-maximize", new SelectionListener<IconButtonEvent>() {
			
			@Override
			public void componentSelected(IconButtonEvent ce) {
				edit();
			}
		}));
	}


	private void loadModel() {
		dispatcher.execute(new GetReportModel(metadata.getId()), null, new AsyncCallback<Report>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Report result) {
				onModelLoad(result);
			}
		});
	}

	private void onModelLoad(Report result) {
		final ReportElement element = result.getElement(0);
		final ReportView view = createView(element);
		
		if(view == null) {
			removeAll();
			add(new Label("Unsupport report type"));
			layout();
			return;
		} 
		
		dispatcher.execute(new GenerateElement<Content>(element), null, new AsyncCallback<Content>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Content result) {
				element.setContent(result);
				view.show(element);
				removeAll();
				add(view.asComponent());
				layout();
			}
		});
	}

	private ReportView createView(ReportElement element) {
		if(element instanceof PivotChartReportElement) {
			return new ChartOFCView();
		} else if(element instanceof PivotTableReportElement) {
			PivotGridPanel gridPanel = new PivotGridPanel();
			gridPanel.setHeaderVisible(false);
			return gridPanel;
		} else {
			return null;
		}
	}
	

	private void edit() {
		eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested,
				new ReportDesignPageState(metadata.getId())));
	}
	
	private void removeFromDashboard() {
		MessageBox.confirm(metadata.getTitle(), I18N.CONSTANTS.confirmRemoveFromDashboard(), new Listener<MessageBoxEvent>() {
			
			@Override
			public void handleEvent(MessageBoxEvent be) {
				if(be.getButtonClicked().getItemId().equals(Dialog.YES)) {
					UpdateReportSubscription update = new UpdateReportSubscription();
					update.setReportId(metadata.getId());
					update.setPinnedToDashboard(false);
					
					dispatcher.execute(update, null, new AsyncCallback<VoidResult>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(VoidResult result) {
							removeFromParent();
						}
					});
				}
			}
		});
	}
}
