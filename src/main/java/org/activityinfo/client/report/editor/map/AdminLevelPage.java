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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.wizard.WizardPage;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class AdminLevelPage extends WizardPage {

    private Dispatcher dispatcher;
    private AdminLevelPanel checkPanel;

    public AdminLevelPage(Dispatcher dispatcher) {
        super();
        this.dispatcher = dispatcher;

        FlowLayout layout = new FlowLayout(15);
        setLayout(layout);

        Text header = new Text(I18N.CONSTANTS.chooseAdminLevelToMap());
        header.setTagName("h1");
        add(header, new FlowData(new Margins(0, 0, 15, 0)));

        this.checkPanel = new AdminLevelPanel(dispatcher);
        add(checkPanel);

        checkPanel.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                AdminLevelPage.this.fireEvent(Events.Change, new BaseEvent(
                    Events.Change));
            }
        });
    }

    public void setIndicators(Collection<Integer> list) {
        this.checkPanel.setIndicators(list);
    }

    public Integer getSelectedLevelId() {
        return checkPanel.getSelectedLevelId();
    }
}
