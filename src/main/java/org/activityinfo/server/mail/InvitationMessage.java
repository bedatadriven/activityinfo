/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.mail;

import org.activityinfo.server.database.hibernate.entity.User;

public class InvitationMessage extends MailMessage {
    private User newUser;
    private User invitingUser;

    public InvitationMessage(User newUser, User invitingUser) {
        this.newUser = newUser;
        this.invitingUser = invitingUser;
    }

    public User getNewUser() {
        return newUser;
    }

    public User getInvitingUser() {
        return invitingUser;
    }

	@Override
	public User getRecipient() {
		return newUser;
	}

}
