/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.report;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
public class RichTextElement implements Serializable {
    private Integer id;
    private ProjectReportVersion version;
    private Integer sectionId;
    private Integer index;
    private String text;

    /**
     * Creates a new RichTextElement and fill some of its values.<br>
     * <br>
     * Not similar to the clone method since it doesn't copy every fields.
     * @return A new RichTextElement object.
     */
    public RichTextElement duplicate() {
        final RichTextElement duplicate = new RichTextElement();
        duplicate.sectionId = this.sectionId;
        duplicate.index = this.index;
        duplicate.text = this.text;

        return duplicate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    public ProjectReportVersion getVersion() {
        return version;
    }

    public void setVersion(ProjectReportVersion version) {
        this.version = version;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    @Column(columnDefinition="TEXT")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
