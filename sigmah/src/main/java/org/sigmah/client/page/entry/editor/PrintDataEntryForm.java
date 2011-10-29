package org.sigmah.client.page.entry.editor;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorGroup;
import org.sigmah.shared.dto.SchemaDTO;

public class PrintDataEntryForm {

	private final EventBus eventBus;
	private final Dispatcher service;

	private SchemaDTO schemaDTO;

	private StringBuilder html;
	private ActivityDTO activity;

	public PrintDataEntryForm(ActivityDTO activity, EventBus eventBus, Dispatcher service) {

		super();
		this.eventBus = eventBus;
		this.service = service;
		this.activity = activity;

		init();
	}

	private void init() {

		html = new StringBuilder();
		html.append("<h1>" + activity.getName() + "</h1>");
		html.append("<table> ");
		html.append("<tr><td>Database :</td><td>" + activity.getDatabase().getName() + "</td></tr>");
		html.append("<tr><td>Activity :</td><td>" + activity.getName() + "</td></tr>");
		html.append("<tr><td>Partner :</td><td>" + activity.getName() + "</td></tr>");

		html.append("<tr><td>Start Date :</td><td></td></tr>");
		html.append("<tr><td>End Date :</td><td>" + activity.getDatabase().getName() + "</td></tr>");
		html.append("<tr><td>Project :</td><td></td></tr>");
		html.append("<tr><td>Location :</td><td></td></tr>");
		//
		// 4. place
		// - field for each admin level
		// - location name
		// - Lat/Lng
		// 5. Attributes
		// 6. Indicators

		html.append("<tr><td>Attributes :</td><td></td></tr>");
		addAttributes();
		html.append("<tr><td>Indicators :</td><td></td></tr>");
		addIndicators();

		html.append("<table>");

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

	private void print() {
		printInPopup(html.toString());
	}

	native void printInPopup(String body) /*-{
		OpenWindow = window.open("", "PrintForm",
				"height=650, width=800,toolbar=no,scrollbars=" + scroll
						+ ",menubar=no");
		OpenWindow.document.write("<html><head>")
		OpenWindow.document
				.write("<title>Print Data Entry Form</title></head>")
		OpenWindow.document.write("<body bgcolor=white>")
		OpenWindow.document.write(body)
		OpenWindow.document.write("<script>window.print();</script></body>")
		OpenWindow.document.write("</html>")

		OpenWindow.document.close()
		self.name = "main"
	}-*/;

}
