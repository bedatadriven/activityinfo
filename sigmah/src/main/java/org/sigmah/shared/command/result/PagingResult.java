/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

import java.util.List;

/**
 * Abstract base class for <code>CommandResult</code>s that are compatible with the
 * GXT loading framework.
 *
 * @see com.extjs.gxt.ui.client.data.PagingLoadResult
 *
 * @param <D> The type of model contained in the list
 */
public abstract class PagingResult<D extends ModelData> extends ListResult<D>
        implements CommandResult, PagingLoadResult<D> {

	private int offset;
	private int totalLength;
	
	protected PagingResult() {
		
	}
	
	public PagingResult(List<D> data) {
		super(data);
        this.offset = 0;
        this.totalLength = data.size();
	}
	
	public PagingResult(List<D> data, int offset, int totalCount) {
		super(data);
		this.offset = offset;
		this.totalLength = totalCount;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public int getTotalLength() {
		return totalLength;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

}
