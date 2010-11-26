package org.sigmah.shared.command;

import org.sigmah.shared.dto.UserInfoDTO;

/**
 * Retrieves all user info to store on client-side.
 * 
 * @author tmi
 * 
 */
public class GetUserInfo implements Command<UserInfoDTO> {

    private static final long serialVersionUID = 3131467894559905726L;

    /**
     * Found organization by user (as a member of this organization).
     */
    private Integer userId;

    public GetUserInfo() {
        // serialization
    }

    public GetUserInfo(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GetUserInfo other = (GetUserInfo) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GetUserInfo [");
        sb.append("user id: ");
        sb.append(userId);
        sb.append("]");
        return sb.toString();
    }
}
