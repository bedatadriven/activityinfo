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
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.model.Dimension;

import com.bedatadriven.rebar.sql.client.SqlResultSetRow;

public class EntityBundler implements Bundler {
    private final Dimension dimension;
    private final String idAlias;
    private final String labelAlias;

    public EntityBundler(Dimension key, String idAlias, String labelAlias) {
        this.dimension = key;
        this.idAlias = idAlias;
        this.labelAlias = labelAlias;
    }

	@Override
    public void bundle(SqlResultSetRow row, Bucket bucket)  {
		if(!row.isNull(idAlias)) {
	        bucket.setCategory(dimension, new EntityCategory(
	                row.getInt(idAlias),
	                row.getString(labelAlias)));
		}
    }
	
	
}