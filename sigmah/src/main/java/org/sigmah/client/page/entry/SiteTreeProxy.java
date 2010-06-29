/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetMonthlyReports;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.Month;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.MonthlyReportResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.IndicatorRowDTO;
import org.sigmah.shared.dto.SiteDTO;

import java.util.List;

public class SiteTreeProxy implements DataProxy {

    private Dispatcher service;
    private List<AdminLevelDTO> hierarchy;
    private int activityId;
    private Month startMonth;
    private Month endMonth;
    private boolean multiplePartners;

    public SiteTreeProxy(Dispatcher service, List<AdminLevelDTO> hierarchy, int activityId) {
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

        if (parent == null) {
            loadAdmin(hierarchy.get(0), null, callback);
        } else if (parent instanceof AdminEntityDTO) {
            AdminEntityDTO entity = (AdminEntityDTO) parent;
            int childLevelIndex = getChildLevel(entity);
            if (childLevelIndex < hierarchy.size() - 1) {
                loadAdmin(hierarchy.get(childLevelIndex), entity.getId(), callback);
            } else {
                loadSites(entity.getId(), callback);
            }
        } else if (parent instanceof SiteDTO) {
            loadIndicators(((SiteDTO) parent).getId(), callback);
        }
    }

    private int getChildLevel(AdminEntityDTO entity) {
        for (int i = 0; i != hierarchy.size(); ++i) {
            if (hierarchy.get(i).getId() == entity.getLevelId()) {
                if (i + 1 < hierarchy.size()) {
                    return i + 1;
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    protected void loadAdmin(AdminLevelDTO level, Integer parentId, final AsyncCallback<List<AdminEntityDTO>> callback) {

        service.execute(new GetAdminEntities(level.getId(), parentId, activityId), null, new AsyncCallback<AdminEntityResult>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(AdminEntityResult result) {
                callback.onSuccess(result.getData());
            }
        });
    }

    protected void loadSites(int parentEntityId, final AsyncCallback<List<SiteDTO>> callback) {

        GetSites cmd = new GetSites();
        cmd.setActivityId(activityId);
        cmd.setAdminEntityId(parentEntityId);

        service.execute(cmd, null, new AsyncCallback<SiteResult>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(SiteResult result) {
                int lastLevelId = hierarchy.get(hierarchy.size() - 1).getId();
                for (SiteDTO site : result.getData()) {
                    site.set("name", site.getAdminEntity(lastLevelId).getName());
                }
                callback.onSuccess(result.getData());

            }
        });
    }

    protected void loadIndicators(int siteId, final AsyncCallback<List<IndicatorRowDTO>> callback) {

        GetMonthlyReports cmd = new GetMonthlyReports(siteId, startMonth, endMonth);

        service.execute(cmd, null, new AsyncCallback<MonthlyReportResult>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(MonthlyReportResult result) {
                for (IndicatorRowDTO row : result.getData()) {
                    row.set("name", row.getIndicatorName());
                }
                callback.onSuccess(result.getData());
            }
        });
    }
}
