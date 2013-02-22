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

import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.crud.PropertyMap;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.Target;
import org.activityinfo.server.database.hibernate.entity.TargetValue;
import org.activityinfo.server.database.hibernate.entity.TargetValueId;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.UpdateTargetValue;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class UpdateTargetValueHandler extends BaseEntityHandler implements
		CommandHandler<UpdateTargetValue> {

	private final static Logger LOG = Logger.getLogger(UpdateTargetValueHandler.class.getName());

	private final Injector injector;

	@Inject
	public UpdateTargetValueHandler(EntityManager em, Injector injector) {
		super(em);
		this.injector = injector;
	}

	@Override
	public CommandResult execute(UpdateTargetValue cmd, User user)
			throws CommandException {

		LOG.fine("[execute] Update command for entity: TargetValue");

		Map<String, Object> changes = cmd.getChanges().getTransientMap();
		PropertyMap changeMap = new PropertyMap(changes);

		try{
			TargetValue targetValue = entityManager().find(TargetValue.class, new TargetValueId(cmd.getTargetId(), cmd.getIndicatorId()));
			if(cmd.getChanges().get("value") !=null){
				targetValue.setValue((Double.valueOf((String)cmd.getChanges().get("value"))));
				entityManager().persist(targetValue);
			
				return new VoidResult();
			}
			
			entityManager().remove(targetValue);			
			return new VoidResult();
		}catch(Exception e){
			// ignore 
		}
		
		Target target  = entityManager().find(Target.class, cmd.getTargetId());
		Indicator indicator = entityManager().find(Indicator.class, cmd.getIndicatorId());
		
		TargetValue targetValue =  new TargetValue();
		targetValue.setId(new TargetValueId(cmd.getTargetId(),cmd.getIndicatorId()));
		targetValue.setValue((Double.valueOf((String)cmd.getChanges().get("value"))));
		targetValue.setTarget(target);
		targetValue.setIndicator(indicator);
		
		entityManager().persist(targetValue);
		

		return new VoidResult();
	}
}
