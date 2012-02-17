package org.sigmah.client.page.report;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.report.resources.ReportResources;

import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;

public class ReportBar extends LayoutContainer {

	private ReportTitleWidget titleWidget;
	private Button saveButton;
	
	public ReportBar() {
		setStyleName(ReportResources.INSTANCE.style().bar());
		
		HBoxLayout layout = new HBoxLayout();
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPadding(new Padding(5));
		
		setLayout(layout);
		setMonitorWindowResize(true);
		
		addTitle();
		
		Button shareButton = new Button("Share");
		add(shareButton);
		
		Button exportButton = new Button(I18N.CONSTANTS.export(), IconImageBundle.ICONS.pdf());
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
}
