package org.sigmah.client.page.config;

import java.util.Set;

import org.sigmah.client.mvp.CrudView;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

public interface LockedPeriodListEditor extends CrudView<LockedPeriodDTO, UserDatabaseDTO> {
	public void setPeriods(Set<LockedPeriodDTO> lockedPeriods);
}	