package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.UserPermissionDTO;

import java.util.List;

/**
 * @see org.activityinfo.shared.command.GetUsers
 *
 * @author Alex Bertram
 */
public class UserResult extends PagingResult<UserPermissionDTO> {
    
    public UserResult() {
    }

    public UserResult(List<UserPermissionDTO> data) {
        super(data);
    }

    public UserResult(List<UserPermissionDTO> data, int offset, int totalCount) {
        super(data, offset, totalCount);
    }
}
