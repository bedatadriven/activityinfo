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

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.shared.dto.DTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface CanCreate<M extends DTO> {

    interface CreateHandler extends EventHandler {
        void onCreate(CreateEvent createEvent);
    }

    interface CancelCreateHandler extends EventHandler {
        void onCancelCreate(CancelCreateEvent createEvent);
    }

    interface StartCreateHandler extends EventHandler {
        void onStartCreate(StartCreateEvent createEvent);
    }

    // The user signals the presenter it wants to add a new item
    HandlerRegistration addCreateHandler(CreateHandler handler);

    // The user signals the presenter it wants to add a new item
    HandlerRegistration addCancelCreateHandler(CancelCreateHandler handler);

    // The user signals the presenter it wants to add a new item
    HandlerRegistration addStartCreateHandler(StartCreateHandler handler);

    // An item is created by the presenter, this method adds the item to the
    // view
    void create(M item);

    // Is the 'new' button enabled?
    void setCreateEnabled(boolean createEnabled);

    // Set the UI for creation of a new entity
    void startCreate();

    // The user wants to exit the new entity creation mode
    void cancelCreate();

    // Let the user know what's going on when creating an entity
    AsyncMonitor getCreatingMonitor();

    // The Presenter has a seperate view for creating/updating domain object
    class CreateEvent extends GwtEvent<CreateHandler> {
        public static final Type TYPE = new Type<CreateHandler>();

        @Override
        public Type<CreateHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(CreateHandler handler) {
            handler.onCreate(this);
        }
    }

    // The Presenter has a seperate view for creating/updating domain object
    class CancelCreateEvent extends GwtEvent<CancelCreateHandler> {
        public static final Type TYPE = new Type<CancelCreateHandler>();

        @Override
        public Type<CancelCreateHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(CancelCreateHandler handler) {
            handler.onCancelCreate(this);
        }
    }

    // The Presenter has a seperate view for creating/updating domain object
    class StartCreateEvent extends GwtEvent<StartCreateHandler> {
        public static final Type TYPE = new Type<StartCreateHandler>();

        @Override
        public Type<StartCreateHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(StartCreateHandler handler) {
            handler.onStartCreate(this);
        }
    }
}
