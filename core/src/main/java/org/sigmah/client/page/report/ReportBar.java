package org.sigmah.client.page.report;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ExportMenuButton;
import org.sigmah.client.page.report.resources.ReportResources;
import org.sigmah.shared.command.RenderElement.Format;

import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;

public class ReportBar extends LayoutContainer {

	private ReportTitleWidget titleWidget;
	private Button saveButton;
	private ToggleButton dashboardButton; 
	private ExportMenuButton exportButton;
	
	public ReportBar() {
		setStyleName(ReportResources.INSTANCE.style().bar());
		
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPadding(new Padding(5));
		
		setLayout(layout);
		setMonitorWindowResize(true);
		
		addTitle();
		
		dashboardButton = new ToggleButton("Pin to dashboard");
		add(dashboardButton);
		
		Button shareButton = new Button("Share");
		add(shareButton);
		
		exportButton = new ExportMenuButton();
		add(exportButton);
		
		saveButton = new Button(I18N.CONSTANTS.save(),  IconImageBundle.ICONS.save());
		add(saveButton);
		
	}

	private void addTitle() {
		
		titleWidget = new ReportTitleWidget();
		
		HBoxLayoutData titleLayout = new HBoxLayoutData(0, 0, 0, 7);
		titleLayout.setFlex(1);
		
		add(titleWidget, titleLayout);
		
	}
	
	public void addTitleEditCompleteListener(Listener<EditorEvent> listener) {
		titleWidget.addEditCompleteListener(listener);
	}

	public void setReportTitle(String value) {
		titleWidget.setText(value);
	}
	
	public Button getSaveButton() {
		return saveButton;
	}
	
	public ToggleButton getDashboardButton() {
		return dashboardButton;
	}

	public ExportMenuButton getExportButton() {
		return exportButton;
		
	}
}
