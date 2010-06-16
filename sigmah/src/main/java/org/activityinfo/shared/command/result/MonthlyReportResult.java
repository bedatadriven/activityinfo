package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.IndicatorRowDTO;

import java.util.List;

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
