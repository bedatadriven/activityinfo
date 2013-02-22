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

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.EntityDTO;

/**
 * Deletes a database entity.
 * 
 * Note: the specified entity must be <code>Deletable</code>
 * 
 * Returns <code>VoidResult</code>
 * 
 * @see org.activityinfo.server.database.hibernate.entity.Deleteable
 * 
 */
public class Delete implements MutatingCommand<VoidResult> {

    private String entityName;
    private int id;

    protected Delete() {

    }

    public Delete(EntityDTO entity) {
        this.entityName = entity.getEntityName();
        this.id = entity.getId();
    }

    public Delete(String entityName, int id) {
        this.entityName = entityName;
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
