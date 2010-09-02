package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.ProjectModelDTOLight;

/**
 * List of project models.
 * 
 * @author tmi
 * 
 */
public class ProjectModelListResult implements CommandResult {

    private static final long serialVersionUID = 7244042578208218094L;

    private List<ProjectModelDTOLight> list;

    public ProjectModelListResult() {
    }

    public ProjectModelListResult(List<ProjectModelDTOLight> list) {
        this.list = list;
    }

    public List<ProjectModelDTOLight> getList() {
        return list;
    }

    public void setList(List<ProjectModelDTOLight> list) {
        this.list = list;
    }
}