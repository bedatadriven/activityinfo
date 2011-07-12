package org.sigmah.shared.dto;

public interface DTOMapper {

	public <T> T  map(Object o, Class<T>  c);
	
}

