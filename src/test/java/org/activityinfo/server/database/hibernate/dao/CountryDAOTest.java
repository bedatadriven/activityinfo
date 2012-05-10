/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.dao.CountryDAO;
import org.activityinfo.server.database.hibernate.entity.Country;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

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
