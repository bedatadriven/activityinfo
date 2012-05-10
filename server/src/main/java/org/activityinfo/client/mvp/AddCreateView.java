package org.activityinfo.client.mvp;

import org.activityinfo.shared.dto.DTO;

public interface AddCreateView<M extends DTO> 
	extends 
		View<M>,
		CanCreate<M>,
		CanUpdate<M> {
}
