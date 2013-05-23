package org.activityinfo.client.filter;

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
import org.activityinfo.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public final class FilterToolBar extends ToolBar {

    private Button applyButton;
    private boolean renderApplyButton;
    private Button removeButton;
    private boolean renderRemoveButton;

    public interface ApplyFilterHandler extends EventHandler {
        void onApplyFilter(ApplyFilterEvent deleteEvent);
    }

    public interface RemoveFilterHandler extends EventHandler {
        void onRemoveFilter(RemoveFilterEvent deleteEvent);
    }

    public static class ApplyFilterEvent extends GwtEvent<ApplyFilterHandler> {
        public static final Type TYPE = new Type<ApplyFilterHandler>();

        @Override
        public Type<ApplyFilterHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(ApplyFilterHandler handler) {
            handler.onApplyFilter(this);
        }
    }

    public static class RemoveFilterEvent extends GwtEvent<RemoveFilterHandler> {
        public static final Type TYPE = new Type<RemoveFilterHandler>();

        @Override
        public Type<RemoveFilterHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(RemoveFilterHandler handler) {
            handler.onRemoveFilter(this);
        }
    }

    public FilterToolBar() {
        this(true, true);
    }

    public FilterToolBar(boolean renderApplyButton, boolean renderRemoveButton) {
        super();
        this.renderApplyButton = renderApplyButton;
        this.renderRemoveButton = renderRemoveButton;

        initializeComponent();

        createApplyButton();
        createRemoveButton();
    }

    private void initializeComponent() {
    }

    private void createRemoveButton() {
        if (renderRemoveButton) {
            removeButton = new Button(
                I18N.CONSTANTS.remove(),
                IconImageBundle.ICONS.delete(),

                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        fireEvent(new RemoveFilterEvent());
                    }
                });

            add(removeButton);
            setRemoveFilterEnabled(false);
        }
    }

    private void createApplyButton() {
        if (renderApplyButton) {
            applyButton = new Button(
                I18N.CONSTANTS.apply(),
                IconImageBundle.ICONS.applyFilter(),

                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        fireEvent(new ApplyFilterEvent());
                    }
                });

            add(applyButton);
            setApplyFilterEnabled(false);
        }
    }

    public HandlerRegistration addApplyFilterHandler(ApplyFilterHandler handler) {
        return this.addHandler(handler, ApplyFilterEvent.TYPE);
    }

    public HandlerRegistration addRemoveFilterHandler(
        RemoveFilterHandler handler) {
        return this.addHandler(handler, RemoveFilterEvent.TYPE);
    }

    public void setRemoveFilterEnabled(boolean enabled) {
        removeButton.setEnabled(enabled);
    }

    public void setApplyFilterEnabled(boolean enabled) {
        applyButton.setEnabled(enabled);
    }

}
