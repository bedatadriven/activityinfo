package org.sigmah.shared.dto.value;

import java.util.Date;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity element.FileVersion.
 * 
 * @author tmi
 * 
 */
public class FileVersionDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 831743691477321862L;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "value.FileVersion";
    }

    // Version's id
    @Override
    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    // Version's number
    public int getVersionNumber() {
        return (Integer) get("versionNumber");
    }

    public void setVersionNumber(int versionNumber) {
        set("versionNumber", versionNumber);
    }

    // Version's path
    public String getPath() {
        return (String) get("path");
    }

    public void setPath(String path) {
        set("path", path);
    }

    // Version's added date
    public Date getAddedDate() {
        return (Date) get("addedDate");
    }

    public void setAddedDate(Date addedDate) {
        set("addedDate", addedDate);
    }

    // Version's added date
    public long getSize() {
        return (Long) get("size");
    }

    public void setSize(long size) {
        set("size", size);
    }

    // Version's author name
    public String getAuthorName() {
        return (String) get("authorName");
    }

    public void setAuthorName(String authorName) {
        set("authorName", authorName);
    }

    // Version's author first name
    public String getAuthorFirstName() {
        return (String) get("authorFirstName");
    }

    public void setAuthorFirstName(String authorFirstName) {
        set("authorFirstName", authorFirstName);
    }

    // Version's name.
    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    // Version's extension.
    public String getExtension() {
        return get("extension");
    }

    public void setExtension(String extension) {
        set("extension", extension);
    }
}
