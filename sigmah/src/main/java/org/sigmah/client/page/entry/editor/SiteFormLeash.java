/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import org.sigmah.shared.dto.SiteDTO;

public interface SiteFormLeash {

    public void setSite(SiteDTO site);

    public  int getActivityId();

    public void destroy();
}
