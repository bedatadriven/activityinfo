package org.activityinfo.client.page.dashboard.portlets;

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

import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.JsonLoadResultReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.ScriptTagProxy;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public class NewsPortlet extends Portlet {

    private Html html;

    public interface Templates extends ClientBundle {

        @Source("News.html")
        TextResource newsTemplate();

    }

    public static final Templates TEMPLATES = GWT.create(Templates.class);

    public NewsPortlet() {

        setHeading("ActivityInfo News");

        html = new Html();
        add(html);

        ModelType type = new ModelType();
        type.setRoot("posts");
        type.addField("title");
        type.addField("id");
        type.addField("date");
        type.addField("excerpt");
        type.addField("url");

        ScriptTagProxy<String> proxy = new ScriptTagProxy<String>(
            "http://about.activityinfo.org?json=get_category_posts&category_slug=news&count=3");

        // need a loader, proxy, and reader
        JsonLoadResultReader<ListLoadResult<ModelData>> reader = new JsonLoadResultReader<ListLoadResult<ModelData>>(
            type);

        final BaseListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(
            proxy,
            reader);

        loader.addLoadListener(new LoadListener() {

            @Override
            public void loaderLoad(LoadEvent le) {
                XTemplate tpl = XTemplate.create(TEMPLATES.newsTemplate()
                    .getText());
                ListLoadResult<ModelData> result = le.getData();
                tpl.overwrite(html.getElement(),
                    Util.getJsObjects(result.getData(), 3));
            }

            @Override
            public void loaderBeforeLoad(LoadEvent le) {
                html.setHtml(I18N.CONSTANTS.loading());
            }

            @Override
            public void loaderLoadException(LoadEvent le) {
                html.setHtml(I18N.CONSTANTS.connectionProblem());
            }

        });

        loader.load();
    }

}
