package org.activityinfo.client.widget;

import com.google.gwt.i18n.client.NumberFormat;

public class CoordinateFormatter implements CoordinateEditor.NumberFormats {

	private NumberFormat dddFormat;
	private NumberFormat shortFracFormat;
	private NumberFormat intFormat;

	public CoordinateFormatter() {
		dddFormat = NumberFormat.getFormat("+0.000000;-0.000000");
        shortFracFormat = NumberFormat.getFormat("0.00");
        intFormat = NumberFormat.getFormat("0");
	}

	@Override
	public double parseDouble(String s) {
		return NumberFormat.getDecimalFormat().parse(s);
	}

	@Override
	public String formatDDd(double value) {
		return dddFormat.format(value);
	}

	@Override
	public String formatShortFrac(double value) {
		return shortFracFormat.format(value);
	}

	@Override
	public String formatInt(double value) {
		return intFormat.format(value);
	}
}
