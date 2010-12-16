/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.sigmah.shared.domain.logframe.LogFrame;
import org.sigmah.shared.domain.reminder.MonitoredPointList;
import org.sigmah.shared.domain.reminder.ReminderList;

@Entity
public class Project extends UserDatabase {
    private static final long serialVersionUID = 3838595995254049090L;

    private LogFrame logFrame;
    private ProjectModel projectModel;
    private Phase currentPhase;
    private List<Phase> phases = new ArrayList<Phase>();
    private Integer calendarId;
    private Double plannedBudget;
    private Double spendBudget;
    private Double receivedBudget;
    private List<ProjectFunding> funding;
    private List<ProjectFunding> funded;
    private Date endDate;
    private Date closeDate;
    private MonitoredPointList pointsList;
    private ReminderList remindersList;
    private Boolean starred = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", length = 23)
    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setLogFrame(LogFrame logFrame) {
        this.logFrame = logFrame;
    }

    @OneToOne(mappedBy = "parentProject", cascade = CascadeType.ALL)
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
     * 
     * @return the ID of the calendar attached to this project.
     */
    public Integer getCalendarId() {
        return calendarId;
    }

    /**
     * Defines the ID of the calendar attached to this project.
     * 
     * @param calendarId
     *            the ID of the calendar to attach.
     */
    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    @Column(name = "planned_budget", nullable = true)
    public Double getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(Double plannedBudget) {
        this.plannedBudget = plannedBudget;
    }

    @Column(name = "spend_budget", nullable = true)
    public Double getSpendBudget() {
        return spendBudget;
    }

    public void setSpendBudget(Double spendBudget) {
        this.spendBudget = spendBudget;
    }

    @Column(name = "received_budget", nullable = true)
    public Double getReceivedBudget() {
        return receivedBudget;
    }

    public void setReceivedBudget(Double receivedBudget) {
        this.receivedBudget = receivedBudget;
    }

    @ManyToMany(mappedBy = "funded", cascade = CascadeType.ALL)
    public List<ProjectFunding> getFunding() {
        return funding;
    }

    public void setFunding(List<ProjectFunding> funding) {
        this.funding = funding;
    }

    @ManyToMany(mappedBy = "funding", cascade = CascadeType.ALL)
    public List<ProjectFunding> getFunded() {
        return funded;
    }

    public void setFunded(List<ProjectFunding> funded) {
        this.funded = funded;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_monitored_points_list", nullable = true)
    public MonitoredPointList getPointsList() {
        return pointsList;
    }

    public void setPointsList(MonitoredPointList pointsList) {
        this.pointsList = pointsList;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reminder_list", nullable = true)
    public ReminderList getRemindersList() {
        return remindersList;
    }

    public void setRemindersList(ReminderList remindersList) {
        this.remindersList = remindersList;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Project)) {
            return false;
        }

        final Project other = (Project) obj;

        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    @Column(name = "starred", nullable = true)
    public Boolean getStarred() {
        return starred;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "close_date", length = 23)
    public Date getCloseDate() {
        return closeDate;
    }
}
