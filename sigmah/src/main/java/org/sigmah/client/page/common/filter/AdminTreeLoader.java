package org.sigmah.client.page.common.filter;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class AdminTreeLoader extends BaseTreeLoader<AdminEntityDTO> {

    private List<AdminLevelDTO> hierarchy = new ArrayList<AdminLevelDTO>();

    public AdminTreeLoader(Dispatcher service) {
        super(new AdminTreeProxy(service, Collections.<AdminLevelDTO>emptyList()));

    }

    public void setHierarchy(List<AdminLevelDTO> hierarchy) {
        this.hierarchy = hierarchy;
        ((AdminTreeProxy) this.proxy).setHierarchy(hierarchy);
    }

    @Override
    public boolean hasChildren(AdminEntityDTO parent) {
        if (hierarchy.size() <= 1) {
            return false;
        }

        return this.hierarchy.get(hierarchy.size() - 1).getId() != parent.getLevelId();
    }
}
