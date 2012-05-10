/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.IndicatorRowDTO;

/**
 * @author Alex Bertram
 */
public class MonthlyReportResult extends ListResult<IndicatorRowDTO> {

    public MonthlyReportResult() {
    }

    public MonthlyReportResult(List<IndicatorRowDTO> data) {
        super(data);
    }
}
