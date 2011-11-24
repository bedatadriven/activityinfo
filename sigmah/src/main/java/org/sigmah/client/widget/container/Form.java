package org.sigmah.client.widget.container;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

/**
 * 
 */
public abstract class Form extends LayoutContainer {

	private final FormLayout layout;
	
	public Form() {
		layout = new FormLayout();
		setLayout(layout);
	}
	
	@Override
	public abstract int getWidth();

	@Override
	public abstract int getHeight();

}
