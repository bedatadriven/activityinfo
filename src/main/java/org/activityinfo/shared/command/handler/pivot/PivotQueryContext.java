package org.activityinfo.shared.command.handler.pivot;

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

import java.util.List;
import java.util.Map;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.Bucket;

import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PivotQueryContext {

    private PivotSites command;
    private ExecutionContext executionContext;
    private SqlDialect dialect;
    private Map<Object, Bucket> buckets = Maps.newHashMap();

    public PivotQueryContext(PivotSites command, ExecutionContext context,
        SqlDialect dialect) {
        this.command = command;
        this.executionContext = context;
        this.dialect = dialect;
    }

    public PivotSites getCommand() {
        return command;
    }

    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    public SqlDialect getDialect() {
        return dialect;
    }

    public void addBucket(Bucket bucket) {
        Bucket existing = buckets.get(bucket.getKey());
        if (existing == null) {
            buckets.put(bucket.getKey(), bucket);
        } else {
            existing.add(bucket);
        }
    }

    public List<Bucket> getBuckets() {
        return Lists.newArrayList(buckets.values());
    }

}
