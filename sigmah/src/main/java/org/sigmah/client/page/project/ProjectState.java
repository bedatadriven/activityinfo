/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project;

import java.util.Collections;
import java.util.List;

import org.sigmah.client.page.HasTab;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.ui.Tab;

/**
 * Serialized state of a project page.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectState implements PageState, TabPage, HasTab {

    private int projectId;
    private String tabTitle;
    private Tab tab;

    private Integer currentSection;

    public ProjectState(int projectId) {
        this.projectId = projectId;
    }

    public static class Parser implements PageStateParser {

        @Override
        public PageState parse(String token) {
            final String[] tokens = token.split("/");

            final ProjectState state = new ProjectState(Integer.parseInt(tokens[0]));

            if(tokens.length > 1) {
                state.setCurrentSection(Integer.parseInt(tokens[1]));
            }

            return state;
        }
    }

    @Override
    public String serializeAsHistoryToken() {
        if(currentSection != null)
            return currentSection.toString();
        else
            return null;
    }

    public ProjectState deriveTo(int section) {
        final ProjectState derivation = new ProjectState(projectId);
        derivation.setCurrentSection(section);
        return derivation;
    }

    @Override
    public PageId getPageId() {
        return new PageId(ProjectPresenter.PAGE_ID.toString() + '!' + projectId);
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Collections.singletonList(ProjectPresenter.PAGE_ID);
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getCurrentSection() {
        if(currentSection == null)
            return 0;
        else
            return currentSection;
    }

    public void setCurrentSection(Integer currentSection) {
        this.currentSection = currentSection;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
        if (tab != null) {
            tab.setTitle(tabTitle);
        }
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    @Override
    public Tab getTab() {
        return tab;
    }

    @Override
    public void setTab(Tab tab) {
        this.tab = tab;
    }
}
