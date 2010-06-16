package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.SiteDTO;

import java.util.Arrays;
import java.util.List;

/**
 * Result from the GetSites command
 *
 * @see org.activityinfo.shared.command.GetSites
 *
 * @author Alex Bertram
 */
public class SiteResult extends PagingResult<SiteDTO> {
    
    public SiteResult() {

    }

    public SiteResult(List<SiteDTO> data) {
        super(data);
    }

    public SiteResult(SiteDTO... sites) {
        super(Arrays.asList(sites));
    }

    public SiteResult(List<SiteDTO> data, int offset, int totalCount) {
        super(data, offset, totalCount);
    }
}
