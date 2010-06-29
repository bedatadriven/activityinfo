package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.UserPermissionDTO;

import java.util.List;

/**
 * @see org.sigmah.shared.command.GetUsers
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
