package org.activityinfo.client.importer;

import org.activityinfo.shared.dto.SiteDTO;

public class IndicatorColumnBinder implements ColumnBinder<SiteDTO> {

    private final int columnIndex;
    private final int indicatorId;
    
    public IndicatorColumnBinder(int columnIndex, int indicatorId) {
        super();
        this.columnIndex = columnIndex;
        this.indicatorId = indicatorId;
    }

    @Override
    public void bind(String[] row, SiteDTO model) {
        try {
            double value = Double.parseDouble(row[columnIndex]);
            model.setIndicatorValue(indicatorId, value);
        } catch(NumberFormatException e) {
            model.setIndicatorValue(indicatorId, null);
        }
    }

}
