package org.sigmah.server.command.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.CreateSubscribe;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.ReportSubscriber;

import com.google.inject.Inject;

public class CreateSubscribeHandler implements CommandHandler<CreateSubscribe> {

	private final EntityManager em;

	@Inject
	public CreateSubscribeHandler(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommandResult execute(CreateSubscribe cmd, User executingUser)
			throws CommandException {
		List<ReportSubscriber> emailList = cmd.getEmailsList();

		List<String> emails = new ArrayList<String>();
		for (ReportSubscriber email : emailList) {
			emails.add(email.getEmail());
		}
		Query query = em.createQuery(
				"select u from User u where u.email in (:email)").setParameter(
				"email", emails);

		List<User> users = query.getResultList();

		for (ReportSubscriber email : emailList) {

			User newUser = new User();
			newUser.setEmail(email.getEmail());

			if (users.contains(newUser)) {

				int userId = users.get(users.indexOf(newUser)).getId();
				ReportSubscription sub = new ReportSubscription(
						em.getReference(ReportDefinition.class,
								cmd.getReportTemplateId()), em.getReference(
								User.class, userId));
				sub.setSubscribed(true);
				sub.setInvitingUser(executingUser);

				em.persist(sub);

			} else {

				newUser.setName("");
				newUser.setFirstName(null);
				newUser.setHashedPassword(null);
				newUser.setNewUser(true);
				newUser.setLocale(executingUser.getLocale());

				em.persist(newUser);

				ReportSubscription sub = new ReportSubscription(
						em.getReference(ReportDefinition.class,
								cmd.getReportTemplateId()), em.getReference(
								User.class, newUser.getId()));
				sub.setSubscribed(true);
				sub.setInvitingUser(executingUser);

				em.persist(sub);
			}
		}

		return new VoidResult();
	}

}
