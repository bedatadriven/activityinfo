package org.activityinfo.client.report.editor.text;

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

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.client.page.report.editor.ReportElementEditor;
import org.activityinfo.shared.command.RenderElement.Format;
import org.activityinfo.shared.report.model.TextReportElement;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;

public class TextElementEditor extends LayoutContainer implements
    ReportElementEditor<TextReportElement> {

    private TextArea textArea;
    private ReportEventHelper events;
    private TextReportElement model = new TextReportElement();

    @Inject
    public TextElementEditor(EventBus eventBus) {
        super();
        setLayout(new FitLayout());
        textArea = new TextArea();
        textArea.addListener(Events.Change, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                model.setText(textArea.getValue());
            }
        });
        add(textArea);
        events = new ReportEventHelper(eventBus, this);
        events.listen(new ReportChangeHandler() {

            @Override
            public void onChanged() {
                textArea.setValue(model.getText());
            }
        });

    }

    @Override
    public void disconnect() {
        events.disconnect();
    }

    @Override
    public Component getWidget() {
        return this;
    }

    @Override
    public List<Format> getExportFormats() {
        return Arrays.asList(Format.PDF, Format.Word);
    }

    @Override
    public void bind(TextReportElement model) {
        this.model = model;
        this.textArea.setValue(model.getText());
    }

    @Override
    public TextReportElement getModel() {
        return model;
    }
}
