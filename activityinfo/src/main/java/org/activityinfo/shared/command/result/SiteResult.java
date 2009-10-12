package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.SiteModel;

import java.util.Arrays;
import java.util.List;

/**
 * Result from the GetSites command
 *
 * @see org.activityinfo.shared.command.GetSites
 *
 * @author Alex Bertram
 */
public class SiteResult extends PagingResult<SiteModel> {
    
    public SiteResult() {

    }

    public SiteResult(List<SiteModel> data) {
        super(data);
    }

    public SiteResult(SiteModel... sites) {
        super(Arrays.asList(sites));
    }

    public SiteResult(List<SiteModel> data, int offset, int totalCount) {
        super(data, offset, totalCount);
    }
}
