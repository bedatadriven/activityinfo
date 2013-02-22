package org.activityinfo.client.page.config.form;

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

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

public class DateRangeForm extends FormPanel {

    private DateField fromDate;
    private DateField toDate;

    public DateRangeForm() {

        fromDate = new DateField();
        fromDate.setFieldLabel(I18N.CONSTANTS.fromDate());
        add(fromDate);

        toDate = new DateField();
        toDate.setFieldLabel(I18N.CONSTANTS.toDate());
        add(toDate);

    }

}
