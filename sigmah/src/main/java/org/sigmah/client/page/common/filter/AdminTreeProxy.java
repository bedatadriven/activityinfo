/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdminTreeProxy implements DataProxy {

    private final Dispatcher service;

    private List<AdminLevelDTO> hierarchy;

    public AdminTreeProxy(Dispatcher service, List<AdminLevelDTO> hierarchy) {
        this.service = service;
        this.hierarchy = hierarchy;
    }

    public void setHierarchy(List<AdminLevelDTO> hierarchy) {
        this.hierarchy = hierarchy;
    }

    public void load(DataReader dataReader, Object parent, final AsyncCallback callback) {

        if (hierarchy.size() == 0) {
            callback.onSuccess(Collections.emptyList());
            return;
        }

        if (parent == null) {

            service.execute(new GetAdminEntities(hierarchy.get(0).getId()), null, new AsyncCallback<AdminEntityResult>() {
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void onSuccess(AdminEntityResult result) {
                    callback.onSuccess(new ArrayList<AdminEntityDTO>(result.getData()));
                }
            });

        } else {

            assert parent instanceof AdminEntityDTO : "expecting AdminEntityDTO";

            AdminEntityDTO parentEntity = (AdminEntityDTO) parent;
            AdminLevelDTO childLevel = (AdminLevelDTO) findChildLevel(parentEntity);

            // find the next child in this hierachy

            if (childLevel == null) {
                callback.onSuccess(Collections.emptyList());
            } else {

                service.execute(new GetAdminEntities(childLevel.getId(), parentEntity.getId()), null, new AsyncCallback<AdminEntityResult>() {
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    public void onSuccess(AdminEntityResult result) {
                        callback.onSuccess(result.getData());
                    }
                });
                
            }
        }

    }

    private AdminLevelDTO findChildLevel(AdminEntityDTO parent) {
        for (AdminLevelDTO level : hierarchy) {
            if (!level.isRoot() && level.getParentLevelId() == parent.getLevelId()) {
                return level;
            }
        }
        return null;
    }

}

