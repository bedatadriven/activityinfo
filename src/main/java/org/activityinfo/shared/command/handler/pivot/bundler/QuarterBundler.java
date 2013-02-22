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
import org.activityinfo.shared.report.content.QuarterCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class QuarterBundler implements Bundler {
    private final Dimension dimension;
    private final String yearAlias;
    private final String quarterAlias;

    public QuarterBundler(Dimension dimension, String yearAlias,
        String quarterAlias) {
        this.dimension = dimension;
        this.yearAlias = yearAlias;
        this.quarterAlias = quarterAlias;
    }

    @Override
    public void bundle(SqlResultSetRow rs, Bucket bucket) {
        // the year can be null in cases where a site does not yet have a
        // reporting period
        // and we query for site counts
        if (!rs.isNull(yearAlias)) {
            int year = rs.getInt(yearAlias);
            int quarter = rs.getInt(quarterAlias);

            bucket.setCategory(dimension, new QuarterCategory(year, quarter));
        }
    }
}