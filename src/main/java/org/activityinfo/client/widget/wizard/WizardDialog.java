package org.activityinfo.client.widget.wizard;

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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WizardDialog extends Window {

    private Button prevButton;
    private Button nextButton;
    private Button cancelButton;
    private Button finishButton;
    private WizardPage[] pages;
    private Wizard wizard;
    private CardLayout cardLayout;
    private int currentPageIndex;
    private WizardCallback callback = new WizardCallback();

    public WizardDialog(Wizard wizard) {
        this.wizard = wizard;
        this.pages = wizard.getPages();

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setWidth(Math.max(550, relativeWidth()));
        setHeight(relativeHeight());

        setHeading(wizard.getTitle());

        for (WizardPage page : pages) {
            add(page);
        }

        wizard.addListener(Events.Change, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                enableButtons();
            }
        });

        addButton(cancelButton = new Button(I18N.CONSTANTS.cancel(),
            new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    onCancel();
                }
            }));
        addButton(prevButton = new Button(I18N.CONSTANTS.backButton(),
            new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    onBack();
                }
            }));
        addButton(nextButton = new Button(I18N.CONSTANTS.nextButton(),
            new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    onNext();
                }
            }));
        addButton(finishButton = new Button(I18N.CONSTANTS.finish(),
            new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    onFinish();
                }
            }));

        setPage(0);
    }

    private int relativeHeight() {
        return (int) ((com.google.gwt.user.client.Window.getClientHeight()) * 0.75);
    }

    private int relativeWidth() {
        return (int) ((com.google.gwt.user.client.Window.getClientWidth()) * 0.80);
    }

    private void enableForm(boolean enabled) {
        prevButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
        finishButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }

    protected void onFinish() {
        enableForm(false);
        this.callback.finish(new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                enableForm(true);
                WizardDialog.this.hide();
                callback.onFinished();
            }

            @Override
            public void onFailure(Throwable caught) {
                enableForm(true);
            }
        });
    }

    protected void onNext() {
        setPage(nextPageIndex());
    }

    protected void onBack() {
        setPage(prevPageIndex());
    }

    protected void onCancel() {
        this.hide();
    }

    public void show(WizardCallback callback) {
        this.callback = callback;
        super.show();
    }

    private void setPage(int pageIndex) {
        cardLayout.setActiveItem(pages[pageIndex]);

        currentPageIndex = pageIndex;
        enableButtons();
    }

    private void enableButtons() {
        finishButton.setEnabled(wizard.isFinishEnabled());
        prevButton.setEnabled(prevPageIndex() >= 0);
        nextButton.setEnabled(pages[currentPageIndex].isNextEnabled() &&
            nextPageIndex() < pages.length);
    }

    private int prevPageIndex() {
        int i = currentPageIndex - 1;
        if (i >= 0 && !wizard.isPageEnabled(pages[i])) {
            i--;
        }
        return i;
    }

    private int nextPageIndex() {
        int i = currentPageIndex + 1;
        if (i < pages.length && !wizard.isPageEnabled(pages[i])) {
            i++;
        }
        return i;
    }
}
