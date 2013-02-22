package org.activityinfo.client.report.editor.map;

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

import java.util.Collection;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.GetAdminLevels;
import org.activityinfo.shared.command.result.AdminLevelResult;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdminLevelPanel extends LayoutContainer {

    private final Dispatcher dispatcher;
    private RadioGroup radioGroup = new RadioGroup();

    public AdminLevelPanel(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void setIndicators(Collection<Integer> indicatorIds) {

        showLoading();

        GetAdminLevels query = new GetAdminLevels();
        query.setIndicatorIds(Sets.newHashSet(indicatorIds));

        dispatcher.execute(query, new AsyncCallback<AdminLevelResult>() {

            @Override
            public void onFailure(Throwable caught) {
                showLoadingFailed();
            }

            @Override
            public void onSuccess(AdminLevelResult result) {
                showOptions(result.getData());
            }
        });
    }

    protected void showOptions(List<AdminLevelDTO> levels) {
        removeAll();
        if (radioGroup != null) {
            radioGroup.removeAllListeners();
        }
        radioGroup = new RadioGroup();
        boolean missingPolygons = false;
        for (AdminLevelDTO level : levels) {
            Radio radio = new Radio();
            radio.setBoxLabel(level.getName());
            radio.setEnabled(level.getPolygons());
            radio.setData("adminLevelId", level.getId());
            radioGroup.add(radio);
            add(radio);

            if (!level.getPolygons()) {
                missingPolygons = true;
            }
        }

        if (missingPolygons) {
            addMissingPolygonMessage();
        }

        radioGroup.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                AdminLevelPanel.this.fireEvent(Events.Change, new BaseEvent(
                    Events.Change));
            }
        });
        layout();
    }

    private void addMissingPolygonMessage() {
        Text text = new Text(I18N.CONSTANTS.noPolygonsWarning());
        Margins margin = new Margins(15, 15, 0, 0);
        add(text, new FlowData(margin));
    }

    private void showLoading() {
        removeAll();
        add(new Text(I18N.CONSTANTS.loading()));
        layout();
    }

    private void showLoadingFailed() {
        removeAll();
        add(new Text(I18N.CONSTANTS.connectionProblem()));
        layout();
    }

    public Integer getSelectedLevelId() {
        if (radioGroup.getValue() == null) {
            return null;
        }
        return (Integer) radioGroup.getValue().getData("adminLevelId");
    }

}
