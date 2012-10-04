package org.activityinfo.shared.report.content;

import org.activityinfo.client.i18n.I18N;

public enum TargetCategory implements DimensionCategory {
	REALIZED, TARGETED;

	@Override
	public Comparable<Integer> getSortKey() {
		return ordinal();
	}

	@Override
	public String getLabel() {
		switch (this) {
		case REALIZED:
			return I18N.CONSTANTS.realized();
		case TARGETED:
			return I18N.CONSTANTS.targeted();
		default:
			return name();
		}
	}
}
