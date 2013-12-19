package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.report.content.AiLatLng;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class PointBundler implements Bundler {

	@Override
	public void bundle(SqlResultSetRow row, Bucket bucket) {
		if(!row.isNull("LX") && !row.isNull("LY")) {
			bucket.setPoint(new AiLatLng(row.getDouble("LY"), row.getDouble("LX")));
		} else if(!row.isNull("AX") && !row.isNull("AY")) {
			bucket.setPoint(new AiLatLng(row.getDouble("AY"), row.getDouble("AX")));
		}
	}
}
