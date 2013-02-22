package org.activityinfo.client.page.entry.form;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.page.entry.form.resources.SiteFormResources;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;

import com.google.gwt.resources.client.TextResource;

public class PrintDataEntryForm {

    private StringBuilder html;
    private ActivityDTO activity;

    public PrintDataEntryForm(ActivityDTO activity) {

        super();
        this.activity = activity;
        init();
    }

    private void init() {

        String contents = getFormContents();

        contents = contents.replace("{$activityName}", activity.getName())
            .replace("{$databaseName}", activity.getDatabase().getName())
            .replace("{$activityName}", activity.getName())
            .replace("{$indicators}", addIndicators())
            .replace("{$attributes}", addAttributes());

        html = new StringBuilder();
        html.append(contents);
    }

    private String getFormContents() {
        TextResource formPage = SiteFormResources.INSTANCE.collectionForm();
        return formPage.getText();
    }

    public void print() {
        Print.it(html.toString());
    }

    private String addIndicators() {
        StringBuilder builder = new StringBuilder();

        builder
            .append("<table border=\"1px\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" class=\"form-detail\">");

        for (IndicatorGroup group : activity.groupIndicators()) {

            if (group.getName() != null) {
                builder
                    .append("<tr><td colspan='3'><h3 class='indicatorGroup'> "
                        + group.getName() + "</h3><td></tr>");
            }

            builder.append("<tr>");
            builder.append("<td>Indicator</td>");
            builder.append("<td>Valeur</td>");
            builder.append("<td>Units</td>");
            builder.append("</tr>");
            for (IndicatorDTO indicator : group.getIndicators()) {
                addIndicator(indicator, builder);
            }

        }

        builder.append("</table>");

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
