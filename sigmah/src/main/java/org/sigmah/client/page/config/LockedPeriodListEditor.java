package org.sigmah.client.page.config;

import java.util.Set;

import org.sigmah.client.mvp.CrudView;
import org.sigmah.shared.domain.LockedPeriod;
import org.sigmah.shared.dto.LockedPeriodDTO;

public interface LockedPeriodListEditor extends CrudView<LockedPeriodDTO> {
	public void setPeriods(Set<LockedPeriod> lockedPeriods);
}	