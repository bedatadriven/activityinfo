package org.activityinfo.client.local.sync.pipeline;

import org.activityinfo.client.local.sync.AppCacheSynchronizer;
import org.activityinfo.client.local.sync.DownSynchronizer;

import com.bedatadriven.rebar.async.AsyncPipeline;
import com.google.inject.Inject;

public class InstallPipeline extends AsyncPipeline {

	@Inject
	public InstallPipeline(
			AppCacheSynchronizer appCacheSynchronizer, 
			DropAll dropAll, 
			DownSynchronizer downSychronizer) {
		super(appCacheSynchronizer, dropAll, downSychronizer);
	}
}
