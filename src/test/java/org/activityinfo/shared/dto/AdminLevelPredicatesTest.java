package org.activityinfo.shared.dto;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
        AdminLevelDTO zoneSante = new AdminLevelDTO(6, province,
            "Zone de Sante");
        AdminLevelDTO aireSante = new AdminLevelDTO(7, zoneSante,
            "Aire de Sante");

        List<AdminLevelDTO> unsorted = Lists.newArrayList(province, district,
            territoire, secteur, groupement, zoneSante, aireSante);

        List<AdminLevelDTO> sorted = AdminLevelPredicates
            .breadthFirstSort(unsorted);

        assertThat(sorted, equalTo((List) Lists.newArrayList(province,
            district, zoneSante, territoire, aireSante, secteur, groupement)));
    }

}
