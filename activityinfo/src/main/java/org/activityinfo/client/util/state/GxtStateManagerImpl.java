package org.activityinfo.client.util.state;

import com.extjs.gxt.ui.client.state.StateManager;

import java.util.Date;
import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GxtStateManagerImpl implements IStateManager {

    private final StateManager gxtMgr;

    public GxtStateManagerImpl() {
        gxtMgr = StateManager.get();
    }

    @Override
    public Object get(String name) {
        return gxtMgr.get(name);
    }

    @Override
    public Date getDate(String name) {
        return gxtMgr.getDate(name);
    }

    @Override
    public Integer getInteger(String name) {
        return gxtMgr.getInteger(name);
    }

    @Override
    public Map<String, Object> getMap(String name) {
        return gxtMgr.getMap(name);
    }

    @Override
    public String getString(String name) {
        return gxtMgr.getString(name);
    }

    @Override
    public void set(String name, Object value) {
        gxtMgr.set(name, value);
    }
}
