package org.activityinfo.client.offline.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.offline.command.CommandQueue;
import org.activityinfo.database.ClientDatabaseStubs;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.UpdateSite;
import org.activityinfo.shared.util.Collector;
import org.junit.Before;
import org.junit.Test;

import com.bedatadriven.rebar.async.NullCallback;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.bedatadriven.rebar.time.calendar.LocalDate;

public class CommandQueueTest {
	
	private SqlDatabase db;
	private CommandQueue queue;
	
	@Before
	public void cleanUpPreemptively() {
		JdbcScheduler.get().forceCleanup();

		db = ClientDatabaseStubs.empty();
		queue = new CommandQueue(new MockEventBus(), db);
		
		db.execute(CommandQueue.createTableIfNotExists(), new NullCallback<Void>());
	}
	
	@Test
	public void testCreateSite() {
		
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("anInt", 34);
		properties.put("aString", "testing");
		properties.put("aDouble", 3.0);
		properties.put("aBoolean", true);
		properties.put("anotherBoolean", false);
		properties.put("aTime", new Date());
		properties.put("aDate", new LocalDate(2011,3,15));
		
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
		
		Collector<Void> deleted = Collector.newCollector();
		queue.remove(reread.getResult(), deleted);
				
		Collector<CommandQueue.QueueEntry> entry2 = Collector.newCollector();
		queue.peek(entry2);
		
		assertThat(entry2.getResult(), is(nullValue()));

		
	}
	
	@Test
	public void testUpdateSite() {
				
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
