/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.report;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Entity
public class ProjectReportModelSection implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private Integer id;
    private Integer projectModelId;
    private Integer parentSectionModelId;
    private String name;
    private Integer index;
    private Integer numberOfTextarea;
    private List<ProjectReportModelSection> subSections;
    private List<KeyQuestion> keyQuestions;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentSectionModelId() {
        return parentSectionModelId;
    }

    public void setParentSectionModelId(Integer parentSectionModelId) {
        this.parentSectionModelId = parentSectionModelId;
    }

    public Integer getProjectModelId() {
        return projectModelId;
    }

    public void setProjectModelId(Integer projectModelId) {
        this.projectModelId = projectModelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getNumberOfTextarea() {
        return numberOfTextarea;
    }

    public void setNumberOfTextarea(Integer numberOfTextarea) {
        this.numberOfTextarea = numberOfTextarea;
    }

    @OneToMany(mappedBy = "parentSectionModelId")
    @OrderBy("index ASC")
    public List<ProjectReportModelSection> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<ProjectReportModelSection> subSections) {
        this.subSections = subSections;
    }

    @OneToMany(mappedBy = "sectionId")
    @OrderBy("index ASC")
    public List<KeyQuestion> getKeyQuestions() {
        return keyQuestions;
    }
    
    public void setKeyQuestions(List<KeyQuestion> keyQuestions) {
        this.keyQuestions = keyQuestions;
    }
}
