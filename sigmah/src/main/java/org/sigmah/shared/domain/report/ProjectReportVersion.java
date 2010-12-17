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
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.sigmah.shared.domain.User;

/**
 * Version of a project report.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
public class ProjectReportVersion implements Serializable {
    private Integer id;

    private ProjectReport report;

    /**
     * Version number of this copy. If null, it means this version is still a draft.
     */
    private Integer version;

    private Date editDate;

    /**
     * Author of this copy.
     */
    private User editor;

    private String phaseName;

    private List<RichTextElement> texts;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    public ProjectReport getReport() {
        return report;
    }

    public void setReport(ProjectReport report) {
        this.report = report;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
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

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    @OrderBy(value = "sectionId, index ASC")
    public List<RichTextElement> getTexts() {
        return texts;
    }

    public void setTexts(List<RichTextElement> texts) {
        this.texts = texts;
    }
}
