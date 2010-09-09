/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.shared.dao.CountryDAO;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.domain.Country;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(InjectionSupport.class)
@Modules({MockHibernateModule.class})
public class CountryDAOTest {

    @Inject
    private CountryDAO countryDAO;

    @Test @OnDataSet("/dbunit/multicountry.db.xml")
    public void testQueryAll() {

        List<Country> countries = countryDAO.queryAllCountriesAlphabetically();

        assertThat(countries.size(), is(equalTo(3)));
        assertThat(countries.get(0).getName(), equalTo("Azerbaijan"));
    }

}
