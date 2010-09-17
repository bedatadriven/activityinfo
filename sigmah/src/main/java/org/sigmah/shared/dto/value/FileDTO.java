package org.sigmah.shared.dto.value;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author tmi
 * 
 */
public class FileDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = -4655699567620520204L;

    private FileVersionDTO lastVersion;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "value.File";
    }

    // File's id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // File's name
    public String getName() {
        return (String) get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Reference to file's versions list
    public List<FileVersionDTO> getVersionsDTO() {
        return get("versionsDTO");
    }

    public void setVersionsDTO(List<FileVersionDTO> versionsDTO) {
        set("versionsDTO", versionsDTO);
    }

    /**
     * Returns the last version (with the higher version number).
     * 
     * @return the last version.
     */
    public FileVersionDTO getLastVersion() {

        if (lastVersion == null) {

            final List<FileVersionDTO> versions = getVersionsDTO();

            if (versions == null || versions.isEmpty()) {
                lastVersion = null;
            }

            // Searches the max version number which identifies the last
            // version.
            int index = 0;
            int maxVersionNumber = versions.get(index).getVersionNumber();
            for (int i = 1; i < versions.size(); i++) {
                if (versions.get(i).getVersionNumber() > maxVersionNumber) {
                    index = i;
                }
            }

            lastVersion = versions.get(index);
        }

        return lastVersion;
    }

}
