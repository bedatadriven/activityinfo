package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.Partner;
import org.activityinfo.server.database.hibernate.entity.Project;
import org.activityinfo.server.database.hibernate.entity.Target;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.command.AddTarget;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.TargetDTO;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

public class AddTargetHandler implements CommandHandler<AddTarget> {

    private final EntityManager em;

    @Inject
    public AddTargetHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(AddTarget cmd, User user)
        throws CommandException {

        TargetDTO form = cmd.getTargetDTO();
        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());

        Partner partner = null;
        if (form.get("partnerId") != null) {
            partner = em.find(Partner.class, form.get("partnerId"));
        }

        Project project = null;
        if (form.get("projectId") != null) {
            project = em.find(Project.class, form.get("projectId"));
        }

        AdminEntity adminEntity = null;
        // if(form.getAdminEntity() != null){
        // adminEntity = em.find(AdminEntity.class,
        // form.getAdminEntity().getId());
        // }

        Target target = new Target();
        target.setName(form.getName());
        target.setUserDatabase(db);
        target.setAdminEntity(adminEntity);
        target.setPartner(partner);
        target.setProject(project);
        target.setDate1(form.getDate1());
        target.setDate2(form.getDate2());

        db.setLastSchemaUpdate(new Date());

        em.persist(target);
        em.persist(db);

        db.getTargets().add(target);

        // if(adminEntity!=null){
        // adminEntity.getTargets().add(target);
        // }
        if (project != null) {
            project.getTargets().add(target);
        }
        if (partner != null) {
            partner.getTargets().add(target);
        }

        return new CreateResult(target.getId());
    }
}