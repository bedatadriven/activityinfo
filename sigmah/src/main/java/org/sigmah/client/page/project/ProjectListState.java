/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Collections;
import java.util.List;
import org.sigmah.client.page.PageStateParser;

/**
 * Serialized state of the project list page.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectListState implements PageState {
    
    public static class Parser implements PageStateParser {
        private final static ProjectListState instance = new ProjectListState();
        
        @Override
        public PageState parse(String token) {
            return instance;
        }
    }

    @Override
    public String serializeAsHistoryToken() {
        return null;
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
