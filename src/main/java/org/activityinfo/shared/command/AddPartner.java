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

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.PartnerDTO;

/**
 * Adds a {@link org.activityinfo.server.database.hibernate.entity.Partner} to
 * the the given
 * {@link org.activityinfo.server.database.hibernate.entity.UserDatabase}
 * 
 * Returns {@link org.activityinfo.shared.command.result.VoidResult}
 * 
 * @author Alex Bertram
 */
public class AddPartner implements MutatingCommand<CreateResult> {

    private int databaseId;
    private PartnerDTO partner;

    public AddPartner() {

    }

    public AddPartner(int databaseId, PartnerDTO partner) {
        this.databaseId = databaseId;
        this.partner = partner;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public PartnerDTO getPartner() {
        return partner;
    }

    public void setPartner(PartnerDTO partner) {
        this.partner = partner;
    }
}