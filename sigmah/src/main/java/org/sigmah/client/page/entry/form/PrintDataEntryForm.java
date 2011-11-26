package org.sigmah.client.page.entry.form;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.entry.form.resources.SiteFormResources;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorGroup;
import org.sigmah.shared.dto.SchemaDTO;

import com.google.gwt.resources.client.TextResource;

public class PrintDataEntryForm {

	private final Dispatcher service;

	private SchemaDTO schemaDTO;

	private StringBuilder html;
	private ActivityDTO activity;

	public PrintDataEntryForm(ActivityDTO activity, Dispatcher service) {

		super();
		this.service = service;
		this.activity = activity;

		init();
	}

	private void init() {

		String contents = getFormContents();
		
		contents = contents.replace("{$activityName}", activity.getName())
				.replace("{$databaseName}", activity.getDatabase().getName())
				.replace("{$activityName}",activity.getName())
				.replace("{$partnerName}", " ")
				.replace("{$projectName}", " ")
				.replace("{$startDate}", "")
				.replace("{$endDate}", " ");
		
		html = new StringBuilder();
		
		html.append(contents);

		print();
	}

	private void addIndicators() {
		for (IndicatorGroup group : activity.groupIndicators()) {

			if (group.getName() != null) {
				html.append("<tr><td> <b> " + group.getName() + "</b></td><td></td></tr>");
			}

			for (IndicatorDTO indicator : group.getIndicators()) {
				addIndicator(indicator);
			}
		}
	}

	private void addIndicator(IndicatorDTO indicator) {
		html.append("<tr><td> <b> " + indicator.getName() + "</b></td><td></td></tr>");
		if (indicator.getDescription() != null && !indicator.getDescription().isEmpty()) {
			html.append("<tr><td> <b> " + indicator.getDescription() + "</b></td><td></td></tr>");
		}
		html.append("<tr><td> <b> " + indicator.getUnits() + "</b></td><td></td></tr>");
	}

	private void addAttributes() {

		for (AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {

			String group= AttributeCheckBoxGroup(attributeGroup);
			html.append("<tr><td>  " + group + "</td><td></td></tr>");
			
		}
	}

	private String AttributeCheckBoxGroup(AttributeGroupDTO group) {
		StringBuilder checkBoxGroup = new StringBuilder();
		for (AttributeDTO attrib : group.getAttributes()) {

			checkBoxGroup.append("<input type=\'checkbox\' value=\'" + attrib.getPropertyName() + "\'>");
			checkBoxGroup.append(attrib.getName() + "</input>");

		}
		return checkBoxGroup.toString();
	}
	
	private String getFormContents(){
		TextResource formPage = SiteFormResources.INSTANCE.collectionForm();
		 String text=	formPage.getText();
		return text;
	}

	private void print() {
		printInPopup(html.toString());
	}

	native void printInPopup(String body) /*-{
		OpenWindow = window.open("", "PrintForm",
				"height=650, width=800,toolbar=no,scrollbars=" + scroll
						+ ",menubar=no");
		OpenWindow.document.write(body)
		OpenWindow.document.write("<script>window.print();</script></body>")
	
		OpenWindow.document.close()
		self.name = "main"
	}-*/;

}
