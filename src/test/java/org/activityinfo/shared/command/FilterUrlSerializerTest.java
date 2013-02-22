package org.activityinfo.shared.command;

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

import org.activityinfo.shared.report.model.DimensionType;
import org.junit.Test;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.common.collect.Sets;

public class FilterUrlSerializerTest {

    @Test
    public void activity() {
        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Activity, 33);

        verifyCorrectSerde(filter);
    }

    @Test
    public void withDates() {
        Filter filter = new Filter();
        filter.getDateRange().setMinDate(new LocalDate(2011, 4, 19));
        filter.getDateRange().setMaxDate(new LocalDate(2012, 3, 31));
        filter.addRestriction(DimensionType.Activity, 1);
        filter.addRestriction(DimensionType.Database, Sets.newHashSet(31, 42));

        verifyCorrectSerde(filter);
    }

    @Test
    public void queryParameter() {
        Filter filter = FilterUrlSerializer
            .fromQueryParameter("Partner 129-Activity 33");
        Filter expected = new Filter();
        expected.addRestriction(DimensionType.Partner, 129);
        expected.addRestriction(DimensionType.Activity, 33);

        assertThat(filter, equalTo(expected));
    }

    private void verifyCorrectSerde(Filter filter) {
        String fragment = FilterUrlSerializer.toUrlFragment(filter);
        Filter deserialized = FilterUrlSerializer.fromUrlFragment(fragment);

        assertThat("deserialized", deserialized, equalTo(filter));
    }

}
