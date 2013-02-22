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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.util.IndicatorNumberFormat;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;
import org.activityinfo.shared.dto.SiteDTO;

import com.google.common.base.Strings;

public class SiteRenderer {

    private IndicatorValueFormatter indicatorValueFormatter;

    public String renderLocation(SiteDTO site, ActivityDTO activity) {
        StringBuilder html = new StringBuilder();

        html.append("<table cellspacing='0'>");
        if (!activity.getLocationType().isAdminLevel()) {
            html.append("<tr><td>");
            html.append(activity.getLocationType().getName()).append(": ");
            html.append("</td><td>");
            html.append(site.getLocationName());
            if (!Strings.isNullOrEmpty(site.getLocationAxe())) {
                html.append("<br>").append(site.getLocationAxe());
            }
            html.append("</td></tr>");
        }
        for (AdminLevelDTO level : activity.getAdminLevels()) {
            AdminEntityDTO entity = site.getAdminEntity(level.getId());
            if (entity != null) {
                html.append("<tr><td>");
                html.append(level.getName()).append(":</td><td>");
                html.append(entity.getName());
                html.append("</td></tr>");
            }
        }

        html.append("</table>");
        return html.toString();
    }

    public String renderSite(SiteDTO site, ActivityDTO activity,
        boolean showEmptyRows, boolean renderComments) {
        StringBuilder html = new StringBuilder();

        if (renderComments && site.getComments() != null) {
            String commentsHtml = site.getComments();
            commentsHtml = commentsHtml.replace("\n", "<br/>");
            html.append("<p class='comments'><span class='groupName'>")
                .append(I18N.CONSTANTS.comments())
                .append(":</span> ")
                .append(commentsHtml)
                .append("</p>");
        }

        for (AttributeGroupDTO group : activity.getAttributeGroups()) {
            renderAttribute(html, group, site);
        }

        if (activity.getReportingFrequency() == ActivityDTO.REPORT_ONCE) {
            html.append(renderIndicators(site, activity, showEmptyRows));
        }

        return html.toString();
    }

    private String renderIndicators(SiteDTO site, ActivityDTO activity,
        boolean showEmptyRows) {
        StringBuilder html = new StringBuilder();
        html.append("<p><span class='groupName'>");
        html.append(I18N.CONSTANTS.indicators());
        html.append(":</p>");
        html.append("<table class='indicatorTable' cellspacing='0'>");
        boolean hasContent = false;
        for (IndicatorGroup group : activity.groupIndicators()) {
            hasContent = hasContent
                || renderIndicatorGroup(html, group, site, showEmptyRows);
        }
        html.append("</table>");

        return hasContent ? html.toString() : "";
    }

    private boolean renderIndicatorGroup(StringBuilder html,
        IndicatorGroup group, SiteDTO site, boolean showEmptyRows) {
        StringBuilder groupHtml = new StringBuilder();
        boolean empty = true;

        if (group.getName() != null) {
            groupHtml.append("<tr><td class='indicatorGroupHeading'>")
                .append(group.getName()).
                append("</td><td>&nbsp;</td></tr>");
        }
        for (IndicatorDTO indicator : group.getIndicators()) {

            Double value;
            if (indicator.getAggregation() == IndicatorDTO.AGGREGATE_SITE_COUNT) {
                value = 1.0;
            } else {
                value = site.getIndicatorValue(indicator);
            }

            if (showEmptyRows
                ||
                (value != null &&
                (indicator.getAggregation() != IndicatorDTO.AGGREGATE_SUM || value != 0))) {

                groupHtml.append("<tr><td class='indicatorHeading");
                if (group.getName() != null) {
                    groupHtml.append(" indicatorGroupChild");
                }

                groupHtml.append("'>").append(indicator.getName())
                    .append("</td><td class='indicatorValue'>")
                    .append(formatValue(indicator, value))
                    .append("</td><td class='indicatorUnits'>")
                    .append(indicator.getUnits())
                    .append("</td></tr>");
                empty = false;
            }
        }
        if (showEmptyRows || !empty) {
            html.append(groupHtml.toString());
            return true;
        } else {
            return false;
        }
    }

    protected String formatValue(IndicatorDTO indicator, Double value) {
        if (value == null) {
            return "-";
        } else {
            if (indicatorValueFormatter != null) {
                return indicatorValueFormatter.format(value);
            } else {
                return IndicatorNumberFormat.INSTANCE.format(value);
            }
        }
    }

    protected void renderAttribute(StringBuilder html, AttributeGroupDTO group,
        SiteDTO site) {
        int count = 0;
        if (group != null) {
            for (AttributeDTO attribute : group.getAttributes()) {
                Boolean value = site.getAttributeValue(attribute.getId());
                if (value != null && value) {
                    if (count == 0) {
                        html.append(
                            "<p class='attribute'><span class='groupName'>")
                            .append(group.getName())
                            .append(": </span><span class='attValues'>");
                    } else {
                        html.append(", ");
                    }
                    html.append(attribute.getName());
                    count++;
                }
            }
            if (count != 0) {
                html.append("</span></p>");
            }
        }
    }

    public void setIndicatorValueFormatter(
        IndicatorValueFormatter indicatorValueFormatter2) {
        this.indicatorValueFormatter = indicatorValueFormatter2;
    }

    public static interface IndicatorValueFormatter {
        String format(Double value);
    }
}
