package org.sigmah.client.page.entry;

import org.junit.Test;
import org.sigmah.client.mock.DispatcherStub;
import org.sigmah.client.page.entry.grouping.AdminGroupingModel;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.DTOs;

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
