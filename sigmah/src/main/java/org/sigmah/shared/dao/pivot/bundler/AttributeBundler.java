package org.sigmah.shared.dao.pivot.bundler;

import org.sigmah.shared.dao.pivot.Bucket;
import org.sigmah.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class AttributeBundler implements Bundler {
    private final Dimension dimension;
    private final int labelColumnIndex;
    private final int attributeCount;

    public AttributeBundler(Dimension dimension, int labelColumnIndex, int attributeCount) {
        this.dimension = dimension;
    	this.labelColumnIndex = labelColumnIndex;
        this.attributeCount = attributeCount;
    }

    @Override
    public void bundle(SqlResultSetRow rs, Bucket bucket)  {
//
//    	StringBuilder buff = new StringBuilder();
//    	for (int i = labelColumnIndex; i < labelColumnIndex + attributeCount; i++) {
//
//    		if (rs.getString(i) != null && !"null".equals(rs.getString(i))) {
//    			if (buff.length() > 0) {
//    				buff.append(", ");
//    			}
//    			buff.append(rs.getString(i));
//    		}
//    	}
//    	bucket.setCategory(dimension, new SimpleCategory(buff.toString()));
    }
}