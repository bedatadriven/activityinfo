package org.sigmah.client.mvp;

import org.sigmah.shared.dto.DTO;

public interface ListPresenter<P extends DTO, V extends CrudView<M, P>, M extends DTO> 
	extends 
		Presenter<V, M> {

}
