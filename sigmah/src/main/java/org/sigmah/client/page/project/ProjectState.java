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
    private String argument;

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

                if(tokens.length > 2) {
                    state.setArgument(tokens[2]);
                } else {
                    state.argument = null;
                }

            }

            return state;
        }
    }

    @Override
    public String serializeAsHistoryToken() {
        StringBuilder tokenBuilder = new StringBuilder();

        if(currentSection != null)
            tokenBuilder.append(currentSection.toString());

        if(argument != null)
            tokenBuilder.append('/').append(argument);

        if(tokenBuilder.length() == 0)
            return null;
        else
            return tokenBuilder.toString();
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

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectState other = (ProjectState) obj;
        if (this.projectId != other.projectId) {
            return false;
        }
        if (this.currentSection != other.currentSection && (this.currentSection == null || !this.currentSection.equals(other.currentSection))) {
            return false;
        }
        if ((this.argument == null) ? (other.argument != null) : !this.argument.equals(other.argument)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.projectId;
        hash = 83 * hash + (this.currentSection != null ? this.currentSection.hashCode() : 0);
        hash = 83 * hash + (this.argument != null ? this.argument.hashCode() : 0);
        return hash;
    }
}
