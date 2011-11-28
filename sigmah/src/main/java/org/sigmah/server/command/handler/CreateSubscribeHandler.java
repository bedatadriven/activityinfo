package org.sigmah.server.command.handler;

import javax.persistence.EntityManager;

import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class CreateSubscribeHandler implements CommandHandler<CreateSubscribe> {

	private EntityManager em;

	@Inject
	public CreateSubscribeHandler(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommandResult execute(CreateSubscribe cmd, User user)
			throws CommandException {

		ReportSubscription sub = new ReportSubscription(em.getReference(
				ReportDefinition.class, cmd.getReportTemplateId()),
				em.getReference(User.class, 0));

		sub.setSubscriberEmail(cmd.getEmailsList());
		
		em.persist(sub);

		return new VoidResult();
	}

}
