package org.sigmah.client.page.entry.form;

import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.SiteDTO;

public interface LocationFormSection extends FormSection<SiteDTO> {

	/**
	 * Returns true if Location DTO is new and has not yet been saved
	 */
	boolean isNew();
	
	LocationDTO getLocation();
	
}
