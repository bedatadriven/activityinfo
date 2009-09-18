package org.activityinfo.client.util;

import java.util.Date;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface CookieProvider {

    public String getCookie(String name);

    public void setCookie(String name, String value, Date expires);

    public void removeCookie(String name);

}
