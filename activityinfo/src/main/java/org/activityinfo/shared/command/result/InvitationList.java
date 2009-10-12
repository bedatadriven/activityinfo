package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.InvitationRow;

import java.util.List;

/**
 *
 * @author Alex Bertram
 */
public class InvitationList extends PagingResult<InvitationRow> {

    public InvitationList() {
    }

    public InvitationList(List<InvitationRow> data) {
        super(data);
    }
}
