package org.activityinfo.client.page.report.editor;

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
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventBus;
import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class ElementDialog extends Dialog implements
    HasReportElement<ReportElement> {
    private ReportElement model;
    private ReportElementEditor editor;
    private EditorProvider editorProvider;
    private Callback callback;

    private ReportEventBus reportEventBus;
    private boolean dirty;

    public interface Callback {
        void onOK(boolean dirty);

        void onClose(boolean dirty);
    }

    @Inject
    public ElementDialog(EventBus eventBus, EditorProvider editorProvider) {
        this.editorProvider = editorProvider;
        setLayout(new FitLayout());
        setButtons(OKCANCEL);

        this.reportEventBus = new ReportEventBus(eventBus, this);
        this.reportEventBus.listen(new ReportChangeHandler() {

            @Override
            public void onChanged() {
                dirty = true;
            }
        });

        setMonitorWindowResize(true);
    }

    public void hideCancel() {
        getButtonById(CANCEL).setVisible(false);
    }

    public void show(ReportElement element, Callback callback) {
        this.callback = callback;

        setWidth((int) (Window.getClientWidth() * 0.90));
        setHeight((int) (Window.getClientHeight() * 0.90));
        if (element.getTitle() == null) {
            setHeading("New Report Element");
        } else {
            setHeading(element.getTitle());
        }

        removeAll();

        bind(element);

        this.editor = editorProvider.create(model);
        this.editor.bind(model);
        add(editor.getWidget());
        layout();

        super.show();
    }

    @Override
    public ReportElement getModel() {
        return model;
    }

    @Override
    protected void onButtonPressed(Button button) {
        hide();
        editor.disconnect();
        if (button.getItemId().equals(Dialog.OK)) {
            callback.onOK(dirty);
        }
    }

    @Override
    protected void onWindowResize(int width, int height) {
        setWidth((int) (width * 0.90));
        setHeight((int) (height * 0.90));
        setPosition((int) (width * 0.05), (int) (height * 0.05));
    }

    @Override
    public void bind(ReportElement model) {
        this.model = model;
    }

    @Override
    protected void onHide() {
        super.onHide();
        callback.onClose(dirty);
    }

    @Override
    public void disconnect() {
        reportEventBus.disconnect();
    }
}
