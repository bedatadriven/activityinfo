/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.mock.MockJPAModule;
import org.sigmah.shared.dao.CountryDAO;
import org.sigmah.shared.domain.Country;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({MockJPAModule.class})
public class CountryDAOTest extends DaoTest{

	@Inject
    protected CountryDAO countryDAO;
    
	public String getResourcePath() {
		return "/dbunit/multicountry.db.xml";
	}
	
	@Test
	public void testDbSetup() throws SQLException {
    	Connection conn = connectionProvider.getConnection();
    	Statement stmt = conn.createStatement();
    	ResultSet rs = stmt.executeQuery("select countryId, name, x1, y1, x2, y2 from Country order by name");
    	rs.next();	
    	assertThat(rs.getString(2), equalTo("Azerbaijan"));
    	rs.last();
    	assertThat(rs.getString(2), equalTo("Rdc"));
	}
	
	@Test
    public void testQueryAll() throws SQLException {
        List<Country> countries = countryDAO.queryAllCountriesAlphabetically();    
        assertThat(countries.size(), is(equalTo(3)));
        assertThat(countries.get(0).getName(), equalTo("Azerbaijan"));
    }
}
