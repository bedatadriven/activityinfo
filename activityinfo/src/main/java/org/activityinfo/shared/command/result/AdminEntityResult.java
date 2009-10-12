package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.AdminEntityModel;

import java.util.List;

/**
 * Result of a <code>GetAdminEntities</code> command.
 *
 * @see org.activityinfo.shared.command.GetAdminEntities
 *
 * @author Alex Bertram
 */
public class AdminEntityResult extends ListResult<AdminEntityModel> {

    public AdminEntityResult() {
        super();
    }

    public AdminEntityResult(List<AdminEntityModel> data) {
        super(data);
    }

    public AdminEntityResult(AdminEntityResult result) {
        super(result);
    }
}
