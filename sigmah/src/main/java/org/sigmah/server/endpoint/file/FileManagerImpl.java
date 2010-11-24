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
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.command.result.ValueResultUtils;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.element.FlexibleElement;
import org.sigmah.shared.domain.value.File;
import org.sigmah.shared.domain.value.FileVersion;
import org.sigmah.shared.domain.value.Value;
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
     * Directory's path name where the images are stored.
     */
    private final String IMAGES_REPOSITORY;

    /**
     * The property's name expected in the properties file to set the uploaded
     * files repository path name.
     */
    private static final String UPLOADED_FILES_REPOSITORY_NAME = "repository.files";

    /**
     * The property's name expected in the properties file to set the images
     * repository path name.
     */
    private static final String IMAGES_REPOSITORY_NAME = "repository.images";

    @Inject
    public FileManagerImpl(Properties configProperties, Injector injector) {

        // Initializes uploaded files repository path.
        UPLOADED_FILES_REPOSITORY = configProperties.getProperty(UPLOADED_FILES_REPOSITORY_NAME);

        if (UPLOADED_FILES_REPOSITORY == null) {
            throw new IllegalStateException("Missing reqquired property '" + UPLOADED_FILES_REPOSITORY_NAME
                    + "' in the upload properties file.");
        }

        // Initializes images repository path.
        IMAGES_REPOSITORY = configProperties.getProperty(IMAGES_REPOSITORY_NAME);

        if (IMAGES_REPOSITORY == null) {
            throw new IllegalStateException("Missing reqquired property '" + IMAGES_REPOSITORY_NAME
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
            log.debug("[saveNewFile] New file.");
        }

        // --------------------------------------------------------------------
        // STEP 1 : saves the file.
        // --------------------------------------------------------------------

        if (log.isDebugEnabled()) {
            log.debug("[saveNewFile] Saves the new file.");
        }

        final File file = new File();

        // Gets the details of the name of the file.
        final String fullName = properties.get(FileUploadUtils.DOCUMENT_NAME);
        final String name = getFileCanonicalName(fullName);
        final String extension = getFileExtension(fullName);

        file.setName(name);

        // Creates and adds the new version.
        file.addVersion(createVersion(1, name, extension, authorId, content));

        em.persist(file);

        // --------------------------------------------------------------------
        // STEP 2 : gets the current value for this list of files.
        // --------------------------------------------------------------------

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

        // Retrieving the current value
        final Query query = em
                .createQuery("SELECT v FROM Value v WHERE v.containerId = :projectId and v.element.id = :elementId");
        query.setParameter("projectId", projectId);
        query.setParameter("elementId", elementId);

        final Value currentValue;
        Object object = null;

        try {
            object = query.getSingleResult();
        } catch (NoResultException nre) {
            // No current value
        }

        // --------------------------------------------------------------------
        // STEP 3 : creates or updates the value with the new file id.
        // --------------------------------------------------------------------

        // The value already exists, must update it.
        if (object != null) {
            currentValue = (Value) object;
            currentValue.setLastModificationAction('U');

            // Sets the value (adds a new file id).
            currentValue.setValue(currentValue.getValue() + ValueResultUtils.DEFAULT_VALUE_SEPARATOR
                    + String.valueOf(file.getId()));
        }
        // The value for this list of files doesn't exist already, must
        // create it.
        else {
            currentValue = new Value();

            // Creation of the value
            currentValue.setLastModificationAction('C');

            // Parent element
            final FlexibleElement element = em.find(FlexibleElement.class, elementId);
            currentValue.setElement(element);

            // Container
            currentValue.setContainerId(projectId);

            // Sets the value (one file id).
            currentValue.setValue(String.valueOf(file.getId()));
        }

        // Modifier
        final User user = em.find(User.class, authorId);
        currentValue.setLastModificationUser(user);

        // Last update date
        currentValue.setLastModificationDate(new Date());

        // Saves or updates the new value.
        em.merge(currentValue);

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

        // Gets the details of the name of the file.
        final String fullName = properties.get(FileUploadUtils.DOCUMENT_NAME);
        final String name = getFileCanonicalName(fullName);
        final String extension = getFileExtension(fullName);

        // Creates and adds the new version.
        final File file = em.find(File.class, Integer.valueOf(id));

        if (log.isDebugEnabled()) {
            log.debug("[save] Found file: " + file.getName() + ".");
        }

        final FileVersion version = createVersion(versionNumber, name, extension, authorId, content);
        version.setComments(properties.get(FileUploadUtils.DOCUMENT_COMMENTS));
        file.addVersion(version);

        em.persist(file);

        return String.valueOf(file.getId());
    }

    /**
     * Creates a file version with the given number and author.
     * 
     * @param versionNumber
     *            The version number.
     * @param name
     *            The version name.
     * @param extension
     *            The version extension.
     * @param authorId
     *            The author id.
     * @param content
     *            The version content.
     * @return The version just created.
     * @throws IOException
     */
    private FileVersion createVersion(int versionNumber, String name, String extension, int authorId, byte[] content)
            throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("[createVersion] Creates a new file version # + " + versionNumber + ".");
        }

        final FileVersion version = new FileVersion();

        // Sets attributes.
        version.setVersionNumber(versionNumber);
        version.setName(name);
        version.setExtension(extension);
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

            // Content file.
            final java.io.File contentFile = new java.io.File(repository, uniqueName);

            if (log.isDebugEnabled()) {
                log.debug("[writeContent] Writes file content to the files repository '" + contentFile + "'.");
            }

            // Streams.
            output = new BufferedOutputStream(new FileOutputStream(contentFile));
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
     * Computes and returns a unique string identifier to name files.
     * 
     * @return A unique string identifier.
     */
    private static String generateUniqueName() {
        // Adds the timestamp to ensure the id uniqueness.
        return UUID.randomUUID().toString() + new Date().getTime();
    }

    @Override
    public DonwloadableFile getFile(String idString, String versionString) {

        final EntityManager em = injector.getInstance(EntityManager.class);

        // Gets file id.
        int id;

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

        return new DonwloadableFile(lastVersion.getName() + '.' + lastVersion.getExtension(), physicalFile);
    }

    @Override
    public java.io.File getImage(String name) {

        // Images repository.
        final java.io.File repository = new java.io.File(IMAGES_REPOSITORY);

        // Image file.
        final java.io.File imageFile = new java.io.File(repository, name);

        return imageFile;
    }

    /**
     * Returns the canonical name of a file. <br/>
     * For example:
     * <ul>
     * <li>myfile.txt -> myfile</li>
     * <li>myfile.txt.other -> myfile.txt</li>
     * <li>myfile. -> myfile</li>
     * <li>myfile -> myfile</li>
     * </ul>
     * 
     * @param fullName
     *            The file name.
     * @return The file canonical name.
     */
    private static String getFileCanonicalName(String fullName) {

        // Invalid name.
        if (fullName == null) {
            return null;
        }

        // Searches for the last '.' (before the extension).
        final int index = fullName.lastIndexOf('.');

        // If the file doesn't have an extension, the name is the entire string.
        if (index == -1) {
            return fullName;
        }

        return fullName.substring(0, index);
    }

    /**
     * Returns the extension of a file. <br/>
     * For example:
     * <ul>
     * <li>myfile.txt -> txt</li>
     * <li>myfile.txt.other -> other</li>
     * <li>myfile. -> <code>null</code></li>
     * <li>myfile -> <code>null</code></li>
     * </ul>
     * 
     * @param fullName
     *            The file name.
     * @return The file extension.
     */
    private static String getFileExtension(String fullName) {

        // Invalid name.
        if (fullName == null) {
            return null;
        }

        // Searches for the last '.' (before the extension).
        final int index = fullName.lastIndexOf('.');

        // The file doesn't have an extension.
        if (index == -1 || index + 1 == fullName.length()) {
            return null;
        }

        return fullName.substring(index + 1);
    }
}
