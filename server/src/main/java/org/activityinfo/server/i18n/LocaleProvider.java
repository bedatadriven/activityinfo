package org.activityinfo.server.i18n;

import java.util.Locale;

import com.google.inject.Provider;
import com.teklabs.gwt.i18n.server.LocaleProxy;

public class LocaleProvider implements Provider<Locale> {


	@Override
	public Locale get() {
		return LocaleProxy.getLocale();
	}

}
