package org.sigmah.client.page.config;

import java.util.List;

import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;

import com.google.gwt.user.client.ui.HasValue;

public interface ShowLockedPeriodsView extends HasValue<List<LockedPeriodDTO>> {
	public void setActivityFilter(ActivityDTO activity);
}
