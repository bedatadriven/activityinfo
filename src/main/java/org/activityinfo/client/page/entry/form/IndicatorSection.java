package org.activityinfo.client.page.entry.form;

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

import org.activityinfo.client.util.IndicatorNumberFormat;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.IndicatorGroup;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.common.collect.Lists;

public class IndicatorSection extends LayoutContainer implements FormSection<SiteDTO> {

    private List<NumberField> indicatorFields = Lists.newArrayList();

    public IndicatorSection(ActivityDTO activity) {

        TableLayout layout = new TableLayout(3);
        layout.setCellPadding(5);
        layout.setCellVerticalAlign(Style.VerticalAlignment.TOP);

        setLayout(layout);
        setStyleAttribute("fontSize", "8pt");
        setScrollMode(Scroll.AUTOY);

        for (IndicatorGroup group : activity.groupIndicators()) {

            if (group.getName() != null) {
                addGroupHeader(group.getName());
            }

            for (IndicatorDTO indicator : group.getIndicators()) {
                addIndicator(indicator);
            }
        }
    }

    private void addGroupHeader(String name) {

        TableData layoutData = new TableData();
        layoutData.setColspan(3);

        Text header = new Text(name);
        header.setStyleAttribute("fontSize", "9pt");
        header.setStyleAttribute("fontWeight", "bold");
        header.setStyleAttribute("marginTop", "6pt");

        add(header, layoutData);
    }

    private void addIndicator(IndicatorDTO indicator) {

        String name = indicator.getName();
        if (indicator.isMandatory()) {
            name += " *";
        }
        Text indicatorLabel = new Text(Format.htmlEncode(name));
        indicatorLabel.setStyleAttribute("fontSize", "9pt");
        add(indicatorLabel);

        NumberField indicatorField = new NumberField();
        indicatorField.setName(indicator.getPropertyName());
        indicatorField.setWidth(50);
        indicatorField.setFormat(IndicatorNumberFormat.INSTANCE);
        indicatorField.setStyleAttribute("textAlign", "right");
        if (indicator.isMandatory()) {
            indicatorField.setAllowBlank(false);
        }

        if (indicator.getDescription() != null
            && !indicator.getDescription().isEmpty()) {
            ToolTipConfig tip = new ToolTipConfig();
            tip.setDismissDelay(0);
            tip.setShowDelay(100);
            tip.setText(indicator.getDescription());

            indicatorField.setToolTip(tip);
        }

        add(indicatorField);

        Text unitLabel = new Text(indicator.getUnits());
        unitLabel.setStyleAttribute("fontSize", "9pt");

        add(unitLabel);

        indicatorFields.add(indicatorField);
    }

    @Override
    public boolean validate() {
        boolean valid = true;
        for (NumberField field : indicatorFields) {
            valid &= field.validate();
        }
        return valid;
    }

    @Override
    public void updateModel(SiteDTO m) {
        for (NumberField field : indicatorFields) {
            m.set(field.getName(), field.getValue());
        }
    }

    @Override
    public void updateForm(SiteDTO m) {
        for (NumberField field : indicatorFields) {
            field.setValue((Number) m.get(field.getName()));
        }
    }

    @Override
    public Component asComponent() {
        return this;
    }
}
