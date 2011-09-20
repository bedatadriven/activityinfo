package org.sigmah.server.util;

import java.util.Date;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import com.bedatadriven.rebar.time.calendar.LocalDate;

public class LocalDateConverter implements CustomConverter {

	@Override
	public Object convert(Object dest, Object src, Class<?> destClass, Class<?> srcClass) {
		if(src == null) {
			return null;
		} else if(src instanceof LocalDate && destClass == Date.class) {
			return ((LocalDate)src).atMidnightInMyTimezone();
		} else if(src instanceof Date && destClass == LocalDate.class ) {
			return new LocalDate( (Date) src );
		} else {
			  throw new MappingException("Converter LocalDateConverter used incorrectly. Arguments passed in were:"
			          + dest + " and " + src); 
		}
	}

}
