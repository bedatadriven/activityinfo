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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.widget.wizard.WizardPage;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class LayerTypePage extends WizardPage {

    private RadioGroup radioGroup = new RadioGroup();
    private Radio proportionalCircleRadio = new Radio();
    private Radio iconRadio = new Radio();
    private Radio piechartRadio = new Radio();
    private Radio polygonRadio = new Radio();

    public LayerTypePage() {

        FlowLayout layout = new FlowLayout();
        layout.setMargin(15);
        setLayout(layout);

        proportionalCircleRadio.setValue(true);

        proportionalCircleRadio
            .setBoxLabel(I18N.CONSTANTS.proportionalCircle());
        iconRadio.setBoxLabel(I18N.CONSTANTS.icon());
        piechartRadio.setBoxLabel(I18N.CONSTANTS.pieChart());
        polygonRadio.setBoxLabel(I18N.CONSTANTS.shadedPolygons());

        radioGroup.add(piechartRadio);
        radioGroup.add(proportionalCircleRadio);
        radioGroup.add(iconRadio);
        radioGroup.add(polygonRadio);

        Text header = new Text(I18N.CONSTANTS.chooseSymbol());
        header.setTagName("h2");

        add(header, new FlowData(new Margins(0, 0, 15, 0)));

        add(proportionalCircleRadio);
        add(iconRadio);
        add(piechartRadio);
        add(polygonRadio);

        // Let the user know whether or not he can select multiple indicators
        // for the layer
        // he wants to add to the map
        radioGroup.addListener(Events.Change, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                LayerTypePage.this.fireEvent(Events.Change, new BaseEvent(
                    Events.Change));
            }
        });
    }

    public MapLayer newLayer() {
        if (radioGroup.getValue() == proportionalCircleRadio) {
            return new BubbleMapLayer();
        } else if (radioGroup.getValue() == iconRadio) {
            return new IconMapLayer();
        } else if (radioGroup.getValue() == piechartRadio) {
            return new PiechartMapLayer();
        } else if (radioGroup.getValue() == polygonRadio) {
            return new PolygonMapLayer();
        } else {
            throw new IllegalStateException();
        }
    }
}
