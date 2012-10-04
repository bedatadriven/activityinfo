package org.activityinfo.server.command;

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
