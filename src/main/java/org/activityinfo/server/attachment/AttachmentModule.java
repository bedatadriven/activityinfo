package org.activityinfo.server.attachment;

import com.google.inject.servlet.ServletModule;

public class AttachmentModule extends ServletModule {

	@Override
	protected void configureServlets() {
        serve("/ActivityInfo/attachment").with(AttachmentServlet.class);
        bind(AttachmentService.class).to(AppEngineAttachmentService.class);
	}
	
	

}
