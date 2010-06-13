package org.activityinfo.client.page.entry;

import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.common.nav.Link;
import org.activityinfo.client.page.common.nav.Navigator;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataEntryNavigator implements Navigator {
    private final Dispatcher service;

    @Inject
    public DataEntryNavigator(Dispatcher service) {
        this.service = service;
    }

    public boolean hasChildren(Link parent) {
        return parent.getPlace() == null;
    }

    public String getHeading() {
        return Application.CONSTANTS.activities();
    }

    public String getStateId() {
        return "entryNavPanel";
    }

    public void load(DataReader<List<Link>> dataReader, Object parent, final AsyncCallback<List<Link>> callback) {

        if (parent == null) {
            service.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void onSuccess(SchemaDTO schema) {
                    List<Link> list = buildTree(schema);
                    callback.onSuccess(list);
                }
            });
        } else {
            List<Link> list = new ArrayList<Link>();
            List<ModelData> children = ((Link) parent).getChildren();
            for (ModelData child : children) {
                list.add((Link) child);
            }
            callback.onSuccess(list);
        }
    }

    private List<Link> buildTree(SchemaDTO schema) {
        List<Link> list = new ArrayList<Link>();
        for (UserDatabaseDTO db : schema.getDatabases()) {
            if (db.getActivities().size() != 0) {

                Link dbLink = new Link(
                        db.getName(),
                        null,
                        Application.ICONS.database());

                Map<String, Link> categories = new HashMap<String, Link>();
                for (ActivityDTO activity : db.getActivities()) {

                    Link actLink = new Link(
                            activity.getName(),
                            new SiteGridPlace(activity),
                            Application.ICONS.table());

                    if (activity.getCategory() != null) {
                        Link category = categories.get(activity.getCategory());
                        if (category == null) {
                            category = new Link(activity.getCategory(), null);
                            categories.put(activity.getCategory(), category);
                            dbLink.add(category);
                        }
                        category.add(actLink);
                    } else {
                        dbLink.add(actLink);
                    }
                }
                list.add(dbLink);
            }
        }
        return list;
    }
}
