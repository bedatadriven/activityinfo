/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import org.junit.Test;
import org.sigmah.server.bootstrap.model.LoginPageModel;

public class LoginViewTest extends ViewTestCase {

    @Test
    public void templateProcesses() {
        assertProcessable(new LoginPageModel());
    }

    @Test
    public void templateWithUrlSuffixProcesses() {
        assertProcessable(new LoginPageModel("#charts"));
    }


}
