package org.activityinfo.client.page.search;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface SearchResources extends ClientBundle {

    static final SearchResources INSTANCE = GWT.create(SearchResources.class);

    @Source("SitesTemplate.html")
    TextResource sitesTemplate();

    @Source("EntitiesTemplate.html")
    TextResource entitiesTemplate();

    @Source("SearchStyles.css")
    SearchStyles searchStyles();

    @Source("location.png")
    ImageResource location();

    @Source("database.png")
    ImageResource database();

    @Source("activity.png")
    ImageResource activity();

    @Source("date.png")
    ImageResource date();

    @Source("addEdited.png")
    ImageResource addEdited();

    @Source("search.png")
    ImageResource search();

    @Source("searchSmall.png")
    ImageResource searchSmall();

    interface SearchStyles extends CssResource {
        String searchBox();

        String link();

        String panelEntityResults();

        String filterView();

        String searchField();
    }
}
