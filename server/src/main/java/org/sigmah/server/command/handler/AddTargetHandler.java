package org.sigmah.server.command.handler;

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.shared.command.AddTarget;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.TargetDTO;
import org.activityinfo.shared.exception.CommandException;
import org.sigmah.server.database.hibernate.entity.AdminEntity;
import org.sigmah.server.database.hibernate.entity.Partner;
import org.sigmah.server.database.hibernate.entity.Project;
import org.sigmah.server.database.hibernate.entity.Target;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;

import com.google.inject.Inject;

public class AddTargetHandler implements CommandHandler<AddTarget> {

	private final EntityManager em;

	@Inject
	public AddTargetHandler(EntityManager em) {
		this.em = em;
	}

	@Override
	public CommandResult execute(AddTarget cmd, User user) throws CommandException {

		TargetDTO form = cmd.getTargetDTO();
		UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
		
		Partner partner = null;
		if(form.getPartner() != null){
			partner = em.find(Partner.class, form.getPartner().getId());
		}
		
		Project project  = null;
		if(form.getProject() != null){
			 project = em.find(Project.class, form.getProject().getId());
		}
		
		AdminEntity adminEntity  = null;
		if(form.getAdminEntity() != null){
			adminEntity = em.find(AdminEntity.class, form.getAdminEntity().getId());	
		}
		

		Target target = new Target();
		target.setName(form.getName());
		target.setUserDatabase(db);
		target.setAdminEntity(adminEntity);
		target.setPartner(partner);
		target.setProject(project);
		target.setDate1(new Date());
		target.setDate2(new Date());

		db.setLastSchemaUpdate(new Date());

		em.persist(target);
		em.persist(db);

		db.getTargets().add(target);
		
		if(adminEntity!=null){
			adminEntity.getTargets().add(target);			
		}
		if(project!=null){
			project.getTargets().add(target);	
		}
		if(partner!=null){
			partner.getTargets().add(target);	
		}

		return new CreateResult(target.getId());
	}
}