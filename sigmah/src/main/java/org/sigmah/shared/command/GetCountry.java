/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.dto.CountryDTO;

/**
 * Retrieves a country by ID.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetCountry implements Command<CountryDTO> {
    private int countryId;

    public GetCountry() {
    }

    public GetCountry(int countryId) {
        this.countryId = countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }
}
