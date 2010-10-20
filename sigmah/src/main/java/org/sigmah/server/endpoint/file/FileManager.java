package org.sigmah.server.endpoint.file;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.sigmah.shared.dto.value.FileUploadUtils;

/**
 * Manages files (upload and download).
 * 
 * @author tmi
 * 
 */
public interface FileManager {

    /**
     * Saves and stores a new file version. If the file doesn't exist already,
     * creates it.
     * 
     * @param properties
     *            The properties map of the uploaded file (see
     *            {@link FileUploadUtils}).
     * @param content
     *            The uploaded file content.
     * @return The id of the just saved file.
     * @throws IOException
     */
    public String save(Map<String, String> properties, byte[] content) throws IOException;

    /**
     * Returns the file for the given id and version number.
     * 
     * @param idString
     *            The file entity id.
     * @param versionString
     *            The desired version number.
     * @return The corresponding file.
     */
    public DonwloadableFile getFile(String idString, String versionString);

    /**
     * Retrieves an image with the given.
     * 
     * @param name
     *            The image's name.
     * @return The image as a file.
     */
    public File getImage(String name);

    /**
     * Utility class to represents a downloaded file.
     * 
     * @author tmi
     * 
     */
    public static class DonwloadableFile {

        /**
         * The file's name (version-independent).
         */
        private final String name;

        /**
         * The file's content (for the expected version).
         */
        private final java.io.File physicalFile;

        public DonwloadableFile(String name, java.io.File physicalFile) {
            super();
            this.name = name;
            this.physicalFile = physicalFile;
        }

        public String getName() {
            return name;
        }

        public java.io.File getPhysicalFile() {
            return physicalFile;
        }
    }
}
