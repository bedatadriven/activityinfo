package org.activityinfo.shared.command.handler.pivot;

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
	
	public PivotQueryContext(PivotSites command, ExecutionContext context, SqlDialect dialect) {
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
		if(existing == null) {
			buckets.put(bucket.getKey(), bucket);
		} else {
			existing.add(bucket);
		}
	}
	public List<Bucket> getBuckets() {
		return Lists.newArrayList(buckets.values());
	}
	
	
	
}
