
package org.sigmah.client.page.config.form;

import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * 
 * Corrects a bug in the GXT 2.0 library: when you try to apply a <code>FitLayout</code>
 * to a <code>FieldSet</code>, the bottom gets cut off.
 * 
 * @author Alex Bertram
 *
 */

public class FieldSetFitLayout extends FitLayout {
	
	public FieldSetFitLayout() {
		super();
	}

	@Override
	protected void setItemSize(Component item, Size size) {
		
		size.height = size.height - 30;
		
		super.setItemSize(item, size);
	}


}