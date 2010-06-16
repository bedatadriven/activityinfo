package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.shared.command.GetMonthlyReports;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.UpdateMonthlyReports;
import org.activityinfo.shared.command.result.MonthlyReportResult;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class MonthlyReportsTest extends CommandTestCase {


    @Test
    public void testMonthCompare() throws Exception {

        Month feb = new Month(2009, 2);

        Month maxMonth = new Month(2009, 2);

        Assert.assertEquals(0, maxMonth.compareTo(feb));
    }

    @Test
    public void testGetReports() throws Exception {

        GetMonthlyReports cmd = new GetMonthlyReports(6);
        cmd.setStartMonth(new Month(2009, 1));
        cmd.setEndMonth(new Month(2009, 2));

        MonthlyReportResult result = execute(cmd);

        Assert.assertEquals(1, result.getData().size());
        Assert.assertEquals(35, result.getData().get(0).getValue(2009, 1).intValue());
        Assert.assertEquals(70, result.getData().get(0).getValue(2009, 2).intValue());
    }

    @Test
    public void testGetReportsWhenEmpty() throws Exception {

        GetMonthlyReports cmd = new GetMonthlyReports(7);
        cmd.setStartMonth(new Month(2009, 1));
        cmd.setEndMonth(new Month(2009, 2));

        MonthlyReportResult result = execute(cmd);

        Assert.assertEquals(1, result.getData().size());
    }

    @Test
    public void testUpdate() throws Exception {
        ArrayList<UpdateMonthlyReports.Change> changes = new ArrayList<UpdateMonthlyReports.Change>();
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 1), 45.0));
        changes.add(new UpdateMonthlyReports.Change(6, new Month(2009, 3), 22.0));


        execute(new UpdateMonthlyReports(6, changes));

        // verify that that changes have been made
        GetMonthlyReports cmd = new GetMonthlyReports(6);
        cmd.setStartMonth(new Month(2009, 1));
        cmd.setEndMonth(new Month(2009, 3));

        MonthlyReportResult result = execute(cmd);

        Assert.assertEquals(1, result.getData().size());
        Assert.assertEquals(45, result.getData().get(0).getValue(2009, 1).intValue());
        Assert.assertEquals(70, result.getData().get(0).getValue(2009, 2).intValue());
        Assert.assertEquals(22, result.getData().get(0).getValue(2009, 3).intValue());
    }


}
