package org.sigmah.client.dispatch.remote;

import com.allen_sauer.gwt.log.client.Log;
import org.sigmah.client.dispatch.CommandProxy;
import org.sigmah.client.dispatch.DispatchEventSource;
import org.sigmah.client.dispatch.DispatchListener;
import org.sigmah.client.dispatch.remote.cache.CommandProxyResult;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class that maintains a list of {@link DispatchListener}s and
 * {@link CommandProxy}
 */
public class ProxyManager implements DispatchEventSource {

    private Map<Class<? extends Command>, List<DispatchListener>> listeners =
            new HashMap<Class<? extends Command>, List<DispatchListener>>();

    private Map<Class<? extends Command>, List<CommandProxy>> proxies =
            new HashMap<Class<? extends Command>, List<CommandProxy>>();

    public ProxyManager() {

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
    public <T extends Command> void registerProxy(Class<T> commandClass, CommandProxy<T> proxy) {
        List<CommandProxy> classProxies = proxies.get(commandClass);
        if (classProxies == null) {
            classProxies = new ArrayList<CommandProxy>();
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
    public CommandProxyResult execute(Command cmd) {

        List<CommandProxy> classProxies = proxies.get(cmd.getClass());

        if (classProxies != null) {

            for (CommandProxy proxy : classProxies) {


                try {
                    CommandProxyResult r = proxy.execute(cmd);
                    if (r.couldExecute) {

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

        return CommandProxyResult.couldNotExecute();
    }
}