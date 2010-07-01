/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Collections;
import java.util.List;

public class ProjectListState implements PageState {

    @Override
    public String serializeAsHistoryToken() {
        return "";
    }

    @Override
    public PageId getPageId() {
        return ProjectListPresenter.PAGE_ID;
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Collections.singletonList(ProjectListPresenter.PAGE_ID);
    }
}
