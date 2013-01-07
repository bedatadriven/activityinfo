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
		Class<? extends Command> commandClass = event.getCommand().getClass();

		for (Class<?> trigger : triggers) {
			if (trigger.isAssignableFrom(commandClass)) {
				LOGGER.fine("handler "+this.getClass().getSimpleName() +" triggered by command "+event.getCommand());
				try {
					onEvent(event);
				} catch (Exception e) {
					LOGGER.warning("couldn't handle command "+commandClass.getSimpleName()+": "+e.getMessage());
					LOGGER.throwing(this.getClass().getSimpleName(), "handleEvent", e);
				}
			}
		}
	}
	
	protected abstract void onEvent(CommandEvent event);
}
