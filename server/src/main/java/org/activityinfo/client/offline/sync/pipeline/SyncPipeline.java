package org.activityinfo.client.offline.sync.pipeline;

import org.activityinfo.client.offline.sync.AppCacheSynchronizer;
import org.activityinfo.client.offline.sync.DownSynchronizer;
import org.activityinfo.client.offline.sync.UpdateSynchronizer;

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
