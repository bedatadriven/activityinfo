package org.sigmah.client.page.dashboard;

import java.util.Date;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.editor.SiteRenderer;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSitesWithoutCoordinates;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

public class UpdateStream extends AiPortlet {
	private class Update extends DockPanel {
		private SiteDTO site;
		private ActivityDTO activity;
		private DockPanel header = new DockPanel();
		private VerticalPanel panelContent = new VerticalPanel();
		private VerticalPanel panelDetails = new VerticalPanel();

		public Update(SiteDTO site, ActivityDTO activity) {
			super();
			
			this.site = site;
			this.activity = activity;
			
			initialize();
			
			createAddOrEditIcon();  // West on root
			
			createDatabaseIconAndLabel(); // West on header
			createActivityIconAndLabel(); // West on header
			createEditAddedDateLabel();   // East on header
			createDetails();              // 
		}

		private void createDetails() {
			if (site.getComments() != null && !site.getComments().isEmpty()) {
				Label labelDetails = new Label(site.getComments());
				labelDetails.setStyleAttribute("background", "#F3F781");
				add(labelDetails);
			}
			SiteRenderer siteRenderer = new SiteRenderer();
			String htmlString = siteRenderer.renderSite(site, activity, false);
			Html html = new Html(htmlString);
	        html.setStyleName("details");
			panelContent.add(html);
		}

		private void createEditAddedDateLabel() {
			Date date = site.isEditedOneOrMoreTimes() ? site.getDateEdited() : site.getDateCreated();
			Label labelDate = createLabel(date.toString());
			labelDate.setStyleAttribute("font-size", "10px");
			labelDate.setStyleAttribute("color", "grey");
			header.add(labelDate, DockPanel.EAST);
		}

		private void createActivityIconAndLabel() {
			header.add(IconImageBundle.ICONS.activity().createImage(), DockPanel.WEST);
			header.add(createLabel(activity.getName()), DockPanel.WEST);
		}

		private void createDatabaseIconAndLabel() {
			header.add(IconImageBundle.ICONS.database().createImage(), DockPanel.WEST);
			header.add(createLabel(activity.getDatabase().getName()), DockPanel.WEST);
		}

		private void createAddOrEditIcon() {
			if (site.isEditedOneOrMoreTimes()) {
				add(IconImageBundle.ICONS.edit().createImage(), DockPanel.WEST);
			} else {
				add(IconImageBundle.ICONS.add().createImage(), DockPanel.WEST);
			}
		}
		
		private Label createLabel(String text) {
			Label label = new Label(text);
			label.setStyleAttribute("font-size", "12px");
			return label;
		}

		private void initialize() {
			add(panelContent, DockPanel.CENTER);
			panelContent.setWidth("100%");
			panelContent.add(header);
			panelContent.add(panelDetails);
			panelContent.setLayout(new FitLayout());
		}
	}

	private SchemaDTO schema;
	private EventBus eventBus;

	public UpdateStream(Dispatcher service) {
		super(service, "Recently updated sites");
		
		setAutoHeight(true);
		setAutoWidth(true);
		
		setLayout(new FitLayout());
		
		getSchema();
	}
	
	private void getSchema() {
		service.execute(new GetSchema(), loadingMonitor, new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO: handle failure
				System.out.println();
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				UpdateStream.this.schema=result;
				UpdateStream.this.getSites();
			}
		});
	}

	private void getSites() {
		service.execute(new GetSitesWithoutCoordinates(), loadingMonitor, new AsyncCallback<SiteResult>() {
			@Override
			public void onFailure(Throwable caught) {
				//TODO: handle failure
			}
			@Override
			public void onSuccess(SiteResult result) {
				for (SiteDTO site : result.getData()) {
					add(new Update(site, schema.getActivityById(site.getActivityId())), new RowData(-1,-1,new Margins(5)));
				}
				layout(true);
			}
		});
	}
}

