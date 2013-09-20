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

import java.util.Arrays;
import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.ReportEventBus;
import org.activityinfo.client.page.report.editor.AddElementPanel.AddCallback;
import org.activityinfo.client.page.report.resources.ReportResources;
import org.activityinfo.shared.command.RenderElement.Format;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class CompositeEditor2 extends LayoutContainer implements
    ReportElementEditor<Report>,
    AddCallback,
    ElementWidget.EventHandler {

    private final Provider<ElementWidget> elementWidgetProvider;

    private Report model;
    private LayoutContainer page;
    private AddElementPanel addPanel;

    private ReportEventBus reportEventBus;

    @Inject
    public CompositeEditor2(EventBus eventBus, AddElementPanel addPanel,
        Provider<ElementWidget> elementWidgetProvider) {
        this.elementWidgetProvider = elementWidgetProvider;

        page = new LayoutContainer();
        page.addStyleName(ReportResources.INSTANCE.style().page());
        add(page);

        this.addPanel = addPanel;
        this.addPanel.setCallback(this);
        page.add(addPanel);

        ReportResources.INSTANCE.style().ensureInjected();

        setLayout(new CompositeEditorLayout());
        setScrollMode(Scroll.AUTOY);
        setMonitorWindowResize(true);

        this.reportEventBus = new ReportEventBus(eventBus, this);
    }

    @Override
    protected void onWindowResize(int width, int height) {
        layout(true);
    }

    @Override
    public void bind(Report model) {
        this.model = model;
        for (Component child : page.getItems()) {
            if (child != addPanel) {
                page.remove(child);
            }
        }
        for (ReportElement element : model.getElements()) {
            addElementWidget(element);
        }
        page.layout();
    }

    private void addElementWidget(ReportElement element) {
        ElementWidget widget = elementWidgetProvider.get();
        widget.bindHandler(this);
        widget.bind(element);
        page.insert(widget, page.getItemCount() - 1);
    }

    @Override
    public Report getModel() {
        return this.model;

    }

    @Override
    public Component getWidget() {
        return this;
    }

    @Override
    public List<Format> getExportFormats() {
        return Arrays.asList(Format.Word, Format.PDF);
    }

    @Override
    public void onAdd(ReportElement element) {
        model.addElement(element);
        addElementWidget(element);
        page.layout();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void onElementRemoveClicked(final ElementWidget widget) {
        MessageBox.confirm(I18N.CONSTANTS.deleteElementTitle(),
            I18N.CONSTANTS.deleteElementMessage(),
            new Listener<MessageBoxEvent>() {

                @Override
                public void handleEvent(MessageBoxEvent event) {
                    if (event.getButtonClicked().getItemId().equals(Dialog.YES)) {
                        model.getElements().remove(widget.getModel());
                        page.remove(widget);
                        page.layout();
                        reportEventBus.fireChange();
                    }
                }
            });
    }

    @Override
    public void onElementChanged(ElementWidget widget) {
        reportEventBus.fireChange();
    }
}
