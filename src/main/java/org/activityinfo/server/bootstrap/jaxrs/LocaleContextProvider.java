package org.activityinfo.server.bootstrap.jaxrs;

import java.util.Locale;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;

@Provider
@Singleton
public class LocaleContextProvider extends
		AbstractInjectableProvider<Locale> {
	public static final String DEFAULT_LANGUAGE = "en";

	@Override
	public Locale getValue(HttpContext c) {
		String language = null;
		Cookie curLocaleCookie = c.getRequest().getCookies().get("locale");
		
		if (null == curLocaleCookie) {
			language = languageFromHeader(c.getRequest());
		} else {
			language = curLocaleCookie.getValue();
		}
		
		if (null == language) {
			language = DEFAULT_LANGUAGE;
		}
		
		if(language.equals("en")) {
			return Locale.ENGLISH;
		} else {
			return Locale.FRANCE;
		}
	}

	private String languageFromHeader(HttpRequestContext request) {
		String acceptLanguages[] = Strings.nullToEmpty(
				request.getHeaderValue("Accept-Language")).split(",");
		
		for (String lang : acceptLanguages) {
			if (lang.startsWith("en")) {
				return "en";
			} else if (lang.startsWith("fr")) {
				return "fr";
			}
		}
		
		return null;
	}
}