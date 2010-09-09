/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.shared.domain.AdminLevel;
import org.sigmah.shared.domain.Bounds;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({BeanMappingModule.class})
public class CountryNameOnlyBeanMappingTest {

    @Inject
    Mapper mapper;

    @Test
    public void countryNameOnlyMapping() {

        Country country = new Country();
        country.setId(1);
        country.setName("Haiti");
        country.getLocationTypes().add(new LocationType());
        country.getAdminLevels().add(new AdminLevel(1, country, "Province", false));
        country.setBounds(new Bounds());

        CountryDTO dto = mapper.map(country, CountryDTO.class, "countryNameOnly");

        assertThat(dto.getName(), equalTo(country.getName()));
        assertTrue(dto.getAdminLevels().isEmpty());
        assertTrue(dto.getLocationTypes().isEmpty());
    }
}
