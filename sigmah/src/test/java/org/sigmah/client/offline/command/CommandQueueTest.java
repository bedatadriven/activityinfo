package org.sigmah.client.offline.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sigmah.database.ClientDatabaseStubs;
import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.UpdateSite;
import org.sigmah.shared.util.Collector;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;

public class CommandQueueTest {
	
	@Before
	public void cleanUpPreemptively() {
		JdbcScheduler.get().forceCleanup();
	}
	
	@Test
	public void testCreateSite() {
		
		SqlDatabase db = ClientDatabaseStubs.empty();
		final CommandQueue queue = new CommandQueue(db);
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("anInt", 34);
		properties.put("aString", "testing");
		properties.put("aDouble", 3.0);
		properties.put("aBoolean", true);
		properties.put("anotherBoolean", false);
		properties.put("aDate", new Date());
		
		final CreateSite cmd = new CreateSite(properties);
		
		db.transaction(new SqlTransactionCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				queue.queue(tx, cmd);
			}
		});
		
		Collector<CommandQueue.QueueEntry> reread = Collector.newCollector();
		queue.peek(reread);
		
		assertThat(reread.getResult(), not(nullValue()));
		assertThat(cmd, equalTo(reread.getResult().getCommand()));
		
		Collector<Boolean> deleted = Collector.newCollector();
		queue.remove(reread.getResult().getId(), deleted);
		
		assertThat(deleted.getResult(), equalTo(true));
		
		Collector<CommandQueue.QueueEntry> entry2 = Collector.newCollector();
		queue.peek(entry2);
		
		assertThat(entry2.getResult(), is(nullValue()));

		
	}
	
	@Test
	public void testUpdateSite() {
		
		SqlDatabase db = ClientDatabaseStubs.empty();
		final CommandQueue queue = new CommandQueue(db);
		
		Map<String, Object> changes = new HashMap<String, Object>();
		changes.put("anInt", 34);
		changes.put("aString", "testing");
		
		final UpdateSite cmd = new UpdateSite(99, changes);
		
		db.transaction(new SqlTransactionCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				queue.queue(tx, cmd);
			}
		});
		
		Collector<CommandQueue.QueueEntry> reread = Collector.newCollector();
		queue.peek(reread);
		

		assertThat(reread.getResult(), not(nullValue()));
		assertThat(cmd, equalTo(reread.getResult().getCommand()));
	}
}
