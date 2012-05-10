package org.activityinfo.client.page.entry.form;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;
import org.activityinfo.shared.dto.SiteDTO;

import com.google.gwt.i18n.client.NumberFormat;

public class SiteRenderer {
	private NumberFormat indicatorFormat = NumberFormat.getFormat("#,###");
	
	public String renderSite(SiteDTO site, ActivityDTO activity, boolean showEmptyRows, boolean renderComments) {
        StringBuilder html = new StringBuilder();

        if(renderComments && site.getComments() != null) {
            String commentsHtml = site.getComments();
            commentsHtml = commentsHtml.replace("\n", "<br/>");
            html.append("<p class='comments'><span class='groupName'>").append(I18N.CONSTANTS.comments()).append(":</span> ")
                    .append(commentsHtml).append("</p>");
        }

        for(AttributeGroupDTO group : activity.getAttributeGroups()) {
            renderAttribute(html, group, site);
        }

        html.append("<table class='indicatorTable' cellspacing='0'>");
        for(IndicatorGroup group : activity.groupIndicators())  {
            renderIndicatorGroup(html, group, site, showEmptyRows);
        }
        html.append("</table>");

        return html.toString();
	}
	
	
    private void renderIndicatorGroup(StringBuilder html, IndicatorGroup group, SiteDTO site, boolean showEmptyRows) {
        StringBuilder groupHtml = new StringBuilder();
        boolean empty = true;

        if(group.getName()!=null) {
            groupHtml.append("<tr><td class='indicatorGroupHeading'>").append(group.getName()).
                    append("</td><td>&nbsp;</td></tr>");
        }
        for(IndicatorDTO indicator : group.getIndicators()) {

            Double value;
            if(indicator.getAggregation() == IndicatorDTO.AGGREGATE_SITE_COUNT) {
                value = 1.0;
            } else {
                value = site.getIndicatorValue(indicator);
            }

            if(showEmptyRows ||
                    (value != null &&
                            (indicator.getAggregation() != IndicatorDTO.AGGREGATE_SUM || value != 0))) {

                groupHtml.append("<tr><td class='indicatorHeading");
                if(group.getName()!=null) {
                    groupHtml.append(" indicatorGroupChild");
                }

                groupHtml.append("'>").append(indicator.getName())
                         .append("</td><td class='indicatorValue'>")
                         .append(formatValue(indicator, value))
                         .append("</td><td class='indicatorUnits'>").append(indicator.getUnits())
                         .append("</td></tr>");
                empty = false;
            }
        }
        if(showEmptyRows || !empty) {
            html.append(groupHtml.toString());
        }
    }

    protected String formatValue(IndicatorDTO indicator, Double value) {
        if(value == null) {
            return "-";
        } else {
            return indicatorFormat.format(value);
        }
    }

    protected void renderAttribute(StringBuilder html, AttributeGroupDTO group, SiteDTO site) {
        int count = 0;
        if (group != null) {
		    for(AttributeDTO attribute : group.getAttributes()) {
		        Boolean value = site.getAttributeValue(attribute.getId());
		        if(value != null && value) {
		            if(count ==0 ) {
		                html.append("<p class='attribute'><span class='groupName'>")
		                    .append(group.getName())
		                    .append(": </span><span class='attValues'>");
		            } else {
		                html.append(", ");
		            }
		            html.append(attribute.getName());
		            count++;
		        }
		    }
		    if(count !=0) {
		        html.append("</span></p>");
		    }
        }
    }
}
