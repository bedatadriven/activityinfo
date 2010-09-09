package org.sigmah.client.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.sigmah.client.dto.ClientDTOMapper;
import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.DTOMapper;
import org.sigmah.shared.dto.UserDatabaseDTO;

public class DTOMapperTest {
	
	@Test
    public void testMapDB() throws SQLException {
		
		
		UserDatabaseDTO dbdto = new UserDatabaseDTO();
		
		for (int i=0; i < 10; i++) {
			Activity act = new Activity();
			act.setId(i);
			act.setName("name_" + i);
			act.setCategory("category_" + i);	
			DTOMapper mapper = new ClientDTOMapper();
			ActivityDTO adto = mapper.map(act, ActivityDTO.class);
			dbdto.getActivities().add(adto);
		}
		
		Assert.assertTrue(dbdto.getActivities().size() == 10);
		
    }
	
	
	@Test
    public void testCountryMapper() throws SQLException {
		DTOMapper mapper = new ClientDTOMapper();
	
		Country country = new Country();
		Set<LocationType> types = new HashSet<LocationType>(); 
		LocationType l1 = new LocationType();
		l1.setId(1);
		LocationType l2 = new LocationType();
		l2.setId(2);
		types.add(l1);
		types.add(l2);
		country.setLocationTypes(types);
		Assert.assertTrue(country.getLocationTypes().size() == 2);
		
		CountryDTO dto = mapper.map(country, CountryDTO.class);
		//Assert.assertTrue(dto.);
		Assert.assertTrue(dto.getLocationTypes().size() == 2);
		
    }
}