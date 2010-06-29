/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.SiteDTO;

import java.util.Arrays;
import java.util.List;

/**
 * Result from the GetSites command
 *
 * @see org.sigmah.shared.command.GetSites
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
