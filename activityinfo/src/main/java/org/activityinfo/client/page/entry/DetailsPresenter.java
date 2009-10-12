package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.page.common.Shutdownable;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.i18n.UIConstants;
/*
 * @author Alex Bertram
 */

public class DetailsPresenter implements Shutdownable {

    public interface View {
        public void setHtml(String html);

        void setSelectionTitle(String title);
    }

    private final EventBus eventBus;
    private final ActivityModel activity;
    private final UIConstants messages;
    private final View view;
    private final NumberFormat indicatorFormat;

    private SiteModel currentSite;

    private boolean showEmptyRows = false;

    private Listener<SiteEvent> siteListener;

    @Inject
    public DetailsPresenter(EventBus eventBus, ActivityModel activity, UIConstants messages, View view) {
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

    private void onSiteSelected(SiteModel site) {

        this.currentSite = site;
        view.setSelectionTitle(site.getLocationName());
        view.setHtml(renderSite(site));
    }

    protected String renderSite(SiteModel site) {

        StringBuilder html = new StringBuilder();


        if(site.getComments() != null) {
            String commentsHtml = site.getComments();
            commentsHtml = commentsHtml.replace("\n", "<br/>");
            html.append("<p class='comments'><span class='groupName'>").append(messages.comments()).append(":</span> ")
                    .append(commentsHtml).append("</p>");
        }


        for(AttributeGroupModel group : activity.getAttributeGroups()) {
            renderAttribute(html, group, site);
        }

        html.append("<table class='indicatorTable' cellspacing='0'>");
        for(IndicatorGroup group : activity.groupIndicators())  {
            renderIndicatorGroup(html, group, site);
        }
        html.append("</table>");

        return html.toString();
    }

    private void renderIndicatorGroup(StringBuilder html, IndicatorGroup group, SiteModel site) {
        StringBuilder groupHtml = new StringBuilder();
        boolean empty = true;

        if(group.getName()!=null) {
            groupHtml.append("<tr><td class='indicatorGroupHeading'>").append(group.getName()).
                    append("</td><td>&nbsp;</td></tr>");
        }
        for(IndicatorModel indicator : group.getIndicators()) {

            Double value;
            if(indicator.getAggregation() == IndicatorModel.AGGREGATE_SITE_COUNT)
                value = 1.0;
            else
                value = site.getIndicatorValue(indicator);

            if(showEmptyRows ||
                    (value != null &&
                            (indicator.getAggregation() != IndicatorModel.AGGREGATE_SUM || value != 0))) {

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

    protected String formatValue(IndicatorModel indicator, Double value) {
        if(value == null) {
            return "-";
        } else {
            return indicatorFormat.format(value);
        }
    }

    protected void renderAttribute(StringBuilder html, AttributeGroupModel group, SiteModel site) {
        int count = 0;
        for(AttributeModel attribute : group.getAttributes()) {
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
