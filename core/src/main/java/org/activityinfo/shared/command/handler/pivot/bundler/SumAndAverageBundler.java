package org.activityinfo.shared.command.handler.pivot.bundler;

import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.dto.IndicatorDTO;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class SumAndAverageBundler implements Bundler {
	
	private String aggregationType;
	private String sum;
	private String avg;
	
    public SumAndAverageBundler(String aggregationType, String sum, String avg) {
		this.aggregationType = aggregationType;
		this.sum = sum;
		this.avg = avg;
	}

    @Override
	public void bundle(SqlResultSetRow rs, Bucket bucket)  {
        int aggMethod = rs.getInt(aggregationType);

        double value;
        if (aggMethod == IndicatorDTO.AGGREGATE_SUM) {
            value = rs.getDouble(sum);
        } else if (aggMethod == IndicatorDTO.AGGREGATE_AVG) {
            value = rs.getDouble(avg);
        } else {
            assert false : "Database has a weird value for aggregation method = " + aggMethod;
            value = rs.getDouble(sum);
        }

        bucket.setDoubleValue(value);
    }
}