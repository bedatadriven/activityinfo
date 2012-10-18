package org.activityinfo.server.report.output;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AppEngineStorageServletTest {

	@Test
	public void parseKey() {
		String uri = "/generated/XYZ123/My+FileName.png";
		assertThat(AppEngineStorageServlet.parseBlobKey(uri), equalTo("XYZ123"));
	}
	
}
