package org.sigmah.client.page.orgunit;

import java.util.Collections;
import java.util.List;

import org.sigmah.client.page.HasTab;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.ui.Tab;

public class OrgUnitState implements PageState, TabPage, HasTab {

    private int orgUnitId;
    private String tabTitle;
    private Tab tab;

    private Integer currentSection;
    private String argument;

    public OrgUnitState(int orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public static class Parser implements PageStateParser {

        @Override
        public PageState parse(String token) {
            final String[] tokens = token.split("/");

            final OrgUnitState state = new OrgUnitState(Integer.parseInt(tokens[0]));

            if (tokens.length > 1) {
                state.setCurrentSection(Integer.parseInt(tokens[1]));

                if (tokens.length > 2) {
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

        if (currentSection != null)
            tokenBuilder.append(currentSection.toString());

        if (argument != null)
            tokenBuilder.append('/').append(argument);

        if (tokenBuilder.length() == 0)
            return null;
        else
            return tokenBuilder.toString();
    }

    public OrgUnitState deriveTo(int section) {
        final OrgUnitState derivation = new OrgUnitState(orgUnitId);
        derivation.setCurrentSection(section);
        return derivation;
    }

    @Override
    public PageId getPageId() {
        return new PageId(OrgUnitPresenter.PAGE_ID.toString() + '!' + orgUnitId);
    }

    @Override
    public List<PageId> getEnclosingFrames() {
        return Collections.singletonList(OrgUnitPresenter.PAGE_ID);
    }

    public int getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(int orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public int getCurrentSection() {
        if (currentSection == null)
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
        final OrgUnitState other = (OrgUnitState) obj;
        if (this.orgUnitId != other.orgUnitId) {
            return false;
        }
        if (this.currentSection != other.currentSection
                && (this.currentSection == null || !this.currentSection.equals(other.currentSection))) {
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
        hash = 83 * hash + this.orgUnitId;
        hash = 83 * hash + (this.currentSection != null ? this.currentSection.hashCode() : 0);
        hash = 83 * hash + (this.argument != null ? this.argument.hashCode() : 0);
        return hash;
    }
}
