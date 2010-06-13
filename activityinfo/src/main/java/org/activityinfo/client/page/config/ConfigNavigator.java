package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.data.DataReader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.callback.Got;
import org.activityinfo.client.i18n.UIConstants;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.nav.Link;
import org.activityinfo.client.page.common.nav.Navigator;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
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
        return Application.CONSTANTS.setup();
    }

    public String getStateId() {
        return "configNavPanel";
    }

    @Override
    public boolean hasChildren(Link parent) {
        return parent.getPlace() instanceof DbListPlace;
    }


    public void load(DataReader<List<Link>> dataReader, Object parent, AsyncCallback<List<Link>> callback) {

        if (parent == null) {

            List<Link> list = new ArrayList<Link>();

            Link accountLink = new Link(messages.mySettings(),
                    new AccountPlace(), icons.setup());

            list.add(accountLink);

            final Link dbListLink = new Link(messages.databases(),
                    new DbListPlace(), icons.database());

            list.add(dbListLink);

            callback.onSuccess(list);

        } else {

            Link link = (Link) parent;
            if (link.getPlace() instanceof DbListPlace) {
                loadDbList(callback);

            } else if (link.getPlace().getPageId().equals(Pages.DatabaseConfig)) {

//                List<Link> list = new ArrayList<Link>();
//                int dbId = ((DbPlace) link.getPlace()).getDatabaseId();
//                list.add(new Link(messages.design(), new DbPlace(Pages.Design, dbId), icons.design()));
//
//                if(((UserDatabaseDTO) link.get("db")).isDesignAllowed())
//                    list.add(new Link(messages.users(), new DbPlace(Pages.DatabaseUsers, dbId), icons.user()));
//
//                list.add(new Link(messages.partners(), new DbPlace(Pages.DatabasePartners, dbId), icons.group()));
//
//                callback.onSuccess(list);
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
                        Link link = new Link(db.getName(), new DbPlace(Pages.DatabaseConfig, db.getId()), icons.database());
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
