package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.UserModel;

import java.util.List;

/**
 * @see org.activityinfo.shared.command.GetUsers
 *
 * @author Alex Bertram
 */
public class UserResult extends PagingResult<UserModel> {
    
    public UserResult() {
    }

    public UserResult(List<UserModel> data) {
        super(data);
    }

    public UserResult(List<UserModel> data, int offset, int totalCount) {
        super(data, offset, totalCount);
    }
}
