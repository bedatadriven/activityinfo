package org.activityinfo.client.page.config;

import java.util.List;

import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;

import com.google.gwt.user.client.ui.HasValue;

public interface ShowLockedPeriodsView extends HasValue<List<LockedPeriodDTO>> {
	public void setActivityFilter(ActivityDTO activity);
	public void setHeader(String header);
}
