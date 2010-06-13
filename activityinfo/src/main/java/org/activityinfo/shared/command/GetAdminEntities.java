package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.AdminEntityResult;

public class GetAdminEntities extends GetListCommand<AdminEntityResult> {

	private int levelId;
	private Integer parentId;
    private Integer activityId;
	
	protected GetAdminEntities() {
		
	}

	public GetAdminEntities(int levelId) {
		this.levelId = levelId;
	}
	
	public GetAdminEntities(int levelId, Integer parentId) {
		super();
		this.levelId = levelId;
		this.parentId = parentId;
	}

    public GetAdminEntities(int levelId, Integer parentId, Integer activityId) {
        this.levelId = levelId;
        this.parentId = parentId;
        this.activityId = activityId;
    }

    public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GetAdminEntities that = (GetAdminEntities) o;

        if (levelId != that.levelId) {
            return false;
        }
        if (activityId != null ? !activityId.equals(that.activityId) : that.activityId != null) {
            return false;
        }
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) {
            return false;
        }

        return true;
    }


    @Override
    public int hashCode() {
        int result = levelId;
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (activityId != null ? activityId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GetEntities{levelId=" + levelId + ", parentId=" + (parentId==null ? "null" : parentId) + ", " +
                "activityId=" + activityId + "}";
    }


}