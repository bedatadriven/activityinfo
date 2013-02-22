

package org.activityinfo.shared.command;

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

import java.util.Map;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.RpcMap;

/**
 *
 * Updates a domain entity on the server.
 *
 * Some entities require specialized commands to create or update. See:
 * <ul>
 * <li>{@link org.activityinfo.shared.command.AddPartner}</li>
 * <li>{@link UpdateUserPermissions}</li>
 * <li>{@link org.activityinfo.shared.command.UpdateSubscription}</li>
 * </ul>
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class UpdateEntity implements MutatingCommand<VoidResult> {

	private int id;
	private String entityName;
	private RpcMap changes;

    protected UpdateEntity() {
		
	}

    public UpdateEntity(String entityName, int id, Map<String, Object> changes) {
        this.entityName = entityName;
        this.id = id;
        this.changes = new RpcMap();
        this.changes.putAll(changes);
    }

	public UpdateEntity(EntityDTO model, Map<String, Object> changes) {
		this.id = model.getId();
        this.entityName = model.getEntityName();
		this.changes = new RpcMap();
		this.changes.putAll(changes);
	}

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public RpcMap getChanges() {
		return changes;
	}

	public void setChanges(RpcMap changes) {
		this.changes = changes;
	}


}
