/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.ReportGenerator;
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
        ReportGenerator generator = new ReportGenerator(null, null, null, null, null);

        generator.generate(user, report, null, dateRange);

        // VERIFY correct file name

		Assert.assertEquals("Report Jan 2009 of Activities", report.getContent().getFileName());

    }
}
