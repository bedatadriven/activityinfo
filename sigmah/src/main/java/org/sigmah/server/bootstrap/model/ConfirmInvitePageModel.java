/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap.model;

import org.sigmah.shared.domain.User;

public class ConfirmInvitePageModel extends PageModel {
    private User user;
    private boolean formIncomplete;

    public ConfirmInvitePageModel(User user) {
        this.setUser(user);
    }

    public static ConfirmInvitePageModel incompleteForm(User user) {
        ConfirmInvitePageModel model = new ConfirmInvitePageModel(user);
        model.setFormIncomplete(true);
        return model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFormIncomplete() {
        return formIncomplete;
    }

    public void setFormIncomplete(boolean formIncomplete) {
        this.formIncomplete = formIncomplete;
    }
}
