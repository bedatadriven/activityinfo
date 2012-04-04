package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.LinkIndicators;
import org.sigmah.shared.command.result.VoidResult;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LinkIndicatorsHandler implements CommandHandlerAsync<LinkIndicators, VoidResult> {

	@Override
	public void execute(LinkIndicators command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {

		SqlUpdate.delete("IndicatorLink")
			.where("sourceIndicatorId", command.getSourceIndicatorId())
			.where("destinationIndicatorId", command.getDestIndicatorId())
			.execute(context.getTransaction());
		
		if(command.isLink()) {
			SqlInsert.insertInto("IndicatorLink")
				.value("sourceIndicatorId", command.getSourceIndicatorId())
				.value("destinationIndicatorId", command.getDestIndicatorId())
				.execute(context.getTransaction());
		}
		
		callback.onSuccess(null);
		
	}

}
