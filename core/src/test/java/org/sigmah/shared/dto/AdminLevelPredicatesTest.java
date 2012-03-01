package org.sigmah.shared.dto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class AdminLevelPredicatesTest {

	@Test
	public void breadthFirstSort() {

		AdminLevelDTO province = new AdminLevelDTO(1, "Province");
		AdminLevelDTO district = new AdminLevelDTO(2, province, "District");
		AdminLevelDTO territoire = new AdminLevelDTO(3, district, "Territoire");
		AdminLevelDTO secteur = new AdminLevelDTO(4, territoire, "Secteur");
		AdminLevelDTO groupement = new AdminLevelDTO(5, secteur, "Groupement");
		AdminLevelDTO zoneSante = new AdminLevelDTO(6, province, "Zone de Sante");
		AdminLevelDTO aireSante = new AdminLevelDTO(7, zoneSante, "Aire de Sante");
		
		List<AdminLevelDTO> unsorted = Lists.newArrayList(province, district, territoire, secteur, groupement, zoneSante, aireSante);
		
		List<AdminLevelDTO> sorted = AdminLevelPredicates.breadthFirstSort(unsorted);
		
		assertThat(sorted, equalTo((List)Lists.newArrayList(province, district, zoneSante, territoire, aireSante, secteur, groupement)));		
	}
	
}
