package org.activityinfo.server.util.beanMapping;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import com.bedatadriven.rebar.time.calendar.LocalDate;

public class LocalDateConverter implements CustomConverter {

    @Override
    public Object convert(Object dest, Object src, Class<?> destClass,
        Class<?> srcClass) {
        if (src == null) {
            return null;
        } else if (src instanceof LocalDate && destClass == Date.class) {
            return ((LocalDate) src).atMidnightInMyTimezone();
        } else if (src instanceof Date && destClass == LocalDate.class) {
            return new LocalDate((Date) src);
        } else {
            throw new MappingException(
                "Converter LocalDateConverter used incorrectly. Arguments passed in were:"
                    + dest + " and " + src);
        }
    }

}
