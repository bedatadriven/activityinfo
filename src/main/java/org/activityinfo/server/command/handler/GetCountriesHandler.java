package org.activityinfo.server.command.handler;

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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.activityinfo.server.database.hibernate.dao.CountryDAO;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetCountries;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CountryResult;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

import com.google.inject.Inject;

public class GetCountriesHandler implements CommandHandler<GetCountries> {

    private final static Logger LOG = Logger
        .getLogger(GetCountriesHandler.class.getName());

    private final CountryDAO countryDAO;
    private final Mapper mapper;

    @Inject
    public GetCountriesHandler(CountryDAO countryDAO, Mapper mapper) {
        this.countryDAO = countryDAO;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandResult execute(GetCountries cmd, User user)
        throws CommandException {
        return new CountryResult(
            mapToDtos(countryDAO.queryAllCountriesAlphabetically()));
    }

    private ArrayList<CountryDTO> mapToDtos(List<Country> countries) {
        ArrayList<CountryDTO> dtos = new ArrayList<CountryDTO>();
        for (Country country : countries) {
            dtos.add(mapper.map(country, CountryDTO.class, "countryNameOnly"));
        }
        return dtos;
    }
}
