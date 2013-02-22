package org.activityinfo.server.command.handler.sync;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.zip.GZIPOutputStream;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.Provider;

public class LocationBuilderTest {

	@Test
	public void test() {
		
	}

//	
//	public static void main(String [] args) throws Exception {
//		GetSyncRegionUpdates request = new GetSyncRegionUpdates("location/4433", null);
//		
//		LocationUpdateBuilder builder = new LocationUpdateBuilder(new MyProvider());
//		SyncRegionUpdate update = builder.build(new User(), request);
//		
//		System.out.println("size: " + update.getSql().length());
//		System.out.println("compressed size: " + gzippedLength((update.getSql())));
//		
//		Files.write(update.getSql(), new File("target/location.sql"), Charsets.UTF_8);
//		
//	}
//	
//	private static int gzippedLength(String sql) throws IOException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		GZIPOutputStream gzos = new GZIPOutputStream(baos);
//		gzos.write(sql.getBytes(Charsets.UTF_8));
//		gzos.close();
//		return baos.size();
//	}
//	
//	private static class MyProvider implements Provider<Connection> {
//
//		@Override
//		public Connection get() {
//			try { 
//				Class.forName("com.mysql.jdbc.Driver");
//				return DriverManager.getConnection("jdbc:mysql://localhost/activityinfo26?user=alexander");
//			} catch(Exception e) {
//				throw new RuntimeException(e); 
//			}
//		}
//	}
}
