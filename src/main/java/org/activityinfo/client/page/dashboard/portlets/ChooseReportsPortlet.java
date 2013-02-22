package org.activityinfo.client.page.dashboard.portlets;

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

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.custom.Portlet;

public class ChooseReportsPortlet extends Portlet {

	public ChooseReportsPortlet() {
		
		setHeading(I18N.CONSTANTS.personalizeDashboard());
		Text label = new Text(I18N.CONSTANTS.emptyDashboard());
		label.setStyleAttribute("padding", "15px");
		label.setStyleAttribute("fontSize", "14px");
		add(label);
		
	}
	
}
