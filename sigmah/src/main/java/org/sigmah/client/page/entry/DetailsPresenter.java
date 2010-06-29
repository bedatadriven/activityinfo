package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.inject.Inject;
import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.page.common.Shutdownable;
import org.sigmah.shared.dto.*;
/*
 * @author Alex Bertram
 */

public class DetailsPresenter implements Shutdownable {

    public interface View {
        public void setHtml(String html);

        void setSelectionTitle(String title);
    }

    private final EventBus eventBus;
    private final ActivityDTO activity;
    private final UIConstants messages;
    private final View view;
    private final NumberFormat indicatorFormat;

    private SiteDTO currentSite;

    private boolean showEmptyRows = false;

    private Listener<SiteEvent> siteListener;

    @Inject
    public DetailsPresenter(EventBus eventBus, ActivityDTO activity, UIConstants messages, View view) {
        this.eventBus = eventBus;
        this.activity = activity;
        this.messages = messages;
        this.view = view;

        indicatorFormat = NumberFormat.getFormat("#,###");

        siteListener = new Listener<SiteEvent>() {
            public void handleEvent(SiteEvent be) {
                if(be.getType() == AppEvents.SiteSelected && be.getSite() != null) {
                    onSiteSelected(be.getSite());
                } else if(be.getType() == AppEvents.SiteChanged) {
                    if(currentSite.getId() == be.getSite().getId()) {
                        onSiteSelected(be.getSite());
                    }
                }
            }
        };
        eventBus.addListener(AppEvents.SiteSelected, siteListener);
        eventBus.addListener(AppEvents.SiteChanged, siteListener);
    }


    public void shutdown() {
        eventBus.removeListener(AppEvents.SiteSelected, siteListener);
        eventBus.removeListener(AppEvents.SiteChanged, siteListener);
    }

    private void onSiteSelected(SiteDTO site) {

        this.currentSite = site;
        view.setSelectionTitle(site.getLocationName());
        view.setHtml(renderSite(site));
    }

    protected String renderSite(SiteDTO site) {

        StringBuilder html = new StringBuilder();


        if(site.getComments() != null) {
            String commentsHtml = site.getComments();
            commentsHtml = commentsHtml.replace("\n", "<br/>");
            html.append("<p class='comments'><span class='groupName'>").append(messages.comments()).append(":</span> ")
                    .append(commentsHtml).append("</p>");
        }


        for(AttributeGroupDTO group : activity.getAttributeGroups()) {
            renderAttribute(html, group, site);
        }

        html.append("<table class='indicatorTable' cellspacing='0'>");
        for(IndicatorGroup group : activity.groupIndicators())  {
            renderIndicatorGroup(html, group, site);
        }
        html.append("</table>");

        return html.toString();
    }

    private void renderIndicatorGroup(StringBuilder html, IndicatorGroup group, SiteDTO site) {
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
        for(AttributeDTO attribute : group.getAttributes()) {
            Boolean value = site.getAttributeValue(attribute.getId());
            if(value != null && value) {
                if(count ==0 ) {
                    html.append("<p class='attribute'><span class='groupName'>")
                            .append(group.getName()).append(": </span><span class='attValues'>");
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
