/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.mail;

import org.sigmah.server.domain.User;

public class Invitation {
    private User newUser;
    private User invitingUser;

    public Invitation(User newUser, User invitingUser) {
        this.newUser = newUser;
        this.invitingUser = invitingUser;
    }

    public User getNewUser() {
        return newUser;
    }

    public User getInvitingUser() {
        return invitingUser;
    }
}
