/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import java.util.List;

import org.sigmah.server.database.hibernate.entity.Country;


/**
 * Data Access Object for {@link org.activityinfo.server.database.hibernate.entity.Country} objects. Implemented by
 * {@link org.activityinfo.server.database.hibernate.dao.DAOInvocationHandler proxy},
 * see the Country class for query definitions.
 *
 * @author Alex Bertram
 */
public interface CountryDAO extends DAO <Country, Integer> {

    /**
     * Returns a list of Countries in alphabetical order.
     * See {@link org.activityinfo.server.database.hibernate.entity.Country} for query definition
     *
     * @return  a list of Countries in alphabetical order
     */
    List<Country> queryAllCountriesAlphabetically();
    
   

}
