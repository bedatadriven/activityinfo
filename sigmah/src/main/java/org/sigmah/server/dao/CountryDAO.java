/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.server.domain.Country;

import java.util.List;


/**
 * Data Access Object for {@link org.sigmah.server.domain.Country} objects. Implemented by
 * {@link org.sigmah.server.dao.hibernate.DAOInvocationHandler proxy},
 * see the Country class for query definitions.
 *
 * @author Alex Bertram
 */
public interface CountryDAO extends DAO<Country, Integer> {

    /**
     * Returns a list of Countries in alphabetical order.
     * See {@link org.sigmah.server.domain.Country} for query definition
     *
     * @return  a list of Countries in alphabetical order
     */
    List<Country> queryAllCountriesAlphabetically();

}
