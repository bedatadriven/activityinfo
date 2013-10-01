package org.activityinfo.server.event.sitechange;

import java.text.DecimalFormat;

import org.activityinfo.client.page.entry.form.IndicatorValueFormatter;

public class JreIndicatorValueFormatter implements IndicatorValueFormatter {
    @Override
    public String format(Double value) {
        return new DecimalFormat("#,##0.####").format(value);
    }
}