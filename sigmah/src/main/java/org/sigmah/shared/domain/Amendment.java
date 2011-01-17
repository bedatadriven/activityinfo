/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.history.HistoryToken;
import org.sigmah.shared.domain.logframe.LogFrame;

/**
 * Represents a version of a project.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
public class Amendment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * State of an amendment.
     */
    public static enum State implements CommandResult {
        DRAFT(Action.LOCK),
        LOCKED(Action.UNLOCK, Action.REJECT, Action.VALIDATE),
        ACTIVE(Action.CREATE),
        REJECTED(),
        ARCHIVED();

        private final Action[] actions;
        private State(Action... actions) {
            this.actions = actions;
        }

        /**
         * Retrieves the list of actions that are available when an amendment is in a given state.
         * @return An array containing the possible actions.
         */
        public Action[] getActions() {
            return actions;
        }
    }

    /**
     * Possible user interaction with an amendment.
     */
    public static enum Action {
        CREATE,
        LOCK,
        UNLOCK,
        REJECT,
        VALIDATE;
    }

    private Integer id;
    private Project parentProject;
    private Integer version;
    private Integer revision;
    private State state;
    private LogFrame logFrame;
    private List<HistoryToken> values;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_amendment")
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "id_project")
    public Project getParentProject() {
        return parentProject;
    }
    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    @Column(name = "version")
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(name = "revision")
    public Integer getRevision() {
        return revision;
    }
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_log_frame", nullable = true)
    public LogFrame getLogFrame() {
        return logFrame;
    }
    public void setLogFrame(LogFrame logFrame) {
        this.logFrame = logFrame;
    }

    @ManyToMany
    public List<HistoryToken> getValues() {
        return values;
    }
    public void setValues(List<HistoryToken> values) {
        this.values = values;
    }

}
