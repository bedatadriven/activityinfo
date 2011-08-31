package org.sigmah.client.page.dashboard;

import org.sigmah.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class WelcomeWidget extends LayoutContainer {

	public WelcomeWidget() {
		super();
	
		RowLayout rowLayout = new RowLayout();
		rowLayout.setOrientation(Orientation.HORIZONTAL);

		setLayout(rowLayout);
		setHeight(88);
		LabelField labelStartTitle = getLabel("Info about your activities.");

		LabelField labelBeginName = getLabel("Activity");
		LabelField labelEndName = getLabel("Info.");
		labelEndName.setStyleAttribute("font-weight", "bold");

		RowData rd = new RowData(-1, -1, new Margins(20, 20, 20, 20));
		add(IconImageBundle.ICONS.logo48().createImage(), rd);
		add(labelStartTitle, rd);
		add(labelBeginName, new RowData(-1, -1, new Margins(20, 0, 20, 0)));
		add(labelEndName, new RowData(-1, -1, new Margins(20, 0, 20, 0)));	
	}
	
	private LabelField getLabel(String text) {
		LabelField label = new LabelField(text);
		label.setStyleAttribute("font-size", "20px");
		return label;
	}

}