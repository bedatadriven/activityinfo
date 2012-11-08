package org.activityinfo.server.event;

import java.util.logging.Logger;

import org.activityinfo.shared.command.Command;

import com.google.common.eventbus.Subscribe;

@SuppressWarnings("rawtypes")
public abstract class CommandEventListener {
	protected Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private Class<? extends Command>[] triggers;
	
	public CommandEventListener(ServerEventBus serverEventBus, Class<? extends Command>... triggers) {
		serverEventBus.register(this);
		this.triggers = triggers;
	}

	@Subscribe
	public void handleEvent(CommandEvent event) {
		LOGGER.fine("listening on event "+event);
		
		Class<? extends Command> commandClass = event.getCommand().getClass();

		for (Class<?> trigger : triggers) {
			if (trigger.isAssignableFrom(commandClass)) {
				LOGGER.fine(trigger.getSimpleName()+" command triggered execution of handler "+this.getClass().getSimpleName());
				onEvent(event);
			}
		}
	}
	
	public abstract void onEvent(CommandEvent event);
}
