package org.activityinfo.client.importer.column;

import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.util.Format;

public class IndicatorBinding extends ColumnBinding {
    private final IndicatorDTO indicator;

    public IndicatorBinding(IndicatorDTO indicator) {
        super();
        this.indicator = indicator;
    }

    @Override
    public String getLabel() {
        return Format.ellipse(indicator.getName(), 35);
    }

    @Override
    public int hashCode() {
        return indicator.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IndicatorDTO && ((IndicatorDTO)indicator).getId() == indicator.getId();
    }

    @Override
    public void bindSite(String string, SiteDTO site) {
        try {
            double value = Double.parseDouble(string);
            site.setIndicatorValue(indicator.getId(), value);
        } catch(NumberFormatException e) {
        }
    }
    
    
}
