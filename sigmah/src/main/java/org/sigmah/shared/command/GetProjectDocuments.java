package org.sigmah.shared.command;

import java.io.Serializable;
import java.util.List;

import org.sigmah.shared.command.result.ProjectReportListResult;

public class GetProjectDocuments implements Command<ProjectReportListResult> {

    private static final long serialVersionUID = 6745399124869095436L;

    public static final class FilesListElement implements Serializable {

        private static final long serialVersionUID = 6451215796448622156L;

        private Long id;
        private String phaseName;
        private String elementLabel;

        public FilesListElement() {
            // Serialization.
        }

        public FilesListElement(Long id, String phaseName, String elementLabel) {
            this.id = id;
            this.phaseName = phaseName;
            this.elementLabel = elementLabel;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPhaseName() {
            return phaseName;
        }

        public void setPhaseName(String phaseName) {
            this.phaseName = phaseName;
        }

        public String getElementLabel() {
            return elementLabel;
        }

        public void setElementLabel(String elementLabel) {
            this.elementLabel = elementLabel;
        }
    }

    private Integer projectId;

    private List<FilesListElement> elements;

    public GetProjectDocuments() {
        // Serialization.
    }

    public GetProjectDocuments(Integer projectId, List<FilesListElement> elements) {
        this.projectId = projectId;
        this.elements = elements;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<FilesListElement> getElements() {
        return elements;
    }

    public void setElements(List<FilesListElement> elements) {
        this.elements = elements;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elements == null) ? 0 : elements.hashCode());
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GetProjectDocuments other = (GetProjectDocuments) obj;
        if (elements == null) {
            if (other.elements != null)
                return false;
        } else if (!elements.equals(other.elements))
            return false;
        if (projectId == null) {
            if (other.projectId != null)
                return false;
        } else if (!projectId.equals(other.projectId))
            return false;
        return true;
    }
}
