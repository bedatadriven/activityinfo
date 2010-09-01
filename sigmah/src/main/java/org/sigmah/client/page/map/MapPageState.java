/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Arrays;
import java.util.List;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.TabPage;

/**
 * @author Alex Bertram
 */
public class MapPageState implements PageState, TabPage {

    public PageId getPageId() {
        return MapPresenter.PAGE_ID;
    }

    public String serializeAsHistoryToken() {
        return null;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(getPageId());
    }

    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.maps();
    }
}
