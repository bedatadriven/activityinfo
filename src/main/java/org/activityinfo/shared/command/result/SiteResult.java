package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.SiteModel;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
/*
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
