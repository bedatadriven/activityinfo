/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.UserPermissionDTO;

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
