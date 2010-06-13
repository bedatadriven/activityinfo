package org.activityinfo.server.endpoint.gwtrpc;

import junit.framework.Assert;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.shared.command.GetReportTemplates;
import org.activityinfo.shared.command.UpdateSubscription;
import org.activityinfo.shared.command.result.ReportTemplateResult;
import org.activityinfo.shared.dto.ReportDefinitionDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportFrequency;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class ReportTest extends CommandTestCase {


    @Test
    public void testReport() throws CommandException {
        ReportTemplateResult reports = execute(new GetReportTemplates());

        Assert.assertEquals("records returned", 1, reports.getData().size());

        ReportDefinitionDTO report = reports.getData().get(0);

        Assert.assertEquals("report title", "Report 1", report.getTitle());
        Assert.assertEquals("owner", "Alex", report.getOwnerName());
        Assert.assertEquals("frequency", ReportFrequency.Monthly, report.getFrequency());
        Assert.assertEquals("report day", Report.LAST_DAY_OF_MONTH, (int) report.getDay());
        Assert.assertEquals("subscribed", true, report.isSubscribed());
    }


    @Test
    public void testReportNoSubscription() throws CommandException {
        setUser(2); // Bavon

        ReportTemplateResult reports = execute(new GetReportTemplates());

        Assert.assertEquals("records returned", 1, reports.getData().size());

        ReportDefinitionDTO report = reports.getData().get(0);

        Assert.assertEquals("subscribed", false, report.isSubscribed());

    }

    @Test
    public void testNewSubscription() throws CommandException {
        setUser(2);

        // update
        execute(new UpdateSubscription(1, true));

        // check for change

        ReportTemplateResult reports = execute(new GetReportTemplates());

        Assert.assertEquals("records returned", 1, reports.getData().size());
        Assert.assertTrue("subscribed", reports.getData().get(0).isSubscribed());

    }


    @Test
    public void testUpdateSubscription() throws CommandException {
        // update
        execute(new UpdateSubscription(1, false));

        // check for change

        ReportTemplateResult reports = execute(new GetReportTemplates());

        Assert.assertEquals("records returned", 1, reports.getData().size());
        Assert.assertFalse("subscribed", reports.getData().get(0).isSubscribed());

    }

}
