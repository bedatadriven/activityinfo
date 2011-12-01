package org.sigmah.server.command.handler;

import java.util.Map;
import javax.persistence.EntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.command.handler.crud.PropertyMap;
import org.sigmah.server.database.hibernate.entity.Indicator;
import org.sigmah.server.database.hibernate.entity.Target;
import org.sigmah.server.database.hibernate.entity.TargetValue;
import org.sigmah.server.database.hibernate.entity.TargetValueId;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.UpdateTargetValue;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class UpdateTargetValueHandler extends BaseEntityHandler implements
		CommandHandler<UpdateTargetValue> {

	private final static Log LOG = LogFactory.getLog(UpdateTargetValueHandler.class);

	private final Injector injector;

	@Inject
	public UpdateTargetValueHandler(EntityManager em, Injector injector) {
		super(em);
		this.injector = injector;
	}

	@Override
	public CommandResult execute(UpdateTargetValue cmd, User user)
			throws CommandException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("[execute] Update command for entity: TargetValue");
		}

		Map<String, Object> changes = cmd.getChanges().getTransientMap();
		PropertyMap changeMap = new PropertyMap(changes);

		try{
			TargetValue targetValue = entityManager().find(TargetValue.class, new TargetValueId(cmd.getTargetId(), cmd.getIndicatorId()));
			if(cmd.getChanges().get("value") !=null){
				targetValue.setValue((Double.valueOf((String)cmd.getChanges().get("value"))));
				entityManager().persist(targetValue);
			
				return null;
			}
			
			entityManager().remove(targetValue);			
			return null;
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
		

		return null;
	}
}
