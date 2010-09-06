package org.sigmah.server.endpoint.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.dao.Transactional;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.server.domain.value.File;
import org.sigmah.server.domain.value.FileVersion;
import org.sigmah.server.domain.value.FilesListValue;
import org.sigmah.server.domain.value.FilesListValueId;
import org.sigmah.server.domain.value.Value;
import org.sigmah.shared.dto.value.FileUploadUtils;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Manages files (upload and download).
 * 
 * @author tmi
 * 
 */
public class FileManagerImpl implements FileManager {

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(FileManagerImpl.class);

    /**
     * To get the entity manager.
     */
    private final Injector injector;

    /**
     * Directory's path name where the uploaded files are stored.
     */
    private final String UPLOADED_FILES_REPOSITORY;

    /**
     * The property's name expected in the properties file to set the repository
     * path name.
     */
    private static final String UPLOADED_FILES_REPOSITORY_NAME = "rootDir";

    @Inject
    public FileManagerImpl(Properties configProperties, Injector injector) {
        UPLOADED_FILES_REPOSITORY = configProperties.getProperty(UPLOADED_FILES_REPOSITORY_NAME);

        if (UPLOADED_FILES_REPOSITORY == null) {
            throw new IllegalStateException("Missing reqquired property '" + UPLOADED_FILES_REPOSITORY_NAME
                    + "' in the upload properties file.");
        }

        this.injector = injector;
    }

    @Override
    public String save(Map<String, String> properties, byte[] content) throws IOException {

        // Uploaded file's id.
        String id = properties.get(FileUploadUtils.DOCUMENT_ID);

        // Author id.
        String authorIdProp = properties.get(FileUploadUtils.DOCUMENT_AUTHOR);
        int authorId;

        try {
            if (authorIdProp == null) {
                authorId = 0;
            } else {
                authorId = Integer.valueOf(authorIdProp);
            }
        } catch (NumberFormatException e) {
            authorId = 0;
        }

        // New file (first version).
        if (id == null) {
            id = saveNewFile(properties, content, authorId);
        }
        // New version.
        else {
            id = saveNewVersion(properties, content, id, authorId);
        }

        return id;
    }

    /**
     * Saves a new file.
     * 
     * @param properties
     *            The properties map of the uploaded file (see
     *            {@link FileUploadUtils}).
     * @param content
     *            The uploaded file content.
     * @param authorId
     *            The author id.
     * @return The id of the just saved file.
     * @throws IOException
     */
    @Transactional
    protected String saveNewFile(Map<String, String> properties, byte[] content, int authorId) throws IOException {

        final EntityManager em = injector.getInstance(EntityManager.class);

        if (log.isDebugEnabled()) {
            log.debug("[save] New file.");
        }

        // Files list id.
        String filesListIdProp = properties.get(FileUploadUtils.DOCUMENT_FILES_LIST);
        long filesListId;

        try {
            if (filesListIdProp == null) {
                filesListId = 0;
            } else {
                filesListId = Integer.valueOf(filesListIdProp);
            }
        } catch (NumberFormatException e) {
            filesListId = 0;
        }

        final File file = new File();
        file.setName(properties.get(FileUploadUtils.DOCUMENT_NAME));

        // Creates and adds the new version.
        file.addVersion(createVersion(1, authorId, content));

        em.persist(file);

        // If files list id is undefined, the uploaded file is the first one
        // of this list, we must create the list value.
        if (filesListId == 0) {

            if (log.isDebugEnabled()) {
                log.debug("[save] First file uploaded, creates the list value.");
            }

            // Finds the max files list value identifier.
            Query query = em.createQuery("SELECT MAX(flv.idList) FROM FilesListValue flv");
            Long currentMaxFilesListValue = (Long) query.getSingleResult();

            // Considers the very first uploaded file (to generate the first
            // id = 0).
            if (currentMaxFilesListValue == null) {
                currentMaxFilesListValue = 0L;
            }

            filesListId = currentMaxFilesListValue + 1;

            // Creates the list value.
            final Value value = new Value();
            value.setLastModificationAction('C');
            value.setLastModificationDate(new Date());
            value.setValue(String.valueOf(filesListId));

            final User user = em.find(User.class, authorId);
            value.setLastModificationUser(user);

            // Element id.
            String elementIdProp = properties.get(FileUploadUtils.DOCUMENT_FLEXIBLE_ELEMENT);
            long elementId;

            try {
                if (elementIdProp == null) {
                    elementId = 0;
                } else {
                    elementId = Integer.valueOf(elementIdProp);
                }
            } catch (NumberFormatException e) {
                elementId = 0;
            }

            final FlexibleElement element = em.find(FlexibleElement.class, elementId);
            value.setElement(element);

            // Project id.
            String projectIdProp = properties.get(FileUploadUtils.DOCUMENT_PROJECT);
            int projectId;

            try {
                if (projectIdProp == null) {
                    projectId = 0;
                } else {
                    projectId = Integer.valueOf(projectIdProp);
                }
            } catch (NumberFormatException e) {
                projectId = 0;
            }

            final Project project = em.find(Project.class, projectId);
            value.setParentProject(project);

            em.persist(value);

            if (log.isDebugEnabled()) {
                log.debug("[save] List value created with id: " + filesListId + ".");
            }
        }

        // Saves the new files list value (file <--> list)
        final FilesListValue value = new FilesListValue();
        value.setIdList(filesListId);
        value.setFile(file);
        value.setId(new FilesListValueId(filesListId, file.getId()));

        em.persist(value);

        return String.valueOf(file.getId());
    }

    /**
     * Saves a new file.
     * 
     * @param properties
     *            The properties map of the uploaded file (see
     *            {@link FileUploadUtils}).
     * @param content
     *            The uploaded file content.
     * @param id
     *            The file which gets a new version.
     * @param authorId
     *            The author id.
     * @return The file id (must be the same as the parameter).
     * @throws IOException
     */
    @Transactional
    protected String saveNewVersion(Map<String, String> properties, byte[] content, String id, int authorId)
            throws IOException {

        final EntityManager em = injector.getInstance(EntityManager.class);

        if (log.isDebugEnabled()) {
            log.debug("[save] New file version.");
        }

        // Manages version number.
        String versionNumberProp = properties.get(FileUploadUtils.DOCUMENT_VERSION);
        int versionNumber;

        try {

            if (versionNumberProp == null) {
                versionNumber = 0;
            } else {
                versionNumber = Integer.valueOf(versionNumberProp);
            }
        } catch (NumberFormatException e) {
            versionNumber = 0;
        }

        // Creates and adds the new version.
        final File file = em.find(File.class, Long.valueOf(id));

        if (log.isDebugEnabled()) {
            log.debug("[save] Found file: " + file.getName() + ".");
        }

        file.addVersion(createVersion(versionNumber, authorId, content));

        em.persist(file);

        return String.valueOf(file.getId());
    }

    /**
     * Creates a file version with the given number and author.
     * 
     * @param versionNumber
     *            The version number.
     * @param user
     *            The author id.
     * @param content
     *            The version content.
     * @return The version just created.
     * @throws IOException
     */
    private FileVersion createVersion(int versionNumber, int authorId, byte[] content) throws IOException {

        final FileVersion version = new FileVersion();

        // Sets attributes.
        version.setVersionNumber(versionNumber);
        version.setAddedDate(new Date());
        version.setSize(Long.valueOf(content.length));
        final User user = new User();
        user.setId(authorId);
        version.setAuthor(user);

        // Saves content.
        version.setPath(writeContent(content));

        return version;
    }

    /**
     * Writes the file content.
     * 
     * @param content
     *            The content as bytes array.
     * @return The unique string identifier to identify the just saved file.
     * @throws IOException
     */
    private String writeContent(byte[] content) throws IOException {

        BufferedOutputStream output = null;
        BufferedInputStream input = null;

        try {

            // Generates the content file name
            final String uniqueName = generateUniqueName();

            // Files repository.
            final java.io.File repository = new java.io.File(UPLOADED_FILES_REPOSITORY);

            // Streams.
            output = new BufferedOutputStream(new FileOutputStream(new java.io.File(repository, uniqueName)));
            input = new BufferedInputStream(new ByteArrayInputStream(content));

            // Writes content as bytes.
            final byte[] buffer = new byte[64 * 1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }

            return uniqueName;
        } finally {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        }
    }

    /**
     * Computes and returns a unique string identifier to name files
     * 
     * @return A unique string identifier.
     */
    private static String generateUniqueName() {
        return UUID.randomUUID().toString();
    }

    @Override
    public DonwloadableFile getFile(String idString, String versionString) {

        final EntityManager em = injector.getInstance(EntityManager.class);

        // Gets file id.
        long id;

        try {
            if (idString == null) {
                id = 0;
            } else {
                id = Integer.valueOf(idString);
            }
        } catch (NumberFormatException e) {
            id = 0;
        }

        // Gets the file entity.
        final File file = em.find(File.class, id);

        // Unable to find the file entity -> download error.
        if (file == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("[getFile] Found file with id=" + file.getId());
        }

        FileVersion lastVersion = null;

        // Downloads the last version.
        if (versionString == null || "".equals(versionString)) {

            if (log.isDebugEnabled()) {
                log.debug("[getFile] Searches last version.");
            }

            final List<FileVersion> versions = file.getVersions();

            if (versions == null || versions.isEmpty()) {
                lastVersion = null;
            } else {

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
        }
        // Downloads a specific version.
        else {

            // Get desired file version.
            int version;

            try {
                version = Integer.valueOf(versionString);
            } catch (NumberFormatException e) {
                version = 0;
            }

            if (log.isDebugEnabled()) {
                log.debug("[getFile] Searches specific version with number=" + version + ".");
            }

            // Searches this specific version.
            final List<FileVersion> versions = file.getVersions();

            if (versions == null || versions.isEmpty()) {
                lastVersion = null;
            }

            for (final FileVersion v : versions) {
                if (v.getVersionNumber() == version) {
                    lastVersion = v;
                    break;
                }
            }
        }

        // Unable to find the desired version -> download error.
        if (lastVersion == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("[getFile] Found version with number=" + lastVersion.getVersionNumber() + ".");
        }

        // Files repository.
        final java.io.File repository = new java.io.File(UPLOADED_FILES_REPOSITORY);

        // Physical file for the desired version.
        final java.io.File physicalFile = new java.io.File(repository, lastVersion.getPath());

        return new DonwloadableFile(file.getName(), physicalFile);
    }
}
