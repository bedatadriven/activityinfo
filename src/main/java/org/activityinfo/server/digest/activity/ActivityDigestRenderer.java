package org.activityinfo.server.digest.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.digest.DigestDateUtil;
import org.activityinfo.server.digest.DigestModel;
import org.activityinfo.server.digest.DigestRenderer;
import org.activityinfo.server.digest.activity.ActivityDigestModel.ActivityMap;
import org.activityinfo.server.digest.activity.ActivityDigestModel.DatabaseModel;
import org.activityinfo.server.digest.activity.ActivityDigestModel.PartnerActivityModel;
import org.ocpsoft.prettytime.PrettyTime;

import com.teklabs.gwt.i18n.server.LocaleProxy;

public class ActivityDigestRenderer implements DigestRenderer {
    private static final int HEADERCELL_WIDTH = 230;
    private static final int SPACERCELL_WIDTH = 20;
    private static final int GRAPHCELL_WIDTH = 21;
    private static final int ROW_HEIGHT = 21;

    private static final boolean RENDER_INACTIVE_ROWS = false;

    private static final int IA_DBS_HEADERCELL_WIDTH = 240;
    private static final int IA_DBS_CONTENTCELL_WIDTH = 400;

    @Override
    public String renderHtml(DigestModel model) throws IOException {
        assert (model instanceof ActivityDigestModel);

        StringBuilder html = new StringBuilder();
        html.append("<div id='act-digest' style='margin-top:20px'>");

        renderHeader(html, (ActivityDigestModel) model);

        renderActiveDatabases(html, (ActivityDigestModel) model);

        renderInactiveDatabases(html, (ActivityDigestModel) model);

        html.append("</div>");
        return html.toString();
    }

    private void renderHeader(StringBuilder html, ActivityDigestModel model) {
        html.append("<div class='act-header'>");
        html.append(I18N.MESSAGES.activityDigestIntro(model.getDays()));
        html.append("<br/>");
        html.append(I18N.MESSAGES.digestUnsubscribe());
        html.append("</div>");
    }

    private void renderActiveDatabases(StringBuilder html, ActivityDigestModel model) throws IOException {
        html.append("<div class='act-active'>");
        html.append("<table class='act-data' border='0' cellpadding='0' cellspacing='0' style='width:");
        html.append(HEADERCELL_WIDTH + SPACERCELL_WIDTH + (model.getDays() * GRAPHCELL_WIDTH));
        html.append("px; border-collapse:collapse;'>");

        renderTableHeader(html, model);

        Collection<DatabaseModel> activeDatabases = model.getActiveDatabases();
        for (DatabaseModel activeDatabase : activeDatabases) {
            renderActiveDatabase(html, activeDatabase);
        }

        html.append("</table>");
        html.append("</div>");
    }

    private void renderTableHeader(StringBuilder html, ActivityDigestModel model) {
        html.append("<tr>");
        html.append("<th style='width:").append(HEADERCELL_WIDTH).append("px;'>&nbsp;</th>");
        html.append("<th style='width:").append(SPACERCELL_WIDTH).append("px;'>&nbsp;</th>");
        for (int i = 0; i < model.getDays(); i++) {
            html.append("<th style='width:").append(GRAPHCELL_WIDTH).append("px;'>&nbsp;</th>");
        }
        html.append("</tr>");
    }

    private void renderActiveDatabase(StringBuilder html, DatabaseModel activeDatabase) {
        
        renderDatabaseRow(html, 2 + activeDatabase.getModel().getDays(), activeDatabase.getName());
        
        renderUserRow(html, activeDatabase.getOwnerActivityMap());

        for (PartnerActivityModel partnerModel : activeDatabase.getPartnerActivityModels()) {
            renderPartnerRow(html, partnerModel);

            for (ActivityMap activityMap : partnerModel.getActivityMaps()) {
                renderUserRow(html, activityMap);
            }
        }
    }

    private void renderDatabaseRow(StringBuilder html, int colspan, String name) {
        html.append("<tr>");
        html.append("<td class='act-database-header' colspan='");
        html.append(colspan);
        html.append("' style='color: #67a639; font-weight:bold; padding-top: 13px'>");
        html.append(name);
        html.append("</td>");
        html.append("</tr>");
    }

    private void renderPartnerRow(StringBuilder html, PartnerActivityModel partnerModel) {
        if (partnerModel.hasActivity() || RENDER_INACTIVE_ROWS) {
            // spacer
            html.append("<tr><td class='act-partner-spacer' colspan='");
            html.append(2 + partnerModel.getDatabaseModel().getModel().getDays());
            html.append("' style='height:8px'>");
            html.append("</td></tr>");
    
            // partner row
            html.append("<tr>");
            html.append("<td class='act-partner-header' style='padding-left: 12px; color: black; font-weight:bold;'>");
            html.append(partnerModel.getPartner().getName());
            html.append("</td>");
    
            html.append("<td>&nbsp;</td>");
    
            renderGraph(html, partnerModel.getDatabaseModel(), partnerModel.getTotalActivityMap());
    
            html.append("</tr>");
        }
    }

    private void renderUserRow(StringBuilder html, ActivityMap activityMap) {
        if (activityMap.hasActivity() || RENDER_INACTIVE_ROWS) {
            html.append("<tr class='act-user-row' style='height:").append(ROW_HEIGHT).append("px;'>");

            html.append("<td class='act-user-header' style='padding-left: 25px;'>");
            html.append("<a href=\"mailto:").append(activityMap.getUser().getEmail()).append("\">");
            html.append(activityMap.getUser().getName());
            html.append("</a>");
            html.append("</td>");

            html.append("<td>&nbsp;</td>");

            renderGraph(html, activityMap.getDatabaseModel(), activityMap.getMap());

            html.append("</tr>");
        }
    }

    private void renderGraph(StringBuilder html, DatabaseModel databaseModel, Map<Integer, Integer> activityMap) {
        List<Integer> list = new ArrayList<Integer>(activityMap.values());
        Collections.reverse(list);
        for (int i = 0; i < list.size(); i++) {
            int value = list.get(i);
            html.append("<td class='act-graphcell' style='");
            html.append("width:").append(GRAPHCELL_WIDTH).append("px; ");
            html.append("height:").append(ROW_HEIGHT).append("px; ");
            html.append("border: 1px solid black; ");
            html.append("background-color:#").append(determineColor(value)).append("; ");
            html.append("text-align:center; vertical-align:middle' ");
            html.append("title='").append(determineTitle(databaseModel, value, i)).append("'>");
            html.append((value != 0) ? value : "&nbsp;");
            html.append("</td>");
        }
    }

    private String determineColor(int value) {
        String R = "85";
        String B = "55";
        String background = "f0f0f0";

        int maxbound = 15;
        int min = 0xb0;
        int max = 0xff;

        if (value <= 0) {
            return background;
        }

        if (value > maxbound) {
            return R + Integer.toHexString(max) + B;
        }

        int range = max - min;
        int step = range / maxbound;
        return R + Integer.toHexString(min + value * step) + B;
    }

    private String determineTitle(DatabaseModel databaseModel, int updates, int dayIndex) {
        Date today = databaseModel.getModel().getDate();
        int totalDays = databaseModel.getModel().getDays();
        Date date = DigestDateUtil.daysAgo(today, (totalDays - dayIndex - 1));
        return I18N.MESSAGES.activityDigestGraphTooltip(updates, date);
    }
    
    private void renderInactiveDatabases(StringBuilder html, ActivityDigestModel model) {
        Collection<DatabaseModel> inactiveDatabases = model.getInactiveDatabases();
        if (!inactiveDatabases.isEmpty()) {
            html.append("<div class='act-inactive' style='margin-top:35px;'>");
            html.append(I18N.MESSAGES.activityDigestInactiveDatabases(model.getDays()));
            html.append("<br>");

            html.append("<table class='act-data' border='0' cellpadding='0' cellspacing='0' ");
            html.append("style='width:").append(IA_DBS_HEADERCELL_WIDTH + IA_DBS_CONTENTCELL_WIDTH).append("px; ");
            html.append("border-collapse:collapse;'>");
            
            html.append("<tr>");
            html.append("<th style='width:").append(IA_DBS_HEADERCELL_WIDTH).append("px;'>&nbsp;</th>");
            html.append("<th style='width:").append(IA_DBS_CONTENTCELL_WIDTH).append("px;'>&nbsp;</th>");
            html.append("</tr>");

            for (DatabaseModel db : inactiveDatabases) {
                html.append("<tr>");
                html.append("<td class='act-database-header' style='color: #67a639; font-weight:bold'>");
                html.append(db.getName());
                html.append("</td>");
                html.append("<td class='act-lastedit' style='font-style: italic'>");
                html.append(formatLastEdited(db));
                html.append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
            html.append("</div>");
        }
    }

    private String formatLastEdited(DatabaseModel db) {
        SiteHistory lastEdit = db.getLastEdit();
        if (lastEdit == null) {
            return I18N.CONSTANTS.lastEditUnknown();
        } else {
            return I18N.CONSTANTS.lastEdit() + " " +
                new PrettyTime(LocaleProxy.getLocale()).format(new Date(lastEdit.getTimeCreated()));
            // return DigestDateUtil.daysBeforeMidnight(new Date(), lastEdit.getTimeCreated()) + " day(s) ago";
        }
    }

}
