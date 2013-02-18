package org.activityinfo.server.command.handler;

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
