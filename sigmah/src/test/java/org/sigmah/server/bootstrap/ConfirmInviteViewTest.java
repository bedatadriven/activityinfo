/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import org.junit.Test;
import org.sigmah.server.bootstrap.model.ConfirmInvitePageModel;
import org.sigmah.server.bootstrap.model.InvalidInvitePageModel;
import org.sigmah.shared.domain.User;

public class ConfirmInviteViewTest extends ViewTestCase {

    @Test
    public void templateProcesses() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@bertram");
        user.setChangePasswordKey("ABC12345");
        user.setLocale("en");

        assertProcessable(new ConfirmInvitePageModel(user));
    }

    @Test
    public void invalidInvitePage() {

        assertProcessable(new InvalidInvitePageModel());

    }

}
