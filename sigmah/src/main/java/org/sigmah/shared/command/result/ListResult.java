/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class of <code>CommandResult</code>s that are compatible with the GXT loading interface.
 *
 * @see com.extjs.gxt.ui.client.data.ListLoadResult
 * @see com.extjs.gxt.ui.client.data.ModelData
 *
 * @param <D> The type of item that the list will contain.
 */
public abstract class ListResult<D extends ModelData> implements CommandResult, ListLoadResult<D> {

	private List<D> data;

	protected ListResult() {
	    data = new ArrayList<D>();	
	}
	
	public ListResult(List<D> data) {
		this.data = data;
	}

    public ListResult(ListResult<D> result) {
        this.data = new ArrayList<D>(result.getData());
    }

    @Override
	public List<D> getData() {
		return data;
	}	
}
