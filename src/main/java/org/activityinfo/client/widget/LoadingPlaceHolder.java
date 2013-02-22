package org.activityinfo.client.widget;

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

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;

/**
 * An AsyncMonitor implementation that shows the "Loading Component" message
 * 
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class LoadingPlaceHolder extends LayoutContainer implements AsyncMonitor {
    private final Html html;

    public LoadingPlaceHolder() {
        super();
        setLayout(new CenterLayout());
        html = new Html();
        html.addStyleName("loading-placeholder");
        html.setHtml(I18N.CONSTANTS.loadingComponent());
        add(html);
    }

    @Override
    public void beforeRequest() {

    }

    @Override
    public void onCompleted() {
        html.setHtml("Loaded.");
    }

    @Override
    public void onConnectionProblem() {
        html.setHtml(I18N.CONSTANTS.connectionProblem());
    }

    @Override
    public boolean onRetrying() {
        html.setHtml(I18N.CONSTANTS.retrying());
        return false;
    }

    @Override
    public void onServerError() {
        html.setHtml(I18N.CONSTANTS.serverError());
    }
}
