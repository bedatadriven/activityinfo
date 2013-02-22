package org.activityinfo.client.local.ui;

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
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.local.LocalController;
import org.activityinfo.client.local.LocalStateChangeEvent;
import org.activityinfo.client.local.LocalStateChangeEvent.State;
import org.activityinfo.client.util.state.CrossSessionStateProvider;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Dialog presented to the user on startup to prompt activation of offline mode
 * 
 */
@Singleton
public class PromptOfflineDialog extends BasePromptDialog {

    private static final String DONT_ASK_STATE_KEY = "offlineSilent2";

    private final CrossSessionStateProvider stateProvider;
    private final LocalController offlineController;

    private boolean promptedThisSession = false;

    @Inject
    public PromptOfflineDialog(EventBus eventBus,
        CrossSessionStateProvider stateProvider,
        LocalController controller) {
        super(composeHtml());

        this.stateProvider = stateProvider;
        this.offlineController = controller;

        setWidth(500);
        setHeight(350);
        setHeading(I18N.CONSTANTS.installOffline());
        setModal(true);

        getButtonBar().removeAll();

        eventBus.addListener(LocalStateChangeEvent.TYPE,
            new Listener<LocalStateChangeEvent>() {
                @Override
                public void handleEvent(LocalStateChangeEvent be) {
                    onOfflineStatusChange(be.getState());
                }
            });

        if (CAPABILITY_PROFILE.isOfflineModeSupported()) {
            addButton(new Button(I18N.CONSTANTS.installOffline(),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        offlineController.install();
                        hide();
                    }
                }));
        }
        addButton(new Button(I18N.CONSTANTS.notNow(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    askMeLater();
                }

            }));
        addButton(new Button(I18N.CONSTANTS.dontAskAgain(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    dontAskAgain();
                }
            }));
    }

    private static String composeHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<p>").append(I18N.CONSTANTS.offlineIntro1())
            .append("</p>");
        if (CAPABILITY_PROFILE.isOfflineModeSupported()) {
            html.append("<p>").append(I18N.CONSTANTS.offlineIntro2())
                .append("</p>");
            html.append("<p>").append(I18N.CONSTANTS.offlineIntro3())
                .append("</p>");
            html.append("<p>").append(I18N.CONSTANTS.offlineIntro4())
                .append("</p>");
        } else {
            html.append(CAPABILITY_PROFILE.isOfflineModeSupported());
        }
        return html.toString();
    }

    private void onOfflineStatusChange(State state) {
        if (state == State.UNINSTALLED && !promptedThisSession
            && shouldAskAgain()) {
            promptedThisSession = true;
            show();
        }
    }

    private void askMeLater() {
        hide();
    }

    private void dontAskAgain() {
        stateProvider.set(DONT_ASK_STATE_KEY, Boolean.TRUE.toString());
        hide();
    }

    public boolean shouldAskAgain() {
        return !Boolean.TRUE.toString().equals(
            stateProvider.getString(DONT_ASK_STATE_KEY));
    }
}
