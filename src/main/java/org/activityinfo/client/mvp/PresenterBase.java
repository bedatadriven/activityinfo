package org.activityinfo.client.mvp;

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

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.dto.DTO;

/*
 * Base class to reduce code in presenter classes and to provide a template
 */
public class PresenterBase<V extends View<M>, M extends DTO>
    implements Presenter<V, M> {

    protected final Dispatcher service;
    protected final EventBus eventBus;
    protected final V view;

    public PresenterBase(Dispatcher service, EventBus eventBus, V view) {
        this.service = service;
        this.eventBus = eventBus;
        this.view = view;

        addListeners();
    }

    /*
     * Adds all the relevant listeners from the view to the presenter
     */
    protected void addListeners() {

    }
}
