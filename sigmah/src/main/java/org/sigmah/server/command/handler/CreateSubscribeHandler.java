package org.sigmah.server.command.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private ReportDefinition reportDef;

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
		
		Query getUser = em.createQuery(
				"select u from User u where u.email in (:email)").setParameter(
				"email", emails);

		Query getSubscriptions = em.createQuery(
				"select r from ReportDefinition r where r.id in (:id)")
				.setParameter("id", cmd.getReportTemplateId());

		List<User> users = getUser.getResultList();
		
		reportDef = (ReportDefinition) getSubscriptions
				.getSingleResult();
		
		Set<ReportSubscription> subs = new HashSet<ReportSubscription>(0);
		
		for(ReportSubscription sub : reportDef.getSubscriptions()){
			subs.add(sub);
		}

		for (ReportSubscriber newSub : emailList) {

			User newUser = new User();
			newUser.setEmail(newSub.getEmail());

			if (users.contains(newUser)) {

				int userId = users.get(users.indexOf(newUser)).getId();
				
				for( ReportSubscription rs  : subs ){
					if(rs.getUser().equals(newUser)){
						rs.setSubscribed(newSub.isSubscribed());
					}
					else{
						ReportSubscription sub = new ReportSubscription(
								em.getReference(ReportDefinition.class,
										cmd.getReportTemplateId()), em.getReference(
										User.class, userId));
						sub.setSubscribed(newSub.isSubscribed());
						sub.setInvitingUser(executingUser);
						
						em.persist(sub);
					}
				}
				
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
		reportDef.setSubscriptions(subs);
		reportDef.setFrequency(cmd.getReportFrequency());
		reportDef.setDay(cmd.getDay());
		em.persist(reportDef);
		return new VoidResult();
	}

}
