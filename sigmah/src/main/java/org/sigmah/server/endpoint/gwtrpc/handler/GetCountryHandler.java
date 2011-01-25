/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetCountry;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.CountryDAO;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.exception.CommandException;

/**
 * Handler for the {@link GetCountry} command.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetCountryHandler implements CommandHandler<GetCountry> {

    private final CountryDAO countryDAO;
    private final Mapper mapper;

    @Inject
    public GetCountryHandler(CountryDAO countryDAO, Mapper mapper) {
        this.countryDAO = countryDAO;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetCountry cmd, User user) throws CommandException {
        final Country country = countryDAO.findById(cmd.getCountryId());
        return mapper.map(country, CountryDTO.class, "countryNameOnly");
    }
    
}
