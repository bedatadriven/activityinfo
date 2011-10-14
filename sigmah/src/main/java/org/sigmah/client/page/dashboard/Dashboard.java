package org.sigmah.client.page.dashboard;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class Dashboard extends Composite implements Page {
	public static final PageId DASHBOARD = new PageId("dashboard");
	private Dispatcher service;
	private com.google.gwt.user.client.ui.VerticalPanel verticalPanel;
	private ScrollPanel scrollPanel;
	private com.google.gwt.user.client.ui.HorizontalPanel horizontalPanel;
	private com.google.gwt.user.client.ui.VerticalPanel panelMain;
	private com.google.gwt.user.client.ui.VerticalPanel panelRight;
	private Block block_1;
	private WelcomeWidget welcomeWidget;
	private UpdateStream updateStream;
	private Block block_2;
	private SitesWithoutLocations sitesWithoutLocations;
	private Block block_report;
	private Reports reports;
	
	@Inject
	public Dashboard(Dispatcher service) { 
		super();
		this.service = service;
		{
			scrollPanel = new ScrollPanel();
			initWidget(scrollPanel);
			scrollPanel.setWidth("100%");
			verticalPanel = new com.google.gwt.user.client.ui.VerticalPanel();
			verticalPanel.setSize("100%", "auto");
			scrollPanel.add(verticalPanel);
			{
				welcomeWidget = new WelcomeWidget();
				verticalPanel.add(welcomeWidget);
			}
			{
				horizontalPanel = new com.google.gwt.user.client.ui.HorizontalPanel();
				verticalPanel.add(horizontalPanel);
				horizontalPanel.setWidth("100%");
				{
					panelMain = new com.google.gwt.user.client.ui.VerticalPanel();
					horizontalPanel.add(panelMain);
					panelMain.setWidth("100%");
					panelMain.setSpacing(10);
					panelMain.setStyleName("dashboard-mainpanel");
					horizontalPanel.setCellWidth(panelMain, "66%");
					{
						block_report = new Block();
						block_report.setTitle(I18N.CONSTANTS.reports());
						panelMain.add(block_report);
						{
							reports = new Reports(service);
							block_report.add(reports);
						}
						block_1 = new Block();
						block_1.setTitle(I18N.CONSTANTS.latestAddedEditedSites());
						panelMain.add(block_1);
						{
							updateStream = new UpdateStream(service);
							block_1.add(updateStream);
						}
					}
				}
				{
					panelRight = new com.google.gwt.user.client.ui.VerticalPanel();
					horizontalPanel.add(panelRight);
					panelRight.setWidth("100%");
					panelRight.setSpacing(10);
					panelRight.setStyleName("dashboard-rightpanel");
					horizontalPanel.setCellWidth(panelRight, "32%");
					{
						block_2 = new Block();
						block_2.setTitle(I18N.CONSTANTS.sitesWithoutCoordinates());
						panelRight.add(block_2);
						{
							sitesWithoutLocations = new SitesWithoutLocations(service);
							block_2.add(sitesWithoutLocations);
						}
					}
				}
			}
		}
	}
	
	
	private void welcomeHeader() {
	}


	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public PageId getPageId() {
		return Dashboard.DASHBOARD;
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	@Override
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}
}
