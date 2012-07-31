package org.activityinfo.client.offline.command;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.UpdateSite;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.async.AsyncFunction;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.client.fn.AsyncSql;
import com.bedatadriven.rebar.sql.client.fn.TxAsyncFunction;
import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.common.base.Function;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * Manages a persistent queue of commands to be sent to the server.
 *
 */
@Singleton
public class CommandQueue {
	

	public static class QueueEntry {
		private int id;
		private Command command;
	
		
		public QueueEntry(int id, Command command) {
			super();
			this.id = id;
			this.command = command;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public Command getCommand() {
			return command;
		}
		public void setCommand(Command command) {
			this.command = command;
		}
	}
	
	private SqlQuery queryNext = 
			SqlQuery.select("id", "command").from("command_queue").orderBy("id");


	private SqlDatabase database;
	
	
	@Inject
	public CommandQueue(SqlDatabase database) {
		this.database = database;
	}
	
	public static TxAsyncFunction<Void, Void> createTableIfNotExists() {
		return AsyncSql.ddl("CREATE TABLE IF NOT EXISTS command_queue (id INTEGER PRIMARY KEY AUTOINCREMENT, command TEXT)");
	}

	/**
	 * Adds a command to the queue to be executed
	 * 
	 * @param cmd
	 * @throws SQLException
	 */
	public void queue(SqlTransaction tx, Command cmd) {
		JsonObject root = serialize(cmd);
		
		SqlInsert.insertInto("command_queue")
			.value("command", root.toString())
			.execute(tx);
	}
	
	public AsyncFunction<Void, List<QueueEntry>> get() {
		return database
				.transaction()
				.compose(queryNext)
				.map(
				   
			 new Function<SqlResultSetRow, QueueEntry>() {
				@Override
				public QueueEntry apply(SqlResultSetRow row) {
					int id = row.getInt("id");
					String json = row.getString("command"); 
					return new QueueEntry(id, deserializeCommand(json));
				}
			});
	}
	
	/**
	 * 
	 * Peeks at the command next line in for execution, without removing it from the queue.
	 * 
	 * @return the Command next in line for execution
	 */
	public void peek(final AsyncCallback<QueueEntry> callback) {
		get().compose(new Function<List<QueueEntry>, QueueEntry>() {

			@Override
			public QueueEntry apply(List<QueueEntry> input) {
				if(input.isEmpty()) {
					return null;
				} else {
					return input.get(0);
				}
			}
			
		}).apply(null, callback);
	}

	
	public AsyncFunction<QueueEntry, Void> remove() {
		return new AsyncFunction<CommandQueue.QueueEntry, Void>() {

			@Override
			protected void doApply(final QueueEntry argument,
					final AsyncCallback<Void> callback) {
				database.transaction(new SqlTransactionCallback() {
					
					@Override
					public void begin(SqlTransaction tx) {
						tx.executeSql("DELETE FROM command_queue WHERE id = ?", new Object[] {
								argument.getId() });
					}

					@Override
					public void onError(SqlException e) {
						callback.onFailure(e);
					}

					@Override
					public void onSuccess() {
						callback.onSuccess(null);
					}
				});				
			}
		};
	}
	
	public void remove(QueueEntry entry, AsyncCallback<Void> callback) {
		remove().apply(entry, callback);
	}
	

	
	private JsonObject serialize(Command cmd) {
		if(cmd instanceof CreateSite) {
			return serialize((CreateSite)cmd);
		} else if(cmd instanceof UpdateSite) {
			return serialize((UpdateSite)cmd);
		} else if(cmd instanceof CreateLocation) {
			return serialize((CreateLocation)cmd);
		} else {
			throw new IllegalArgumentException("Cannot serialize commands of type " + cmd.getClass());
		}
	}

	private JsonObject serialize(CreateSite cmd) {
		JsonObject root = new JsonObject();
		root.addProperty("commandClass", "CreateSite");
		root.add("properties", encodeMap(cmd.getProperties().getTransientMap()));
		return root;
	}
	
	private JsonObject serialize(UpdateSite cmd) {
		JsonObject root = new JsonObject();
		root.addProperty("commandClass", "UpdateSite");
		root.addProperty("siteId", cmd.getSiteId());
		root.add("changes", encodeMap(cmd.getChanges().getTransientMap()));
		return root;
	}
	
	private JsonObject serialize(CreateLocation cmd) {
		JsonObject root = new JsonObject();
		root.addProperty("commandClass", "CreateLocation");
		root.add("properties", encodeMap(cmd.getProperties().getTransientMap()));
		return root;
	}
	
	private Command deserializeCommand(String json) {
		JsonObject root = (JsonObject) new JsonParser().parse(json);
		String commandClass = root.get("commandClass").getAsString();
		
		if("CreateSite".equals(commandClass)) {
			return deserializeCreateSite(root);
		} else if("UpdateSite".equals(commandClass)) {
			return deserializeUpdateSite(root);
		} else if("CreateLocation".equals(commandClass)) {
			return deserializeCreateLocation(root);
		} else {
			throw new RuntimeException("Cannot deserialize queud command of class " + commandClass);
		}
	}


	private CreateSite deserializeCreateSite(JsonObject root) {
		return new CreateSite(
			decodeMap(root.get("properties").getAsJsonObject()));
	}

	private UpdateSite deserializeUpdateSite(JsonObject root) {
		return new UpdateSite(root.get("siteId").getAsInt(), 
				decodeMap(root.get("changes").getAsJsonObject()));
	}
	
	private CreateLocation deserializeCreateLocation(JsonObject root) {
		return new CreateLocation(decodeMap(root.get("properties").getAsJsonObject()));
	}

	
	private JsonObject encodeMap(Map<String, Object> map) {
		JsonObject root = new JsonObject();
		for(Entry<String,Object> property : map.entrySet()) {
			if(property.getValue() != null) {
				JsonObject value = new JsonObject();
				
				if(property.getValue() instanceof String) {
					value.addProperty("type", "String");
					value.addProperty("value", (String)property.getValue());
				} else if(property.getValue() instanceof Integer ) {
					value.addProperty("type", "Integer");
					value.addProperty("value", (Integer)property.getValue());
				} else if(property.getValue() instanceof Double) {
					value.addProperty("type", "Double");
					value.addProperty("value", (Double)property.getValue());
				} else if(property.getValue() instanceof Date) {
					Date date = (Date)property.getValue();
	
					value.addProperty("type", "Date");
					value.addProperty("time", date.getTime());
				} else if(property.getValue() instanceof Boolean) {
					value.addProperty("type", "Boolean");
					value.addProperty("value", (Boolean)property.getValue());
				} else if(property.getValue() instanceof LocalDate) {
					value.addProperty("type", "LocalDate");
					value.addProperty("value", property.getValue().toString());
					
				} else {
          Log.error("Cannot convert handle map value '" + property.getKey() + ", type " + property.getKey() +
							": " + property.getValue().getClass().getName());
          value = null;
				}

        if(value != null ) {
				  root.add(property.getKey(), value);
        }
			}
		}
		return root;
	}
	
	private Map<String,Object> decodeMap(JsonObject root) {
		Map<String,Object> map = new HashMap<String, Object>();
		for(Entry<String,JsonElement> property : root.entrySet()) {
			JsonObject value = (JsonObject)property.getValue();
			String type = value.get("type").getAsString();
			
			if("String".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsString());
			} else if("Integer".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsInt());
			} else if("Double".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsDouble());
			} else if("Date".equals(type)) {
				map.put(property.getKey(), new Date(value.get("time").getAsLong()));
			} else if("Boolean".equals(type)) {
				map.put(property.getKey(), value.get("value").getAsBoolean());
			} else if("LocalDate".equals(type)) {
				map.put(property.getKey(), LocalDate.parse( value.get("value").getAsString()));
			} else {
				throw new IllegalArgumentException("map contains key with unsupported value type -- " + 
					property.getKey() + ": " + type);
			}
			
		}
		return map;
	}
}
