package org.sigmah.client.page.dashboard;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.editor.SiteRenderer;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Update extends VerticalPanel {
	private SiteDTO site;
	private ActivityDTO activity;
	private static final IconImageBundle iconImageBundle = IconImageBundle.ICONS;
	private Label labelDatabase_1;
	private Label labelPartner;
	private Label labelActivity;
	private Label labelDaysAgo;
	private HTML htmlDetails;
	private Label labelLocation;
	private VerticalPanel panelAddEditIcon;
	private VerticalPanel panelHeaderContent;
	private HorizontalPanel panelHeader;
	private VerticalPanel layoutcontainerDetails;

	public Update(SiteDTO site, ActivityDTO activity) {
		super();
		setStyleName("dashboard-update-mainpanel");
		setSize("100%", "auto");
		
		this.site = site;
		this.activity = activity;
		
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		if (site.isEditedOneOrMoreTimes()) {
			horizontalPanel_1.setStyleName("dashboard-update-edit");
		} else {
			horizontalPanel_1.setStyleName("dashboard-update-add");
		}		
		
		panelAddEditIcon = new VerticalPanel();
		horizontalPanel_1.add(panelAddEditIcon);
		panelAddEditIcon.setWidth("42");
		
		panelHeaderContent = new VerticalPanel();
		
		panelHeader = new HorizontalPanel();
		panelHeader.setSpacing(5);
		
		panelHeader.add(iconImageBundle.location().createImage());
		
		labelLocation = new Label(site.getPrettyLocationName());
		panelHeader.add(labelLocation);
		
		panelHeader.add(iconImageBundle.database().createImage());
		labelDatabase_1 = new Label(activity.getDatabase().getName());
		panelHeader.add(labelDatabase_1);
		
		panelHeader.add(iconImageBundle.activity().createImage());
		labelActivity = new Label(activity.getName());
		panelHeader.add(labelActivity);
		
		panelHeader.add(iconImageBundle.partner().createImage());
		labelPartner = new Label(site.getPartnerName());
		panelHeader.add(labelPartner);
		
		panelHeader.add(iconImageBundle.time().createImage());
		labelDaysAgo = new Label(getHumanReadableDate());
		panelHeader.add(labelDaysAgo);
		panelHeaderContent.add(panelHeader);
		
		layoutcontainerDetails = new VerticalPanel();
		
		htmlDetails = new HTML();
		layoutcontainerDetails.add(htmlDetails);
		htmlDetails.setWidth("auto");
		panelHeaderContent.add(layoutcontainerDetails);
		horizontalPanel_1.add(panelHeaderContent);
		add(horizontalPanel_1);
		horizontalPanel_1.setWidth("100%");
		
		createAddOrEditIcon(); 
		createDetails();
	}
	
	/**
	 * Returns date in format of "[edited/added] X days ago
	 */
	private String getHumanReadableDate() {
		if (site.isEditedOneOrMoreTimes()) {
			String date = DateTimeFormat.getFormat("yyyy-MMM-dd").format(site.getDateEdited());
			return I18N.MESSAGES.edited(date);
		} else {
			String date = DateTimeFormat.getFormat("yyyy-MMM-dd").format(site.getDateCreated());
			return I18N.MESSAGES.added(date);
		}
	}

	private void createDetails() {
		if (site.getComments() != null && !site.getComments().isEmpty()) {
			HorizontalPanel panelNotes = new HorizontalPanel();
			Label labelDetails = new Label(site.getComments());
			//panelNotes.setStyleAttribute("background", "#F3F781");
			panelNotes.add(IconImageBundle.ICONS.note().createImage());
			panelNotes.add(labelDetails);
			panelNotes.setSpacing(5);
			layoutcontainerDetails.add(panelNotes);
		}
		SiteRenderer siteRenderer = new SiteRenderer();
		String htmlString = siteRenderer.renderSite(site, activity, false, false);
		
		htmlDetails.setHTML(htmlString);
		htmlDetails.setStyleName("details");
	}

	private void createAddOrEditIcon() {
		if (site.isEditedOneOrMoreTimes()) {
			panelAddEditIcon.add(iconImageBundle.edit32().createImage());
		} else {
			panelAddEditIcon.add(iconImageBundle.add32().createImage());
		}
	}
	
}
