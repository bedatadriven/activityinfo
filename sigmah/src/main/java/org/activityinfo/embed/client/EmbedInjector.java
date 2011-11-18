package org.activityinfo.embed.client;


import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({EmbedModule.class})
public interface EmbedInjector extends Ginjector {

	SitesList getSitesList();
	
}
