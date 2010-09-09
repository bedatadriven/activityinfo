/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

import org.sigmah.shared.domain.User;

/**
 * @author Alex Bertram
 */
public class ResetPasswordPageModel extends PageModel {

    private User user;

    public ResetPasswordPageModel(User user) {
        this.user = user;
    }


    public User getUser() {
        return user;
    }
}
