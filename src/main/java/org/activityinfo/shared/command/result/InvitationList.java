package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.InvitationDTO;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class InvitationList extends PagingResult<InvitationDTO> {

    public InvitationList() {
    }

    public InvitationList(List<InvitationDTO> data) {
        super(data);
    }
}
