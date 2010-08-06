/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.project;

import java.util.Arrays;
import java.util.List;
import org.sigmah.client.page.HasTab;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.ui.Tab;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectState implements PageState, TabPage, HasTab {
    private final PageId pageId;
    private final int projectId;
    private PageId sectionId;
    private Tab tab;
    
    private String title;
    
    public ProjectState(int projectId) {
        pageId = new PageId(ProjectPresenter.PAGE_ID.toString() + '!' + projectId);
        this.projectId = projectId;
        this.sectionId = new PageId("welcome");
        this.title = null;
    }
    
    @Override
    public String serializeAsHistoryToken() {
        return null;
    }

    @Override
    public PageId getPageId() {
        return pageId;
    }
    
    public void setSection(String section) {
        this.sectionId = new PageId(section);
    }

    @Override
    public String getTabTitle() {
        return title;
    }
    
    public void setTabTitle(String title) {
        this.title = title;
        if(tab != null)
            tab.setTitle(title);
    }

    public int getProjectId() {
        return projectId;
    }

    @Override
    public Tab getTab() {
        return tab;
    }

    @Override
    public void setTab(Tab tab) {
        this.tab = tab;
    }
    
    @Override
    public List<PageId> getEnclosingFrames() {
        return Arrays.asList((PageId)ProjectPresenter.PAGE_ID, sectionId);
    }
    
    public static class Parser implements PageStateParser {
        @Override
        public PageState parse(String token) {
            final String[] tokens = token.split("/");
            
            final ProjectState state = new ProjectState(Integer.parseInt(tokens[0]));
            
            return state;
        }
    }
}
