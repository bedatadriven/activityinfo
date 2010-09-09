package org.sigmah.shared.dto;

import org.sigmah.shared.command.handler.CommandHandler;

public interface DTOMapper {

	public <T> T  map(Object o, Class<T>  c);
	
}

