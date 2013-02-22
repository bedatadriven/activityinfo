package org.activityinfo.client.page.report.editor;

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

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;

/**
 * Layouts the single page element centered horizontally.
 * 
 *
 */
final class CompositeEditorLayout extends Layout {
	@Override
	protected void onLayout(Container<?> container, El target) {
	    super.onLayout(container, target);

	    Size size = target.getStyleSize();

	    Component page = container.getItem(0);
	    
	    int hMargin = (size.width-650)/2;
	    Margins margins = new Margins(25, hMargin, 25, hMargin);
	    	
		applyMargins(page.el(), margins);
	}
}