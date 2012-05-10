package org.activityinfo.shared.command.handler;

import java.util.Map;

import org.activityinfo.shared.command.GetDimensionLabels;
import org.activityinfo.shared.command.GetDimensionLabels.DimensionLabels;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetDimensionLabelsHandler implements CommandHandlerAsync<GetDimensionLabels, DimensionLabels> {
	
	@Override
	public void execute(GetDimensionLabels command, ExecutionContext context,
			final AsyncCallback<DimensionLabels> callback) {
		
		SqlQuery query = composeQuery(command);
		query.execute(context.getTransaction(), new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				Map<Integer, String> labels = Maps.newHashMap();
				for (SqlResultSetRow row : results.getRows()) {
					labels.put(row.getInt("id"), row.getString("name"));
				}
				callback.onSuccess(new DimensionLabels(labels));
			}
		});
	}

	private SqlQuery composeQuery(GetDimensionLabels command) {
		String tableName;
		String primaryKey;
		
		switch(command.getType()) {
		case Database:
			tableName = "UserDatabase";
			primaryKey = "DatabaseId";
			break;
		case AdminLevel:
			tableName = "AdminEntity";
			primaryKey = "AdminEntityId";
			break;
		default:
			tableName = command.getType().toString();
			primaryKey = tableName + "Id";
		}
		
		return SqlQuery.select()
				.appendColumn("name")
				.appendColumn(primaryKey, "id")
				.from(tableName)
				.where(primaryKey)
				.in(command.getIds());
	}
}

