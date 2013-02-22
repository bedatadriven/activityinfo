package org.activityinfo.client.report.editor.chart;

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

import java.util.List;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement.Type;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ChartTypeGroup extends BaseObservable implements
    HasReportElement<PivotChartReportElement> {

    private static final String TYPE_DATA = "chartType";

    private final ReportEventHelper events;
    private final List<ToggleButton> buttons = Lists.newArrayList();

    private PivotChartReportElement model = new PivotChartReportElement();

    private final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

        @Override
        public void componentSelected(ButtonEvent ce) {
            fireEvent(Events.Select, new BaseEvent(Events.Select));

            model.setType((Type) ce.getButton().getData(TYPE_DATA));
            events.fireChange();
        }
    };

    public ChartTypeGroup(EventBus eventBus) {
        this.events = new ReportEventHelper(eventBus, this);
        addButton(Type.ClusteredBar, IconImageBundle.ICONS.barChart());
        addButton(Type.Line, IconImageBundle.ICONS.curveChart());
        addButton(Type.Pie, IconImageBundle.ICONS.pieChart());

        events.listen(new ReportChangeHandler() {

            @Override
            public void onChanged() {
                setSelection(model.getType());
            }
        });
    }

    private void addButton(Type type, AbstractImagePrototype icon) {
        ToggleButton button = new ToggleButton("", icon);
        button.setToggleGroup(TYPE_DATA);
        button.setData(TYPE_DATA, type);
        button.addSelectionListener(listener);
        buttons.add(button);
    }

    public PivotChartReportElement.Type getSelection() {
        for (ToggleButton button : buttons) {
            if (button.isPressed()) {
                return button.getData(TYPE_DATA);
            }
        }
        return PivotChartReportElement.Type.ClusteredBar;
    }

    public void setSelection(PivotChartReportElement.Type type) {
        if (type == null) {
            type = PivotChartReportElement.Type.ClusteredBar;
        }

        for (ToggleButton button : buttons) {
            if (type.equals(button.getData(TYPE_DATA))) {
                button.toggle(true);
            }
        }
    }

    public List<ToggleButton> getButtons() {
        return buttons;
    }

    @Override
    public void bind(PivotChartReportElement model) {
        this.model = model;
        setSelection(model.getType());
    }

    @Override
    public PivotChartReportElement getModel() {
        return model;
    }

    @Override
    public void disconnect() {
        events.disconnect();
    }

}
