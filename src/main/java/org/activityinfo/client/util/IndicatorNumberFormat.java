package org.activityinfo.client.util;

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

import org.activityinfo.client.page.entry.form.IndicatorValueFormatter;

import com.google.gwt.i18n.client.NumberFormat;

public class IndicatorNumberFormat implements IndicatorValueFormatter {

    public static final NumberFormat INSTANCE = NumberFormat
        .getFormat("#,##0.####");

    @Override
    public String format(Double value) {
        if(value == null) {
            return "";
        }
        return INSTANCE.format(value);
    }

}
