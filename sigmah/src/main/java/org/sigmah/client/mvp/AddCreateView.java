package org.sigmah.client.mvp;

import org.sigmah.shared.dto.DTO;

public interface AddCreateView<M extends DTO> 
	extends 
		View<M>,
		CanCreate<M>,
		CanUpdate<M> {
}
