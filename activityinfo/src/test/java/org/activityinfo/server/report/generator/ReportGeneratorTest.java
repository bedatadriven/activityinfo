package org.activityinfo.server.report.generator;

import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.shared.report.model.DateUnit;
import org.activityinfo.shared.report.model.Parameter;
import org.activityinfo.shared.report.model.Report;
import org.junit.Test;
import org.junit.Assert;
import static org.easymock.EasyMock.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

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

        // Input test data: report model + parameter
		Report report = new Report();
		Parameter param = new Parameter();
		param.setName("MONTH");
		param.setType(Parameter.Type.DATE);
		param.setDateUnit(DateUnit.MONTH);
		report.addParameter(param);
		report.setFileName("Report ${MONTH} of Activities");

        // Input test data: parameter values
		Map<String, Object> paramValues = new HashMap<String, Object>();
		paramValues.put("MONTH", new Date(0));

		// class under test
        ReportGenerator generator = new ReportGenerator(null, null, null, null, null);

        generator.generate(user, report, null, paramValues);

        // VERIFY correct file name

		Assert.assertEquals("Report Jan 1970 of Activities", report.getContent().getFileName());

    }
}
