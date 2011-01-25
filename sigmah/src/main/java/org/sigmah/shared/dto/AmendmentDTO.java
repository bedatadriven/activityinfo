/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.domain.Amendment;
import org.sigmah.shared.dto.logframe.LogFrameDTO;

/**
 * DTO mapping class for {@link Amendment}s.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class AmendmentDTO extends BaseModelData implements EntityDTO {

    public AmendmentDTO() {
    }

    /**
     * Creates a new amendmentDTO using the values defined in the given project.
     * @param projectDTO a project DTO
     */
    public AmendmentDTO(ProjectDTO projectDTO) {
        set("id", 0); // No ID for this type of amendment
        set("version",projectDTO.getAmendmentVersion());
        set("revision",projectDTO.getAmendmentRevision());
        prepareName();
    }

    @Override
    public String getEntityName() {
        return "Amendment";
    }

    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public Integer getVersion() {
        return (Integer) get("version");
    }

    public void setVersion(Integer version) {
        set("version", version);
    }

    public Integer getRevision() {
        return (Integer) get("revision");
    }

    public void setRevision(Integer revision) {
        set("revision", revision);
    }

    public Amendment.State getState() {
        return get("state");
    }
    public void setState(Amendment.State state) {
        set("state", state);
    }

    public LogFrameDTO getLogFrameDTO() {
        return get("logFrameDTO");
    }

    public void setLogFrameDTO(LogFrameDTO logFrameDTO) {
        set("logFrameDTO", logFrameDTO);
    }

    /**
     * Initialize the "name" property with the version and revision numbers.
     */
    public final void prepareName() {
        String version = "0";
        String revision = "0";

        try {
            version = Integer.toString(getVersion());
            revision = Integer.toString(getRevision());
            
        } catch (NullPointerException e) {
        }

        set("text", I18N.MESSAGES.amendmentName(version, revision));
    }
}
