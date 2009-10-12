package org.activityinfo.clientjre.mock;

import org.activityinfo.client.util.CookieProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockCookieProvider implements CookieProvider {

    public Map<String, String> cookies = new HashMap<String, String>();

    @Override
    public String getCookie(String name) {
        return cookies.get(name);
    }

    @Override
    public void setCookie(String name, String value, Date expires) {

    }

    @Override
    public void removeCookie(String name) {

    }
}
