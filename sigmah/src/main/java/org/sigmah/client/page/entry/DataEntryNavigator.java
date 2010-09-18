/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.nav.Link;
import org.sigmah.client.page.common.nav.Navigator;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

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
        return parent.getPageState() == null;
    }

    public String getHeading() {
        return I18N.CONSTANTS.activities();
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

                Link dbLink = Link.folderLabelled(db.getName())
                        .usingKey(databaseKey(db))
                        .withIcon(IconImageBundle.ICONS.database())
                        .build();

                Map<String, Link> categories = new HashMap<String, Link>();
                for (ActivityDTO activity : db.getActivities()) {

                    Link actLink = Link
                            .to(new SiteGridPageState(activity))
                            .labeled(activity.getName())
                            .withIcon(IconImageBundle.ICONS.table()).build();

                    if (activity.getCategory() != null) {
                        Link category = categories.get(activity.getCategory());
                        if (category == null) {
                            category = Link.folderLabelled(activity.getCategory())
                                    .usingKey(categoryKey(activity, categories)).build();
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

    private String categoryKey(ActivityDTO activity, Map<String, Link> categories) {
        return "category" + activity.getDatabase().getId() + activity.getCategory() + categories.size();
    }

    private String databaseKey(UserDatabaseDTO db) {
        return "database" + db.getId();
    }
}
