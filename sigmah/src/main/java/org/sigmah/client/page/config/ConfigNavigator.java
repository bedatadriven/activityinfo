/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config;

import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.nav.Link;
import org.sigmah.client.page.common.nav.Navigator;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ConfigNavigator implements Navigator {

    protected final Dispatcher service;
    protected final UIConstants messages;
    protected final IconImageBundle icons;


    @Inject
    public ConfigNavigator(Dispatcher service, UIConstants messages, IconImageBundle icons) {
        this.service = service;
        this.messages = messages;
        this.icons = icons;
    }

    public String getHeading() {
        return I18N.CONSTANTS.setup();
    }

    public String getStateId() {
        return "configNavPanel";
    }

    @Override
    public boolean hasChildren(Link parent) {
        return parent.getPageState() instanceof DbListPageState;
    }


    public void load(DataReader<List<Link>> dataReader, Object parent, AsyncCallback<List<Link>> callback) {

        if (parent == null) {

            Link accountLink = Link
                    .to(new AccountPageState())
                    .labeled(messages.mySettings())
                    .withIcon(icons.setup()).build();

            Link dbListLink = Link
                    .to(new DbListPageState())
                    .labeled(messages.databases())
                    .withIcon(icons.database()).build();

            callback.onSuccess(Arrays.asList(accountLink, dbListLink));

        } else {
            Link link = (Link) parent;
            if (link.getPageState() instanceof DbListPageState) {
                loadDbList(callback);
            }
        }
    }

    public void loadDbList(final AsyncCallback<List<Link>> callback) {
        service.execute(new GetSchema(), null, new Got<SchemaDTO>() {
            @Override
            public void got(SchemaDTO result) {
                List<Link> list = new ArrayList<Link>();
                for (UserDatabaseDTO db : result.getDatabases()) {
                    if (db.isDesignAllowed() || db.isManageUsersAllowed()) {
                        Link link = Link
                                .to(new DbPageState(DbConfigPresenter.DatabaseConfig, db.getId()))
                                .labeled(db.getName())
                                .withIcon(icons.database()).build();
                        link.set("db", db);
                        list.add(link);
                    }
                }
                callback.onSuccess(list);
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }


}
