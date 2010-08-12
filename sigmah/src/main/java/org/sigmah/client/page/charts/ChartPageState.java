/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartPageState implements PageState {

    public String serializeAsHistoryToken() {
        return null;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(getPageId());
    }

    @Override
    public PageId getPageId() {
        return ChartPagePresenter.PAGE_ID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChartPageState;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
