package org.activityinfo.client.page.common.filter;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import org.activityinfo.client.Application;
import org.activityinfo.shared.report.model.ParameterizedFilter;

import java.util.Date;
/*
 * @author Alex Bertram
 */

public class DateRangePanel extends FormPanel {
    private DateField date1;
    private DateField date2;

    public DateRangePanel() {

        setHeading(Application.CONSTANTS.filterByDate());
        setIcon(Application.ICONS.filter());

        setLabelWidth(100);
        setFieldWidth(100);
    
        date1 = new DateField();
        date1.setFieldLabel(Application.CONSTANTS.fromDate());
        add(date1);

        date2 = new DateField();
        date2.setFieldLabel(Application.CONSTANTS.toDate());
        add(date2);
    }

    public Date getMinDate() {
        return date1.getValue();
    }

    public Date getMaxDate() {
        return date2.getValue();
    }

    public void updateFilter(ParameterizedFilter filter) {
        filter.setMinDate(date1.getValue());
        filter.setMaxDate(date2.getValue());
    }
}
