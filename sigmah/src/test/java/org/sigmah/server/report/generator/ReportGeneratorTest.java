package org.sigmah.server.report.generator;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.server.domain.User;
import org.sigmah.server.util.DateUtilCalendarImpl;
import org.sigmah.shared.date.DateUtil;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.Report;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.replay;

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
