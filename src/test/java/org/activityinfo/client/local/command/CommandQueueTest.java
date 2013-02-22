package org.activityinfo.client.local.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activityinfo.client.MockEventBus;
import org.activityinfo.client.local.command.CommandQueue;
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
