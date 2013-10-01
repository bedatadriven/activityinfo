package org.activityinfo.client.importer.column;

import java.util.Date;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.dto.SiteDTO;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateBinding extends ColumnBinding {

    private DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd");
   
    
    @Override
    public String getLabel() {
        return I18N.CONSTANTS.date();
    }

    @Override
    public void bindSite(String value, SiteDTO model) {
        try {
            Date date = format.parse(value);
            model.setDate1(date);
            model.setDate2(date);
            
        } catch(IllegalArgumentException e) {
        }
    }
   
}
