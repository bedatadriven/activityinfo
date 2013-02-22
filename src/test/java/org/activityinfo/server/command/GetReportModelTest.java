package org.activityinfo.server.command;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.GetReportModel;
import org.activityinfo.shared.dto.ReportDTO;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/getreporttests.db.xml")
public class GetReportModelTest extends CommandTestCase {

    @Test
    public void selectReportOnly() {
        setUser(1);
        ReportDTO result = execute(new GetReportModel(3));
        assertNotNull(result.getReport());
        assertEquals("Report 3", result.getReport().getTitle());
        assertNull(result.getReportMetadataDTO());
    }

    @Test
    public void selectReportOnly2() {
        setUser(1);
        ReportDTO result = execute(new GetReportModel(3, false));
        assertNotNull(result.getReport());
        assertEquals("Report 3", result.getReport().getTitle());
        assertNull(result.getReportMetadataDTO());
    }

    @Test
    public void selectReportWithMetadata() {
        setUser(1);
        ReportDTO result = execute(new GetReportModel(3, true));
        assertNotNull(result.getReport());
        assertEquals("Report 3", result.getReport().getTitle());

        assertNotNull(result.getReportMetadataDTO());
        assertEquals("Alex", result.getReportMetadataDTO().getOwnerName());
    }
}
