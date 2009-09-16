package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

import org.activityinfo.client.command.CommandService;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.GetMonthlyReports;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.MonthlyReportResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.AdminLevelModel;
import org.activityinfo.shared.dto.IndicatorRow;
import org.activityinfo.shared.dto.SiteModel;

public class SiteTreeProxy implements DataProxy {

    private CommandService service;
    private List<AdminLevelModel> hierarchy;
    private int activityId;
    private Month startMonth;
    private Month endMonth;
    private boolean multiplePartners;

    public SiteTreeProxy(CommandService service, List<AdminLevelModel> hierarchy, int activityId) {
        this.service = service;
        this.hierarchy = hierarchy;
        this.activityId = activityId;
    }

    public Month getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Month startMonth) {
        this.startMonth = startMonth;
    }

    public Month getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Month endMonth) {
        this.endMonth = endMonth;
    }

    public void load(DataReader reader, Object parent, AsyncCallback callback) {

        if(parent == null) {
            loadAdmin(hierarchy.get(0), null, callback);
        } else if(parent instanceof AdminEntityModel) {
            AdminEntityModel entity = (AdminEntityModel) parent;
            int childLevelIndex = getChildLevel(entity);
            if(childLevelIndex < hierarchy.size()-1) {
                loadAdmin(hierarchy.get(childLevelIndex), entity.getId(), callback);
            } else {
                loadSites(entity.getId(), callback);
            }
        } else if(parent instanceof SiteModel) {
            loadIndicators(((SiteModel) parent).getId(), callback);
        }
    }

    private int getChildLevel(AdminEntityModel entity) {
        for(int i=0; i!=hierarchy.size(); ++i) {
            if(hierarchy.get(i).getId() == entity.getLevelId()) {
                if(i+1< hierarchy.size()) {
                    return i+1;
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    protected void loadAdmin(AdminLevelModel level, Integer parentId, final AsyncCallback<List<AdminEntityModel>> callback) {

        service.execute(new GetAdminEntities(level.getId(), parentId, activityId), null, new AsyncCallback<AdminEntityResult>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(AdminEntityResult result) {
                callback.onSuccess(result.getData());
            }
        });
    }

    protected void loadSites(int parentEntityId, final AsyncCallback<List<SiteModel>> callback) {

        GetSites cmd = new GetSites();
        cmd.setActivityId(activityId);
        cmd.setAdminEntityId(parentEntityId);

        service.execute(cmd, null, new AsyncCallback<SiteResult>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(SiteResult result) {
                int lastLevelId = hierarchy.get(hierarchy.size()-1).getId();
                for(SiteModel site : result.getData()) {
                    site.set("name", site.getAdminEntity(lastLevelId).getName());
                }
                callback.onSuccess(result.getData());

            }
        });
    }

    protected void loadIndicators(int siteId, final AsyncCallback<List<IndicatorRow>> callback) {

        GetMonthlyReports cmd = new GetMonthlyReports(siteId, startMonth, endMonth);

        service.execute(cmd, null, new AsyncCallback<MonthlyReportResult>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(MonthlyReportResult result) {
                for(IndicatorRow row : result.getData()) {
                    row.set("name", row.getIndicatorName());
                }
                callback.onSuccess(result.getData());
            }
        });
    }
}
