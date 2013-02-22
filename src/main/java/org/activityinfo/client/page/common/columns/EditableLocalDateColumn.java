package org.activityinfo.client.page.common.columns;

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

import java.util.Date;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;

public class EditableLocalDateColumn extends LocalDateColumn {

    public EditableLocalDateColumn(String property, String header, int width) {
        super(property, header, width);

        DateField datefieldStartDate = new DateField();
        datefieldStartDate.getPropertyEditor().setFormat(FORMAT);
        setEditor(new CellEditor(datefieldStartDate) {

            @Override
            public Object preProcessValue(Object value) {
                if (value == null) {
                    return null;
                } else {
                    return ((LocalDate) value).atMidnightInMyTimezone();
                }
            }

            @Override
            public Object postProcessValue(Object value) {
                if (value == null) {
                    return null;
                } else {
                    return new LocalDate((Date) value);
                }
            }
        });
    }
}