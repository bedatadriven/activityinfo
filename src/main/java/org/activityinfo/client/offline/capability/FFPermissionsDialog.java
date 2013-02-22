package org.activityinfo.client.offline.capability;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.local.capability.PermissionRefusedException;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FFPermissionsDialog extends Window {

    private AppCache appCache = AppCacheFactory.get();
    private boolean checking = true;
    private AsyncCallback<Void> callback;

    public FFPermissionsDialog(final AsyncCallback<Void> callback) {

        setWidth(450);
        setHeight(250);

        this.callback = callback;

        add(new Html(
            "<p>FireFox requires your permission before enabling offline mode:</p>"
                +
                "<p>Please click the 'Allow' button at the top of this window.</p>"
                +
                "<p>If you do not see an 'Allow' button, you may need to reload the page"
                +
                "before continuing.</p>"));

        Scheduler.get().scheduleFinally(new RepeatingCommand() {

            @Override
            public boolean execute() {
                return checkPermissions();
            }
        });

        getButtonBar().add(
            new Button("Reload page", new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    com.google.gwt.user.client.Window.Location.reload();
                }
            }));

        getButtonBar().add(
            new Button(I18N.CONSTANTS.cancel(),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        hide();
                        callback.onFailure(new PermissionRefusedException());
                    }
                }));

    }

    private boolean checkPermissions() {
        if (isPermissionGranted()) {
            hide();
            callback.onSuccess(null);
        } else {
            appCache.checkForUpdate();
        }
        return checking;
    }

    private boolean isPermissionGranted() {
        return this.appCache.getStatus() != Status.UNCACHED;
    }

    @Override
    public void hide() {
        checking = false;
        super.hide();
    }
}
