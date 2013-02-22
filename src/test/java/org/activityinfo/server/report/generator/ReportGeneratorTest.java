package org.activityinfo.server.report.generator;

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

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.date.DateUtilCalendarImpl;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.util.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ReportGeneratorTest {

    private <T> T createAndReplayMock(Class<T> clazz) {
        T mock = createNiceMock(clazz);
        replay(mock);
        return mock;
    }

    @Test
    public void testFileName() {

        // Input user
        User user = new User();
        user.setLocale("en");

        // Input test data: report model + parameter
        Report report = new Report();
        report.setFileName("Report ${DATE_RANGE} of Activities");

        // Input test data: parameter values
        DateUtil dateUtil = new DateUtilCalendarImpl();
        DateRange dateRange = dateUtil.monthRange(2009, 1);

        // class under test
        ReportGenerator generator = new ReportGenerator(null, null, null, null,
            null);

        generator.generate(user, report, null, dateRange);

        // VERIFY correct file name

        Assert.assertEquals("Report Jan 2009 of Activities", report
            .getContent().getFileName());

    }
}
