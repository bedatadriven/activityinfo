/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.dozer.Mapper;
import org.sigmah.server.dao.CountryDAO;
import org.sigmah.server.domain.Country;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetCountries;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CountryResult;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.exception.CommandException;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class GetCountriesHandler implements CommandHandler<GetCountries> {

    private final CountryDAO countryDAO;
    private final Mapper mapper;
    
    private final EntityManager entityManager;

    @Inject
    public GetCountriesHandler(CountryDAO countryDAO, Mapper mapper, EntityManager entityManager) {
        this.countryDAO = countryDAO;
        this.mapper = mapper;
        
        this.entityManager = entityManager;
    }

    @Override
    public CommandResult execute(GetCountries cmd, User user) throws CommandException {
        if(cmd.isContainingProjects()) {
            return new CountryResult(mapToDtos(entityManager.createQuery("SELECT c FROM Country c WHERE c IN (SELECT co.id FROM Project p, IN(p.country) co)").getResultList()));
        }
        
        return new CountryResult(mapToDtos(
                countryDAO.queryAllCountriesAlphabetically()
        ));
    }

    private ArrayList<CountryDTO> mapToDtos(List<Country> countries) {
        ArrayList<CountryDTO> dtos = new ArrayList<CountryDTO>();
        for(Country country : countries) {
            dtos.add(mapper.map(country, CountryDTO.class, "countryNameOnly"));
        }
        return dtos;
    }
}
