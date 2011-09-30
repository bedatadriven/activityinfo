package org.sigmah.client.page.entry;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateParser;

public  class SiteTreeGridPageState implements PageState, SitePageState {
	public static PageId SITE_TREE_VIEW = new PageId("site-treeview");
	public enum TreeType {
		GEO,
		TIME
	}
	// Default on showing timed treeview
	private TreeType treeType = TreeType.TIME;
	private int activityId;
	
	// Time 
	private int month;
	private int year;
	
	// Geo
	private int adminEntityId;
	
    public TreeType getTreeType() {
		return treeType;
	}
	public int getActivityId() {
		return activityId;
	}
	public int getMonth() {
		return month;
	}
	public int getYear() {
		return year;
	}
	public int getAdminEntityId() {
		return adminEntityId;
	}
	public SiteTreeGridPageState setTreeType(TreeType treeType) {
		this.treeType = treeType;
		return this;
	}
	
	public SiteTreeGridPageState setActivityId(int activityId) {
		this.activityId = activityId;
		return this;
	}

	public static class Parser implements PageStateParser {
        @Override
        public PageState parse(String token) {
        	SiteTreeGridPageState place = new SiteTreeGridPageState();

            for(String t : token.split("/")) {
                if (t.startsWith("time")) {
	            	place.treeType = TreeType.TIME;
	            } else if (t.startsWith("geo")) {
	            	place.treeType = TreeType.GEO;
	            } else if (t.startsWith("month")) {
	            	place.month=Integer.parseInt(t.substring("month=".length()));
	            } else if (t.startsWith("year")) {
	            	place.year = Integer.parseInt(t.substring("year=".length()));
	            } else if (t.startsWith("adminEntity")) {
	            	place.adminEntityId = Integer.parseInt(t.substring("adminEntity=".length()));
	            } else {
                    place.activityId = Integer.parseInt(t);
	            }
            }
            
            return place;
        }
    }
	
	@Override
	public String serializeAsHistoryToken() {
		StringBuilder sb = new StringBuilder();
		sb.append(activityId);
		sb.append("/");
		if (treeType == TreeType.GEO) {
			sb.append("geo");
			sb.append("/");
			sb.append(adminEntityId);
		} else if (treeType == TreeType.TIME) {
			sb.append("time");
			sb.append("/");
			sb.append("year=");
			sb.append(year);
			sb.append("/");
			sb.append("month=");
			sb.append(month);
		}
		return sb.toString();
	}
	
	@Override
	public PageId getPageId() {
		return SITE_TREE_VIEW;
	}
	
	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(SITE_TREE_VIEW);
	}
}
