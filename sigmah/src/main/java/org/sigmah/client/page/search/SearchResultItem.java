package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.report.content.EntityCategory;
import org.sigmah.shared.report.content.PivotTableData.Axis;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;

public class SearchResultItem  extends LayoutContainer {
	private LabelField labelDatabaseName;
	private HorizontalPanel panelTop = new HorizontalPanel();
	private VerticalPanel panelChilds = new VerticalPanel();
	private int activityCount = 0;
	private int indicatorCount = 0;
	
	public SearchResultItem() {
		super();

		initializeComponent();
		SearchResources.INSTANCE.searchStyles().ensureInjected();

		addDatabaseIcon();
		addDatabaseLabel();
		
		add(panelTop);
		add(panelChilds);
	}


	public int getActivityCount() {
		return activityCount;
	}


	public int getIndicatorCount() {
		return indicatorCount;
	}


	private void addDatabaseIcon() {
		Image imageDatabase = IconImageBundle.ICONS.database().createImage();
		panelTop.add(imageDatabase);
	}


	private void initializeComponent() {
	}


	private void addDatabaseLabel() {
		labelDatabaseName = new LabelField();
		panelTop.add(labelDatabaseName);
	}


	public void setDabaseName(String databaseName) {
		labelDatabaseName.setValue(databaseName);
	}


	public void setChilds(List<Axis> childList) {
		for (Axis axis : childList) {
			VerticalPanel panelAll = new VerticalPanel();
			HorizontalPanel panelChild = new HorizontalPanel();

			HorizontalPanel spacer = new HorizontalPanel();
			spacer.setWidth(20);
			panelChild.add(spacer);
			Image image = IconImageBundle.ICONS.activity().createImage();
			panelChild.add(image);
			panelAll.add(panelChild);
			
			Hyperlink link = new Hyperlink(axis.getLabel(),
					"site-grid/" + ((EntityCategory)axis.getCategory()).getId());
			link.setStylePrimaryName("link");
			panelChild.add(link);
			
			for (Axis childAxis : axis.getChildren()) {
				HorizontalPanel panelIndicator = new HorizontalPanel();

				HorizontalPanel spacerIndicator = new HorizontalPanel();
				spacerIndicator.setWidth(40);
				panelIndicator.add(spacerIndicator);
				panelIndicator.add(IconImageBundle.ICONS.indicator().createImage());
				
//				Hyperlink linkIndicator = new Hyperlink(childAxis.getLabel(),
//						"site-grid/" + ((EntityCategory)childAxis.getCategory()).getId());
//				linkIndicator.setStylePrimaryName("link");
//				panelIndicator.add(linkIndicator);
				
				LabelField labelIndicator = new LabelField(childAxis.getLabel());
				panelIndicator.add(labelIndicator);
				
				panelAll.add(panelIndicator);
				indicatorCount++;
			}
			
			activityCount++;
			panelChilds.add(panelAll);
		}
	}
}
