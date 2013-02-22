package org.activityinfo.shared.command.handler.pivot.bundler;

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

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.WeekCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class MySqlYearWeekBundler implements Bundler {

	private Dimension dimension;
	private String weekAlias;

	public MySqlYearWeekBundler(Dimension dimension, String weekAlias) {
		this.dimension = dimension;
		this.weekAlias = weekAlias;
	}

	@Override
	public void bundle(SqlResultSetRow row, Bucket bucket) {
		if(!row.isNull(weekAlias)) {
			String yearWeek = row.getString(weekAlias);
			if(yearWeek.length() == 6) {
				bucket.setCategory(dimension, new WeekCategory(
						Integer.parseInt(yearWeek.substring(0, 4)),
						Integer.parseInt(yearWeek.substring(4, 6))));
			}
		}
	}

}
