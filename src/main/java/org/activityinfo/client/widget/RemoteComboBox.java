

package org.activityinfo.client.widget;

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

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * Subclass of {@link com.extjs.gxt.ui.client.widget.form.ComboBox} that is better
 * at handling remote connection failures than the standard GXT implementation.
 *
 * In GXT's implementation, the remote query is executed when the user clicks the trigger
 * button for the first time; if this query fails, the ComboBox populates its internal
 * list with an empty set, and so subsequent clicks on the trigger are not even executed.
 *
 * This implementation assures that the remote call is made each time the trigger button is clicked.
 * Caching is then done on the dispatch level, for example, at
 * {@link org.activityinfo.client.dispatch.remote.cache.AdminEntityCache}
 *
 * @author Alex Bertram
 */
public class RemoteComboBox<T extends ModelData> extends ComboBox<T> {

    @Override
    public void doQuery(String q, boolean forceAll) {
		if (forceAll && getSelection().size() <= 0) {
			store.getLoader().load();
		} else {
			store.filter(getDisplayField(), q);
		}
		expand();
    }
}
