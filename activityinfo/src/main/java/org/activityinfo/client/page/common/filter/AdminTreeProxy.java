package org.activityinfo.client.page.common.filter;

import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.dto.AdminLevelModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class AdminTreeProxy implements DataProxy {

    private final Dispatcher service;

    private List<AdminLevelModel> hierarchy;

    public AdminTreeProxy(Dispatcher service, List<AdminLevelModel> hierarchy) {
        this.service = service;
        this.hierarchy = hierarchy;
    }


    public void setHierarchy(List<AdminLevelModel> hierarchy) {
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
                    callback.onSuccess(new ArrayList<AdminEntityModel>(result.getData()));
                }
            });

        } else {

            assert parent instanceof AdminEntityModel : "expecting AdminEntityModel";

            AdminEntityModel parentEntity = (AdminEntityModel) parent;
            AdminLevelModel childLevel = (AdminLevelModel) findChildLevel(parentEntity);

            //find the next child in this hierachy

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

    private AdminLevelModel findChildLevel(AdminEntityModel parent) {
        for (AdminLevelModel level : hierarchy) {
            if (!level.isRoot() && level.getParentLevelId() == parent.getLevelId()) {
                return level;
            }
        }
        return null;
    }

}

