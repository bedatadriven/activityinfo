package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.AdminEntityModel;

import java.util.List;
/*
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
