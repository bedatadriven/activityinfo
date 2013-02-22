package org.activityinfo.client.report.editor.map.layerOptions;

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
import org.activityinfo.shared.report.model.DateRange;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateRangeDialog extends Dialog {

    private DateField date1;
    private DateField date2;

    private SelectionCallback<DateRange> callback;

    public DateRangeDialog() {

        FormLayout layout = new FormLayout();
        layout.setLabelWidth(50);
        layout.setDefaultWidth(100);

        setLayout(layout);
        setButtons(OKCANCEL);
        setHeading(I18N.CONSTANTS.customDateRange());
        setWidth(200);
        setHeight(150);
        setBodyStyle("padding: 5px");

        date1 = new DateField();
        date1.setFieldLabel(I18N.CONSTANTS.fromDate());
        date1.getPropertyEditor().setFormat(
            DateTimeFormat.getMediumDateFormat());

        date2 = new DateField();
        date2.setFieldLabel(I18N.CONSTANTS.toDate());
        date2.getPropertyEditor().setFormat(
            DateTimeFormat.getMediumDateFormat());

        add(date1);
        add(date2);

    }

    public void show(SelectionCallback<DateRange> callback) {
        this.callback = callback;
        show();
    }

    @Override
    protected void onButtonPressed(Button button) {
        if (button.getItemId().equals("ok")) {
            if (callback != null) {
                callback.onSelected(new DateRange(date1.getValue(), date2
                    .getValue()));
            }
        }
        this.callback = null;
        hide();
    }
}
