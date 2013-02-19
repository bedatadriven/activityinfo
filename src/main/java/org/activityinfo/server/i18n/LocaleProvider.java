package org.activityinfo.server.i18n;

import java.util.Locale;

import javax.inject.Provider;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LocaleProvider implements Provider<Locale> {
    
	public static final String DEFAULT_LANGUAGE = "en";

	private final Provider<HttpServletRequest> req;
	
	@Inject
	public LocaleProvider(Provider<HttpServletRequest> req) {
        super();
        this.req = req;
    }

    @Override
	public Locale get() {
		String language = null;
		for(Cookie cookie : req.get().getCookies()) {
		    if(cookie.getName().equals(AuthenticatedUser.USER_LOCAL_COOKIE)) {
		        language = cookie.getValue();
		    }
		}
		
		if (language == null) {
			language = languageFromHeader();
		}
		
		if (language == null) {
			language = DEFAULT_LANGUAGE;
		}
		
		if(language.equals("en")) {
			return Locale.ENGLISH;
		} else {
			return Locale.FRANCE;
		}
	}

	private String languageFromHeader() {
		String acceptLanguages[] = Strings.nullToEmpty(
				req.get().getHeader("Accept-Language")).split(",");
		
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