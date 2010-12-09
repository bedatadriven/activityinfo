/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.element.FlexibleElement;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
public class ProjectReport implements Serializable {

    private static final long serialVersionUID = -7388489166961720683L;

    private Integer id;
    private Project project;
    private ProjectReportModel model;
    private String phaseName;
    private String name;
    private FlexibleElement flexibleElement;

    private List<RichTextElement> texts;

    private Date lastEditDate;
    private User editor;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    public ProjectReportModel getModel() {
        return model;
    }

    public void setModel(ProjectReportModel model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @OneToOne
    public FlexibleElement getFlexibleElement() {
        return flexibleElement;
    }

    public void setFlexibleElement(FlexibleElement flexibleElement) {
        this.flexibleElement = flexibleElement;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @OrderBy(value = "sectionId, index ASC")
    public List<RichTextElement> getTexts() {
        return texts;
    }

    public void setTexts(List<RichTextElement> texts) {
        this.texts = texts;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    @ManyToOne
    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    @Transient
    public String getEditorShortName() {
        return User.getUserShortName(editor);
    }
}
