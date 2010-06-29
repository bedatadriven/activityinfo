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

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.CountryDAO;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.domain.Country;
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
