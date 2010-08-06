/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.table;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

import java.util.Arrays;
import java.util.List;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.TabPage;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPageState implements PageState, TabPage {

    @Override
    public PageId getPageId() {
        return PivotPresenter.Pivot;
    }

    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(PivotPresenter.Pivot);
    }

    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.tables();
    }

    public static class Parser implements PageStateParser {
        @Override
        public PageState parse(String token) {
            return new PivotPageState();
        }
    }
}
