package org.activityinfo.client.page.entry;

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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.entry.form.SiteRenderer;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DetailTab extends TabItem {

    private final Html content;
    private final Dispatcher dispatcher;

    public DetailTab(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        setText(I18N.CONSTANTS.details());

        content = new Html();
        content.setStyleName("details");
        add(content);

    }

    public void setSite(final SiteDTO site) {
        content.setHtml(I18N.CONSTANTS.loading());
        dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(SchemaDTO result) {
                render(result, site);

            }
        });
    }

    private void render(SchemaDTO schema, SiteDTO site) {
        SiteRenderer renderer = new SiteRenderer();
        content.setHtml(renderer.renderSite(site,
            schema.getActivityById(site.getActivityId()), false, true));
    }
}
