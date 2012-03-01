package org.sigmah.server.command.handler;

import javax.persistence.EntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.database.hibernate.entity.Indicator;
import org.sigmah.server.database.hibernate.entity.IndicatorLink;
import org.sigmah.server.database.hibernate.entity.IndicatorLinkId;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.UpdateIndicatorLink;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class UpdateIndicatorLinkHandler implements
		CommandHandler<UpdateIndicatorLink> {

	private static final Log LOG = LogFactory
			.getLog(UpdateIndicatorLinkHandler.class);

	private final EntityManager entityManager;

	@Inject
	public UpdateIndicatorLinkHandler(EntityManager em) {
		this.entityManager = em;
	}

	@Override
	public CommandResult execute(UpdateIndicatorLink cmd, User user)
			throws CommandException {

		int srcId = cmd.getSourceIndicator();
		int destinationId = cmd.getDestinationIndicators();
		
		if (cmd.isDelete()) {
			IndicatorLink link = entityManager.find(IndicatorLink.class, new IndicatorLinkId(srcId, destinationId));
			entityManager.remove(link);

			return new VoidResult();
		}
		
		Indicator sourceIndicator = entityManager.find(Indicator.class, srcId);
		Indicator destinationIndicator = entityManager.find(Indicator.class,destinationId);

		IndicatorLink link = new IndicatorLink();
		link.setId(new IndicatorLinkId(srcId, destinationId));
		link.setSourceIndicator(sourceIndicator);
		link.setDestinationIndicator(destinationIndicator);
		entityManager.persist(link);

		return new VoidResult();

	}

}
