package org.activityinfo.shared.command.result;

import java.util.List;
import java.util.Collections;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.DTO;

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
