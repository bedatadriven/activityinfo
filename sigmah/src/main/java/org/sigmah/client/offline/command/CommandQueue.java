package org.sigmah.client.offline.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.dao.SqlInsertBuilder;
import org.sigmah.shared.dao.SqlQueryBuilder;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * Manages a persistent queue of commands to be sent to the server.
 * 
 * @author alex
 *
 */
@Singleton
public class CommandQueue {
	
	private final Connection connection;
	
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
	
	
	@Inject
	public CommandQueue(Connection connection) {
		this.connection = connection;		
		/*
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS command_queue (id INTEGER PRIMARY KEY AUTOINCREMENT, command TEXT) " );
		} catch (SQLException e) {
			Log.error("Could not create the command_queue table!", e);
		}*/
	}

	/**
	 * Adds a command to the queue to be executed
	 * 
	 * @param cmd
	 * @throws SQLException
	 */
	public void queue(Command cmd) throws SQLException {
		/*
		if(cmd instanceof CreateEntity) {
			queueCreateEntity((CreateEntity) cmd); 
		} else {
			throw new RuntimeException("Cannot queue class of type " + cmd.getClass().getName());
		}*/
	}
	
	/**
	 * 
	 * Peeks at the command next line in for execution, without removing it from the queue.
	 * 
	 * @return the Command next in line for execution
	 */
	public void peek(AsyncCallback<QueueEntry> callback) {
		callback.onSuccess(null);
		/*try {
			ResultSet rs = SqlQueryBuilder.select("id, command").from("command_queue").orderBy("id").executeQuery(connection);
			if(rs.next()) {
				callback.onSuccess(new QueueEntry(rs.getInt(1), deserializeCommand(rs.getString(2))));
			} else {
				callback.onSuccess(null);
			}
		} catch(SQLException e){
			callback.onFailure(e);
		}*/
	}
	
	public void remove(int queueId, AsyncCallback<Boolean> callback) {
		try {
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM command_queue WHERE id = ?");
			stmt.setInt(1, queueId);
			int rowsAffected = stmt.executeUpdate();
			connection.commit();
			
			callback.onSuccess(rowsAffected == 1);
			
			
		} catch(SQLException e) {
			callback.onFailure(e);
		}
	}
	

	private void queueCreateEntity(CreateEntity cmd) throws SQLException {
		
		JsonObject root = serialize(cmd);
		
		SqlInsertBuilder.insertInto("command_queue")
			.value("command", root.toString())
			.execute(connection);
	}

	private JsonObject serialize(CreateEntity cmd) {
		JsonObject root = new JsonObject();
		root.addProperty("commandClass", "CreateEntity");
		root.addProperty("entityName", cmd.getEntityName());
		root.add("properties", encodeMap(cmd.getProperties().getTransientMap()));
		return root;
	}
	
	
	private Command deserializeCommand(String json) {
		JsonObject root = (JsonObject) new JsonParser().parse(json);
		String commandClass = root.get("commandClass").getAsString();
		
		if("CreateEntity".equals(commandClass)) {
			return deserializeCreateEntity(root);
		} else {
			throw new RuntimeException("Cannot deserialize queud command of class " + commandClass);
		}
	}

	private CreateEntity deserializeCreateEntity(JsonObject root) {
		return new CreateEntity(
			root.get("entityName").getAsString(),
			decodeMap(root.get("properties").getAsJsonObject()));
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
				} else {
					throw new IllegalArgumentException("Cannot convert handle map value type " + property.getKey() +
							": " + property.getValue().getClass().getName());
				}
				
				root.add(property.getKey(), value);
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
			} else {
				throw new IllegalArgumentException("map contains key with unsupported value type -- " + 
					property.getKey() + ": " + type);
			}
			
		}
		return map;
	}
	
}
