package org.sigmah.client.data.proxy;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.ListResult;
import org.sigmah.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdminEntityProxy extends RpcProxy<ListResult<AdminEntityDTO>> {

	private final Dispatcher dispatcher;
	private final int levelId;
	private Integer parentId;
	
	public AdminEntityProxy(Dispatcher dispatcher, int levelId) {
		this.dispatcher = dispatcher;
		this.levelId = levelId;
	}
	
	public void setParentAdminEntityId(Integer id) {
		this.parentId = id;
	}
	
	@Override
	protected void load(Object loadConfig,
			final AsyncCallback<ListResult<AdminEntityDTO>> callback) {

		GetAdminEntities query = new GetAdminEntities(levelId);
		query.setParentId(parentId);
		
		dispatcher.execute(query, null, new AsyncCallback<AdminEntityResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(AdminEntityResult result) {
				callback.onSuccess(result);
			}
		});
	}
}
