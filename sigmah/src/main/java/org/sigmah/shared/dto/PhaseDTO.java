/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PhaseDTO extends BaseModelData implements EntityDTO, Comparable<PhaseDTO> {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        return "Phase";
    }

    @Override
    public int getId() {
        return (Integer) get("id");
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

    /**
     * Returns if the phase id ended.
     */
    public boolean isEnded() {
        return getEndDate() != null;
    }

    /**
     * Returns if this phase is a successor of the given phase.
     * 
     * @param phase
     *            The phase.
     * @return If this phase is a successor of the given phase.
     */
    public boolean isSuccessor(PhaseDTO phase) {

        if (phase == null) {
            return false;
        }

        final List<PhaseModelDTO> successors = phase.getPhaseModelDTO().getSuccessorsDTO();
        if (successors != null) {
            for (final PhaseModelDTO successor : successors) {
                final PhaseDTO p = getParentProjectDTO().getPhaseFromModel(successor);
                if (this.equals(p)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int compareTo(PhaseDTO o) {
        if (getPhaseModelDTO() != null && o.getPhaseModelDTO() != null) {
            if (getPhaseModelDTO().getDisplayOrder() == o.getPhaseModelDTO().getDisplayOrder()) {
                return 0;
            } else if (getPhaseModelDTO().getDisplayOrder() > o.getPhaseModelDTO().getDisplayOrder()) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof PhaseDTO)) {
            return false;
        }

        final PhaseDTO other = (PhaseDTO) obj;

        return getId() == other.getId();
    }

}
