package org.activityinfo.client.local.sync.pipeline;

import org.activityinfo.client.local.sync.AppCacheSynchronizer;
import org.activityinfo.client.local.sync.DownSynchronizer;
import org.activityinfo.client.local.sync.UpdateSynchronizer;

import com.bedatadriven.rebar.async.AsyncPipeline;
import com.google.inject.Inject;

public class SyncPipeline extends AsyncPipeline {

	@Inject
	public SyncPipeline(
			AppCacheSynchronizer appCacheSynchronizer, 
			UpdateSynchronizer updateSynchronizer, 
			DownSynchronizer downSychronizer) {
		super(appCacheSynchronizer, updateSynchronizer, downSychronizer);
	}
}
