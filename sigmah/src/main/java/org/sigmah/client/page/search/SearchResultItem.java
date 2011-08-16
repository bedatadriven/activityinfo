package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.i18n.I18N;
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
	
	public SearchResultItem() {
		super();

		initializeComponent();
		SearchResources.INSTANCE.searchStyles().ensureInjected();

		addDatabaseIcon();
		addDatabaseLabel();
		
		add(panelTop);
		add(panelChilds);
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
			HorizontalPanel panelChild = new HorizontalPanel();

			HorizontalPanel spacer = new HorizontalPanel();
			spacer.setWidth(20);
			panelChild.add(spacer);
			Image image = IconImageBundle.ICONS.activity().createImage();
			panelChild.add(image);
			
			Hyperlink link = new Hyperlink(axis.getLabel(),
					"site-grid/" + ((EntityCategory)axis.getCategory()).getId());
			link.setStylePrimaryName("link");
			panelChild.add(link);
			if (axis.getCells().size() > 0) {
				panelChild.add(new LabelField(
						axis.getCells().values().iterator().next().getValue().toString() + I18N.CONSTANTS.sites()));
			}
			
			panelChilds.add(panelChild);
		}
	}
}
