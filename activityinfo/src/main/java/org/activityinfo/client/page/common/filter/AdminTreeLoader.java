package org.activityinfo.client.page.common.filter;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.AdminLevelModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class AdminTreeLoader extends BaseTreeLoader<AdminEntityModel> {

    private List<AdminLevelModel> hierarchy = new ArrayList<AdminLevelModel>();

    public AdminTreeLoader(Dispatcher service) {
        super(new AdminTreeProxy(service, Collections.<AdminLevelModel>emptyList()));

    }

    public void setHierarchy(List<AdminLevelModel> hierarchy) {
        this.hierarchy = hierarchy;
        ((AdminTreeProxy) this.proxy).setHierarchy(hierarchy);
    }

    @Override
    public boolean hasChildren(AdminEntityModel parent) {
        if (hierarchy.size() <= 1)
            return false;

        return this.hierarchy.get(hierarchy.size() - 1).getId() != parent.getLevelId();
    }
}
