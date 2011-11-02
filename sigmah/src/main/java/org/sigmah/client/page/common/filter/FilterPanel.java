package org.sigmah.client.page.common.filter;

import org.sigmah.shared.command.Filter;

import com.google.gwt.user.client.ui.HasValue;

public interface FilterPanel extends HasValue<Filter> {
	
	/**
	 * Applies a base filter to the contents of this FilterPanel.
	 * @param filter
	 */
	void applyBaseFilter(Filter filter);

}
