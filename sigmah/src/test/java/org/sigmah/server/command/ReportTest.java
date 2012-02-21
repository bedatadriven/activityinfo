/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.shared.command.GetReports;
import org.sigmah.shared.command.UpdateSubscription;
import org.sigmah.shared.command.result.ReportsResult;
import org.sigmah.shared.dto.ReportMetadataDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportFrequency;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class ReportTest extends CommandTestCase {


    @Test
    public void testReport() throws CommandException {
        ReportsResult reports = execute(new GetReports());

        Assert.assertEquals("records returned", 1, reports.getData().size());

        ReportMetadataDTO report = reports.getData().get(0);

        Assert.assertEquals("report title", "Report 1", report.getTitle());
//        Assert.assertEquals("owner", "Alex", report.getOwnerName());
//        Assert.assertEquals("frequency", ReportFrequency.Monthly, report.getFrequency());
//        Assert.assertEquals("report day", Report.LAST_DAY_OF_MONTH, (int) report.getDay());
//        Assert.assertEquals("subscribed", true, report.isSubscribed());
    }


    @Test
    @Ignore("in progress")
    public void testReportNoSubscription() throws CommandException {
        setUser(2); // Bavon

        ReportsResult reports = execute(new GetReports());

        Assert.assertEquals("records returned", 1, reports.getData().size());

        ReportMetadataDTO report = reports.getData().get(0);

        Assert.assertEquals("subscribed", false, report.isSubscribed());

    }

    @Test
    @Ignore("in progress")
    public void testNewSubscription() throws CommandException {
        setUser(2);

        // update
        execute(new UpdateSubscription(1, true));

        // check for change

        ReportsResult reports = execute(new GetReports());

        Assert.assertEquals("records returned", 1, reports.getData().size());
        Assert.assertTrue("subscribed", reports.getData().get(0).isSubscribed());

    }


    @Test
    @Ignore("in progress")
    public void testUpdateSubscription() throws CommandException {
        // update
        execute(new UpdateSubscription(1, false));

        // check for change

        ReportsResult reports = execute(new GetReports());

        Assert.assertEquals("records returned", 1, reports.getData().size());
        Assert.assertFalse("subscribed", reports.getData().get(0).isSubscribed());

    }

}
