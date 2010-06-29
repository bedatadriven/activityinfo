package org.sigmah.client.mock;

import org.sigmah.client.util.state.IStateManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockStateManager implements IStateManager {


    Map<String, Object> state = new HashMap<String, Object>();

    @Override
    public Object get(String name) {
        return state.get(name);
    }

    @Override
    public Date getDate(String name) {
        return (Date)get(name);
    }

    @Override
    public Integer getInteger(String name) {
        return (Integer)get(name);
    }

    @Override
    public Map<String, Object> getMap(String name) {
        return (Map<String,Object>)get(name);
    }

    @Override
    public String getString(String name) {
        return (String)get(name);
    }

    @Override
    public void set(String name, Object value) {
        state.put(name, value);
    }
}
