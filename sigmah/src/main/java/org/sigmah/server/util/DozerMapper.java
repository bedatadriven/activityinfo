package org.sigmah.server.util;

import org.dozer.Mapper;
import org.sigmah.shared.dto.DTOMapper;

import com.google.inject.Inject;

public class DozerMapper implements DTOMapper {

	private Mapper mapper;

	@Inject
	public DozerMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public <T> T map(Object o, Class<T> c) {
		return mapper.map(o,c);
	}

}
