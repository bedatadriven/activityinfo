/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.CountryDAO;
import org.activityinfo.server.domain.Country;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetCountries;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CountryResult;
import org.activityinfo.shared.dto.CountryModel;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

public class GetCountriesHandler implements CommandHandler<GetCountries> {

    private final CountryDAO countryDAO;
    private final Mapper mapper;

    @Inject
    public GetCountriesHandler(CountryDAO countryDAO, Mapper mapper) {
        this.countryDAO = countryDAO;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetCountries cmd, User user) throws CommandException {
        return new CountryResult(mapToDtos(
                countryDAO.queryAllCountriesAlphabetically()
        ));
    }

    private ArrayList<CountryModel> mapToDtos(List<Country> countries) {
        ArrayList<CountryModel> dtos = new ArrayList<CountryModel>();
        for(Country country : countries) {
            dtos.add(mapper.map(country, CountryModel.class, "countryNameOnly"));
        }
        return dtos;
    }
}
