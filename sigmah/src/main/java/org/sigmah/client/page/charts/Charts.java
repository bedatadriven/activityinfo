/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import org.sigmah.client.page.PageId;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class Charts {

    public static class ChartPageId extends PageId {
        public ChartPageId(String id) {
            super(id);
        }

    }

    public static final PageId Charts = new ChartPageId("charts");


}
