package org.sigmah.client.page.report.editor;

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