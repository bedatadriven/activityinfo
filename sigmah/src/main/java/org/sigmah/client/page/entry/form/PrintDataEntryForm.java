package org.sigmah.client.page.entry.form;

import org.sigmah.client.page.entry.form.resources.SiteFormResources;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorGroup;
import com.google.gwt.resources.client.TextResource;

public class PrintDataEntryForm {

	private StringBuilder html;
	private ActivityDTO activity;

	public PrintDataEntryForm(ActivityDTO activity) {

		super();
		this.activity = activity;
		init();
		print();
	}

	private void init() {

		String contents = getFormContents();

		contents = contents.replace("{$activityName}", activity.getName())
				.replace("{$databaseName}", activity.getDatabase().getName())
				.replace("{$activityName}", activity.getName())
				.replace("{$partnerName}", " ").replace("{$projectName}", " ")
				.replace("{$startDate}", "").replace("{$endDate}", " ")
				.replace("{$indicators}", addIndicators())
				.replace("{$attributes}", addAttributes());

		html = new StringBuilder();
		html.append(contents);
	}

	private String getFormContents() {
		TextResource formPage = SiteFormResources.INSTANCE.collectionForm();
		return formPage.getText();
	}

	private void print() {
		Print.it(html.toString());
	}

	private String addIndicators() {
		StringBuilder builder = new StringBuilder();
		for (IndicatorGroup group : activity.groupIndicators()) {

			if (group.getName() != null) {
				builder.append("<h2> " + group.getName() + "</h2>");
			}
			builder.append("<table border=\"1px\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" class=\"form-detail\">");
			builder.append("<tr>");
			builder.append("<td>Indicator</td>");
			builder.append("<td>Valeur</td>");
			builder.append("<td>Units</td>");
			builder.append("</tr>");
			for (IndicatorDTO indicator : group.getIndicators()) {
				addIndicator(indicator, builder);
			}
			builder.append("</table>");
		}

		return builder.toString();
	}

	private void addIndicator(IndicatorDTO indicator, StringBuilder builder) {
		builder.append("<tr>");
		builder.append("<td>" + indicator.getName() + "</td>");
		builder.append("<td>&nbsp;</td>");
		builder.append("<td>" + indicator.getUnits() + "</td>");
		builder.append("</tr>");
	}

	private String addAttributes() {

		StringBuilder builder = new StringBuilder();
		for (AttributeGroupDTO attributeGroup : activity.getAttributeGroups()) {

			builder.append("<tr>");
			builder.append("<td id=\"field-set\" valign=\"top\">"
					+ attributeGroup.getName() + ":</td><td>");

			attributeCheckBoxGroup(attributeGroup, builder);
			builder.append("</td></tr>");
		}
		return builder.toString();
	}

	private void attributeCheckBoxGroup(AttributeGroupDTO group,
			StringBuilder builder) {

		for (AttributeDTO attribture : group.getAttributes()) {
			builder.append("[  ] " + attribture.getName() + "<br />");
		}

	}

}
