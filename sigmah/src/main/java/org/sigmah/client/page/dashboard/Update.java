package org.sigmah.client.page.dashboard;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.entry.editor.SiteRenderer;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.DateTimeFormat;

public class Update extends LayoutContainer {
	private SiteDTO site;
	private ActivityDTO activity;
	private final HorizontalPanel horizontalPanel = new HorizontalPanel();
	private static final IconImageBundle iconImageBundle = IconImageBundle.ICONS;
	private LabelField labelDatabase_1;
	private LabelField labelActivity;
	private LabelField labelDaysAgo;
	private Html htmlDetails;
	private LabelField labelLocation;
	private VerticalPanel panelAddEditIcon;
	private VerticalPanel panelHeaderContent;
	private HorizontalPanel panelHeader;
	private LayoutContainer layoutcontainerDetails;

	public Update(SiteDTO site, ActivityDTO activity) {
		super();
		setSize("auto", "auto");
		
		this.site = site;
		this.activity = activity;
		
		if (site.isEditedOneOrMoreTimes()) {
			setStyleAttribute("background", "#F5F6CE");
		} else {
			setStyleAttribute("background", "#D8F6CE");
		}
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		
		panelAddEditIcon = new VerticalPanel();
		panelAddEditIcon.setHorizontalAlign(HorizontalAlignment.CENTER);
		horizontalPanel_1.add(panelAddEditIcon);
		panelAddEditIcon.setWidth("42");
		
		panelHeaderContent = new VerticalPanel();
		
		panelHeader = new HorizontalPanel();
		panelHeader.setSpacing(5);
		
		panelHeader.add(iconImageBundle.location().createImage());
		
		labelLocation = new LabelField(site.getPrettyLocationName());
		panelHeader.add(labelLocation);
		
		panelHeader.add(iconImageBundle.database().createImage());
		labelDatabase_1 = new LabelField(activity.getDatabase().getName());
		panelHeader.add(labelDatabase_1);
		
		panelHeader.add(iconImageBundle.activity().createImage());
		labelActivity = new LabelField(activity.getName());
		panelHeader.add(labelActivity);
		
		panelHeader.add(iconImageBundle.time().createImage());
		labelDaysAgo = new LabelField(getHumanReadableDate());
		panelHeader.add(labelDaysAgo);
		panelHeaderContent.add(panelHeader);
		
		layoutcontainerDetails = new LayoutContainer();
		layoutcontainerDetails.setLayout(new FitLayout());
		
		htmlDetails = new Html();
		layoutcontainerDetails.add(htmlDetails);
		htmlDetails.setWidth("auto");
		panelHeaderContent.add(layoutcontainerDetails);
		horizontalPanel_1.add(panelHeaderContent);
		add(horizontalPanel_1);
		horizontalPanel_1.setWidth("auto");
		
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
			LabelField labelDetails = new LabelField(site.getComments());
			panelNotes.setStyleAttribute("background", "#F3F781");
			panelNotes.add(IconImageBundle.ICONS.note().createImage());
			panelNotes.add(labelDetails);
			panelNotes.setSpacing(5);
			layoutcontainerDetails.add(panelNotes);
		}
		SiteRenderer siteRenderer = new SiteRenderer();
		String htmlString = siteRenderer.renderSite(site, activity, false, false);
		
		htmlDetails.setHtml(htmlString);
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
