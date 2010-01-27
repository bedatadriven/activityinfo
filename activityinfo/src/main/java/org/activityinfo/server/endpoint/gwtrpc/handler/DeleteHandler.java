/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.SiteDAO;
import org.activityinfo.server.domain.*;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.result.CommandResult;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.Delete
 * @see org.activityinfo.server.domain.Deleteable
 */
public class DeleteHandler implements CommandHandler<Delete> {
    private final SchemaDAO schemaDAO;
    private final SiteDAO siteDAO;

    @Inject
    public DeleteHandler(SchemaDAO schemaDAO, SiteDAO siteDAO) {
        this.schemaDAO = schemaDAO;
        this.siteDAO = siteDAO;
    }

    @Override
    public CommandResult execute(Delete cmd, User user) {

        Deleteable entity;

        // TODO check permissions for delete!

        if ("Site".equals(cmd.getEntityName())) {
            entity = siteDAO.findById(cmd.getId());
        } else if ("UserDatabase".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(UserDatabase.class, cmd.getId());
        } else if ("Activity".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(Activity.class, cmd.getId());
        } else if ("AttributeGroup".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(AttributeGroup.class, cmd.getId());
        } else if ("Attribute".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(Attribute.class, cmd.getId());
        } else if ("Indicator".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(Indicator.class, cmd.getId());
        } else {
            throw new RuntimeException("Cannot delete entity type " + cmd.getEntityName());
        }

        if (entity == null)
            return null;


        ((Deleteable) entity).delete();

        return null;
    }
}
