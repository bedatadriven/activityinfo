package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.IndicatorRow;

import java.util.List;

/**
 * @author Alex Bertram
 */
public class MonthlyReportResult extends ListResult<IndicatorRow> {

    public MonthlyReportResult() {
    }

    public MonthlyReportResult(List<IndicatorRow> data) {
        super(data);
    }
}
