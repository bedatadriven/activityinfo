package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.IndicatorRowDTO;

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
