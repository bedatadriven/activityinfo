/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.dialog;

import org.sigmah.client.dispatch.AsyncMonitor;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface SaveChangesCallback {

    public void save(AsyncMonitor monitor);

    public void cancel();

    public void discard();

}
