package org.sigmah.server.mail;

import com.google.inject.AbstractModule;

public class MailSenderStubModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MailSender.class).to(MailSenderStub.class);
	}

}
