/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

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

    private final static Logger LOG = Logger.getLogger(GetCountriesHandler.class.getName());

    private final CountryDAO countryDAO;
    private final Mapper mapper;


    @Inject
    public GetCountriesHandler(CountryDAO countryDAO, Mapper mapper) {
        this.countryDAO = countryDAO;
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandResult execute(GetCountries cmd, User user) throws CommandException {        
        return new CountryResult(mapToDtos(countryDAO.queryAllCountriesAlphabetically()));
    }

    private ArrayList<CountryDTO> mapToDtos(List<Country> countries) {
        ArrayList<CountryDTO> dtos = new ArrayList<CountryDTO>();
        for (Country country : countries) {
            dtos.add(mapper.map(country, CountryDTO.class, "countryNameOnly"));
        }
        return dtos;
    }
}
