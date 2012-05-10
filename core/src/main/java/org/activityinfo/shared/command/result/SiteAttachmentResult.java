package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.SiteAttachmentDTO;

import com.extjs.gxt.ui.client.data.ListLoadResult;

public class SiteAttachmentResult implements CommandResult, ListLoadResult<SiteAttachmentDTO> {
    
	private List<SiteAttachmentDTO> data;


    /** Required for serialization */
    public SiteAttachmentResult() {
        
    }

    public SiteAttachmentResult(List<SiteAttachmentDTO> dtos) {
        this.data = dtos;
    }
    @Override
    public List<SiteAttachmentDTO> getData() {
        return data;
    }
    public void setData(List<SiteAttachmentDTO> data) {
        this.data = data;
    }
    
    
}
