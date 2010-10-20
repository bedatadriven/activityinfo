/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.sigmah.shared.domain.value.Value;

@Entity
public class Project extends UserDatabase {
    private static final long serialVersionUID = 3838595995254049090L;

    private LogFrame logFrame;
    private ProjectModel projectModel;
    private Phase currentPhase;
    private List<Phase> phases = new ArrayList<Phase>();
    private List<Value> values = new ArrayList<Value>();
    private Integer calendarId;

    public void setLogFrame(LogFrame logFrame) {
        this.logFrame = logFrame;
    }

    @OneToOne
    public LogFrame getLogFrame() {
        return logFrame;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_project_model")
    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel model) {
        this.projectModel = model;
    }

    @OneToOne
    @JoinColumn(name = "id_current_phase")
    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    @OneToMany(mappedBy = "parentProject", cascade = CascadeType.ALL)
    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    @OneToMany(mappedBy = "parentProject", cascade = CascadeType.ALL)
    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    /**
     * Adds a phase to the project.
     * 
     * @param phase
     *            The new phase.
     */
    public void addPhase(Phase phase) {

        if (phase == null) {
            return;
        }

        phases.add(phase);
        phase.setParentProject(this);
    }

    /**
     * Retrieves the ID of the calendar attached to this project.
     * @return the ID of the calendar attached to this project.
     */
    public Integer getCalendarId() {
        return calendarId;
    }

    /**
     * Defines the ID of the calendar attached to this project.
     * @param calendarId the ID of the calendar to attach.
     */
    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }
}
