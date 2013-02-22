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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public abstract class FormSectionWithFormLayout<M> extends LayoutContainer implements FormSection<M> {

	private final FormLayout layout;

	public FormSectionWithFormLayout() {
		layout = new FormLayout();
		setLayout(layout);
		setScrollMode(Scroll.AUTOY);
		setStyleAttribute("padding", "10px");
	}
	
	protected final FormLayout getFormLayout() {
		return layout;
	}

	@Override
	public Component asComponent() {
		return this;
	}
	
	
}
