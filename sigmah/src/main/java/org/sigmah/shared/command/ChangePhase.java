package org.sigmah.shared.command;

import org.sigmah.shared.command.result.ProjectListResult;

/**
 * The command to change the current phase of a project. This command can be
 * used to ends a project too.
 * 
 * @author tmi
 * 
 */
public class ChangePhase implements Command<ProjectListResult> {

    private static final long serialVersionUID = -8923839444770227946L;

    /**
     * The project id.
     */
    private int projectId;

    /**
     * The id of the phase to activate. If this id is <code>null</code>, the
     * current phase will be ended and no other phase will be activated (closes
     * the project).
     */
    private Integer phaseId;

    public ChangePhase() {
        // required, or serialization exception
    }

    public ChangePhase(int projectId, Integer phaseId) {
        this.projectId = projectId;
        this.phaseId = phaseId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Integer getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Integer phaseId) {
        this.phaseId = phaseId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChangePhase other = (ChangePhase) obj;
        if (projectId != other.projectId)
            return false;
        if (phaseId != other.phaseId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        toString.append("ChangePhase:[projectId=").append(projectId);
        toString.append(";phaseId=").append(phaseId);
        toString.append("]");
        return toString.toString();
    }
}
