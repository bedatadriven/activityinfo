/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.LocationType;

import java.util.List;


/**
 * Data Access Object for {@link org.sigmah.shared.domain.Country} objects. Implemented by
 * {@link org.sigmah.server.dao.hibernate.DAOInvocationHandler proxy},
 * see the Country class for query definitions.
 *
 * @author Alex Bertram
 */
public interface CountryDAO extends DAO <Country, Integer> {

    /**
     * Returns a list of Countries in alphabetical order.
     * See {@link org.sigmah.shared.domain.Country} for query definition
     *
     * @return  a list of Countries in alphabetical order
     */
    List<Country> queryAllCountriesAlphabetically();
    
   

}
