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

import java.util.Arrays;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.shared.command.result.PartnerResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@Modules({ TestDatabaseModule.class })
public class GetPartnersDimensionHandlerTest extends CommandTestCase2 {

    // empty
    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void testEmptyFilter() throws CommandException {
        PartnerResult result = this.execute();
        assertThat(result.getData().size(), equalTo(0));
    }


    // data entry filter population query
    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void testActivity() throws CommandException {
        PartnerResult result = execute(DimensionType.Activity, 1);
        assertThat(result.getData().size(), equalTo(2));
        assertThat(result.getData().get(0).getName(), equalTo("NRC"));
        assertThat(result.getData().get(1).getName(), equalTo("Solidarites"));
    }

    // report filter population query
    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void testIndicatorEmpty() throws CommandException {
        // empty
        PartnerResult result = execute(DimensionType.Indicator, 103, 675);
        assertThat(result.getData().size(), equalTo(0));
    }

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void testIndicatorValues() throws CommandException {
        // NRC, Solidarites
        PartnerResult result = execute(DimensionType.Indicator, 1, 2);
        assertThat(result.getData().size(), equalTo(2));
        assertThat(result.getData().get(0).getName(), equalTo("NRC"));
        assertThat(result.getData().get(1).getName(), equalTo("Solidarites"));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testIndicatorLinked100() throws CommandException {
        // empty
        PartnerResult result = execute(DimensionType.Indicator, 100);
        assertThat(result.getData().size(), equalTo(0));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testIndicatorLinked1() throws CommandException {
        // NRC, NRC2
        PartnerResult result = execute(DimensionType.Indicator, 1);
        assertThat(result.getData().size(), equalTo(2));
        assertThat(result.getData().get(0).getName(), equalTo("NRC"));
        assertThat(result.getData().get(1).getName(), equalTo("NRC2"));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testIndicatorLinked2() throws CommandException {
        // NRC
        PartnerResult result = execute(DimensionType.Indicator, 2);
        assertThat(result.getData().size(), equalTo(1));
        assertThat(result.getData().get(0).getName(), equalTo("NRC"));
    }

    @Test
    @OnDataSet("/dbunit/sites-linked.db.xml")
    public void testIndicatorLinked12100() throws CommandException {
        // NRC, NRC2
        PartnerResult result = execute(DimensionType.Indicator, 1, 2, 100);
        assertThat(result.getData().size(), equalTo(2));
        assertThat(result.getData().get(0).getName(), equalTo("NRC"));
        assertThat(result.getData().get(1).getName(), equalTo("NRC2"));
    }


    private PartnerResult execute() {
        return this.execute(null, (Integer[]) null);
    }

    private PartnerResult execute(DimensionType type, Integer... params) {
        setUser(1);
        Filter filter = new Filter();
        if (type != null) {
            filter.addRestriction(type, Arrays.asList(params));
        }
        return execute(new GetPartnersDimension(filter));
    }
}
