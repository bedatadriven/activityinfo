package org.activityinfo.server.command.handler;

import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.dao.SiteDAO;
import org.activityinfo.server.domain.*;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.UnexpectedCommandException;

import com.google.inject.Inject;

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

        if("Site".equals(cmd.getEntityName())) {
            entity = siteDAO.findSiteById(cmd.getId());
        } else if("Database".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(UserDatabase.class, cmd.getId());
        } else if("Activity".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(Activity.class, cmd.getId());
        } else if("AttributeGroup".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(AttributeGroup.class, cmd.getId());
        } else if("Attribute".equals(cmd.getEntityName())) {
            entity = schemaDAO.findById(Attribute.class, cmd.getId());
        } else if("Indicator".equals(cmd.getEntityName())) {
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
