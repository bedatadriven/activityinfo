package org.activityinfo.client.offline.sync.pipeline;

import org.activityinfo.client.offline.sync.AppCacheSynchronizer;
import org.activityinfo.client.offline.sync.DownSynchronizer;
import org.activityinfo.client.offline.sync.UpdateSynchronizer;

public class SyncPipeline extends AsyncPipeline {

	public SyncPipeline(
			AppCacheSynchronizer appCacheSynchronizer, 
			UpdateSynchronizer updateSynchronizer, 
			DownSynchronizer downSychronizer) {
		super(appCacheSynchronizer, updateSynchronizer, downSychronizer);
	}
}
