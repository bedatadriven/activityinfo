package org.activityinfo.client.page.entry;

import org.activityinfo.client.dispatch.DispatcherStub;
import org.activityinfo.client.page.entry.SiteAdminTreeLoader;
import org.activityinfo.client.page.entry.grouping.AdminGroupingModel;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.DTOs;
import org.junit.Test;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;

public class SiteAdminTreeLoaderTest {

	@Test
	public void load() {
		
		DispatcherStub dispatcher = new DispatcherStub();
		dispatcher.setResult(new GetSchema(), DTOs.PEAR.SCHEMA);
		dispatcher.setResult(new GetAdminEntities(1), DTOs.PROVINCES);
		
		SiteAdminTreeLoader loader = new SiteAdminTreeLoader(dispatcher, new AdminGroupingModel(1));
			
		TreeStore<ModelData> store = new TreeStore<ModelData>(loader);
		
		loader.load();
	}
	
}
