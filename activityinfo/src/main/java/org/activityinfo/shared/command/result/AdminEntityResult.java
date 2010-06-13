package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.AdminEntityDTO;

import java.util.List;

/**
 * Result of a <code>GetAdminEntities</code> command.
 *
 * @see org.activityinfo.shared.command.GetAdminEntities
 *
 * @author Alex Bertram
 */
public class AdminEntityResult extends ListResult<AdminEntityDTO> {

    public AdminEntityResult() {
        super();
    }

    public AdminEntityResult(List<AdminEntityDTO> data) {
        super(data);
    }

    public AdminEntityResult(AdminEntityResult result) {
        super(result);
    }
}
