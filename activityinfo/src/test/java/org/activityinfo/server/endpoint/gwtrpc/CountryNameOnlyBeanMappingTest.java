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

package org.activityinfo.server.endpoint.gwtrpc;

import com.google.inject.Inject;
import org.activityinfo.server.util.BeanMappingModule;
import org.activityinfo.server.domain.AdminLevel;
import org.activityinfo.server.domain.Bounds;
import org.activityinfo.server.domain.Country;
import org.activityinfo.server.domain.LocationType;
import org.activityinfo.shared.dto.CountryModel;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

        CountryModel dto = mapper.map(country, CountryModel.class, "countryNameOnly");

        assertThat(dto.getName(), equalTo(country.getName()));
        assertTrue(dto.getAdminLevels().isEmpty());
        assertTrue(dto.getLocationTypes().isEmpty());
    }
}
