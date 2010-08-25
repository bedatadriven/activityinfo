/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.value;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class FilesListValueDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "value.FilesListValue";
    }

    // File id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Files list id
    public long getIdList() {
        return (Long) get("idList");
    }

    public void setIdList(long idList) {
        set("idList", idList);
    }

    // File metadata
    public FileDTO getFileDTO() {
        return (FileDTO) get("fileDTO");
    }

    public void setFileDTO(FileDTO fileDTO) {
        set("fileDTO", fileDTO);
    }

}
