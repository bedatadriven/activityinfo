/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PhaseDTO extends BaseModelData implements EntityDTO, Comparable<PhaseDTO> {
    
	private static final long serialVersionUID = 8520711106031085130L;

	@Override
	public String getEntityName() {
		return "Phase";
	}
	
	@Override
    public int getId() {
        return (Integer)get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public Date getStartDate() {
        return get("startDate");
    }

    public void setStartDate(Date startDate) {
        set("startDate", startDate);
    }
    
    public Date getEndDate() {
        return get("endDate");
    }

    public void setEndDate(Date endDate) {
        set("endDate", endDate);
    }
    
    public ProjectDTO getParentProjectDTO() {
        return get("parentProjectDTO");
    }

    public void setParentProjectDTO(ProjectDTO parentProjectDTO) {
        set("parentProjectDTO", parentProjectDTO);
    }
    
    public PhaseModelDTO getPhaseModelDTO() {
        return get("phaseModelDTO");
    }

    public void setPhaseModelDTO(PhaseModelDTO phaseModelDTO) {
        set("phaseModelDTO", phaseModelDTO);
    }
    
	@Override
	public int compareTo(PhaseDTO o) {
		if (getPhaseModelDTO() != null && o.getPhaseModelDTO() != null) {
			if (getPhaseModelDTO().getDisplayOrder() == o.getPhaseModelDTO().getDisplayOrder()) {
				return 0;
			}
			else if (getPhaseModelDTO().getDisplayOrder() > o.getPhaseModelDTO().getDisplayOrder()) {
				return 1;
			}
			else {
				return -1;
			}
		}
		return 0;
	}
    
}
