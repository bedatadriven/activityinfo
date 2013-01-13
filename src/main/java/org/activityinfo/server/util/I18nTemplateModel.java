package org.activityinfo.server.util;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.activityinfo.client.i18n.UIConstants;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class I18nTemplateModel implements freemarker.template.TemplateHashModel {

	private final Provider<UIConstants> constants; 
	
	@Inject
	public I18nTemplateModel(Provider<UIConstants> constants) {
		super();
		this.constants = constants;
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		UIConstants instance = constants.get();
		try {
			return new freemarker.template.SimpleScalar((String)instance.getClass().getMethod(key).invoke(instance));
		} catch (Exception e) {
			throw new TemplateModelException(e);
		}
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return false;
	}
	
}