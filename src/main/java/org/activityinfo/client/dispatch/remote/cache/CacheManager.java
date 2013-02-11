/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.CommandCache;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.DispatchListener;
import org.activityinfo.client.local.sync.SyncCompleteEvent;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Utility class that maintains a list of {@link DispatchListener}s and
 * {@link org.activityinfo.client.dispatch.CommandCache}
 */
@Singleton
public class CacheManager implements DispatchEventSource {

    private Map<Class<? extends Command>, List<DispatchListener>> listeners =
            new HashMap<Class<? extends Command>, List<DispatchListener>>();

    private Map<Class<? extends Command>, List<CommandCache>> proxies =
            new HashMap<Class<? extends Command>, List<CommandCache>>();

    @Inject
    public CacheManager(EventBus eventBus) {
    	eventBus.addListener(SyncCompleteEvent.TYPE, new Listener<SyncCompleteEvent>() {

			@Override
			public void handleEvent(SyncCompleteEvent be) {
				clearAllCaches();
			}
		});
    }

    private void clearAllCaches() {
		for(List<CommandCache> list : proxies.values()) {
			for(CommandCache<?> cache : list) {
				cache.clear();
			}
		}
	}

	@Override
    public <T extends Command> void registerListener(Class<T> commandClass, DispatchListener<T> listener) {
        List<DispatchListener> classListeners = listeners.get(commandClass);
        if (classListeners == null) {
            classListeners = new ArrayList<DispatchListener>();
            listeners.put(commandClass, classListeners);
        }
        classListeners.add(listener);
    }

    @Override
    public <T extends Command> void registerProxy(Class<T> commandClass, CommandCache<T> proxy) {
        List<CommandCache> classProxies = proxies.get(commandClass);
        if (classProxies == null) {
            classProxies = new ArrayList<CommandCache>();
            proxies.put(commandClass, classProxies);
        }
        classProxies.add(proxy);
    }

    public void notifyListenersOfSuccess(Command cmd, CommandResult result) {

        List<DispatchListener> classListeners = listeners.get(cmd.getClass());
        if (classListeners != null) {
            for (DispatchListener listener : classListeners) {
                try {
                    listener.onSuccess(cmd, result);
                } catch (Exception e) {
                    Log.error("ProxyManager: listener threw exception during onSuccess notification.", e);
                }
            }
        }
    }

    public void notifyListenersBefore(Command cmd) {

        List<DispatchListener> classListeners = listeners.get(cmd.getClass());
        if (classListeners != null) {
            for (DispatchListener listener : classListeners) {
                try {
                    listener.beforeDispatched(cmd);
                } catch (Exception e) {
                    Log.error("ProxyManager: listener threw exception during beforeCalled notification", e);
                }
            }
        }
    }

    /**
     * Attempts to execute the command locally using one of the registered
     * proxies
     *
     * @param cmd
     * @return
     */
    public CacheResult execute(Command cmd) {

        List<CommandCache> classProxies = proxies.get(cmd.getClass());

        if (classProxies != null) {

            for (CommandCache proxy : classProxies) {


                try {
                    CacheResult r = proxy.maybeExecute(cmd);
                    if (r.isCouldExecute()) {

                        Log.debug("ProxyManager: EXECUTED (!!) " + cmd.toString() + " locally with proxy "
                                + proxy.getClass().getName());


                        return r;
                    } else {
                        Log.debug("ProxyManager: Failed to execute " + cmd.toString() + " locally with proxy "
                                + proxy.getClass().getName());


                        return r;
                    }
                } catch (Exception e) {
                    Log.error("ProxyManager: proxy threw exception during call to execute", e);
                }
            }
        }
        return CacheResult.couldNotExecute();
    }
}