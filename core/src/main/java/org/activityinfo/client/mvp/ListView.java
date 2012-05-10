package org.activityinfo.client.mvp;

import java.util.List;

import org.activityinfo.shared.dto.DTO;

/*
 * M: the model to perform C/U/D actions upon
 * L: a List of models to display in a list
 * P: the parent model containing the list of models
 */
public interface ListView<M extends DTO, P extends DTO> 
	extends View<M> {
	
	public void setParent(P parent);
	public void setItems(List<M> items);
}
