/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.charts;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Arrays;
import java.util.List;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.TabPage;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ChartPageState implements PageState, TabPage {

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

    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.charts();
    }
}
