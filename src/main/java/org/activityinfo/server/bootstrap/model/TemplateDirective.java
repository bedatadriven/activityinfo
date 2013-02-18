package org.activityinfo.server.bootstrap.model;


import freemarker.template.Template;

public class TemplateDirective {
	final Template template;
	
	final PageModel pageModel;

	public TemplateDirective(Template template, PageModel pageModel) {
		this.template = template;
		this.pageModel = pageModel;
	}

	public Template getTemplate() {
		return template;
	}

	public PageModel getPageModel() {
		return pageModel;
	}
}
