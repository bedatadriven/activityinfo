package org.sigmah.server.endpoint.gwtrpc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.google.inject.Singleton;

@Singleton
public class UploadManager {

    private static final Log log = LogFactory.getLog(UploadManager.class);

    public static final String UPLOADED_FILES_DIR;

    static {

        if (log.isDebugEnabled()) {
            log.debug("[init] Loads properties.");
        }

        // Loads properties.
        final InputStream input = UploadManager.class.getClassLoader().getResourceAsStream("upload.properties");
        final Properties props = new Properties();

        try {
            props.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Upload properties file cannot be read.", e);
        }

        // Uploaded file directory.
        String property = props.getProperty("rootDir");
        if (property == null) {
            throw new IllegalStateException("Missing reqquired property rootDir in the upload properties file.");
        }

        UPLOADED_FILES_DIR = property;

        if (log.isDebugEnabled()) {
            log.debug("[init] Read property rootDir=" + UPLOADED_FILES_DIR + ".");
        }
    }

    private Injector injector;

    @Inject
    public UploadManager(Injector injector) {
        this.injector = injector;
    }

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
    public String save(Map<String, String> properties, byte[] content) throws IOException {

        // Uploaded file's id.
        String id = properties.get(FileUploadUtils.DOCUMENT_ID);

        final EntityManager em = injector.getInstance(EntityManager.class);
        final EntityTransaction t = em.getTransaction();
        t.begin();

        final File file;

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

            file = new File();
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
                final Long currentMaxFilesListValue = (Long) query.getSingleResult();

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

            id = String.valueOf(file.getId());
        }
        // New version.
        else {

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
            file = em.find(File.class, Long.valueOf(id));

            if (log.isDebugEnabled()) {
                log.debug("[save] Found file: " + file.getName() + ".");
            }

            file.addVersion(createVersion(versionNumber, authorId, content));

            em.persist(file);
        }

        t.commit();

        return id;
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
    private static FileVersion createVersion(int versionNumber, int authorId, byte[] content) throws IOException {

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
    private static String writeContent(byte[] content) throws IOException {

        BufferedOutputStream output = null;
        BufferedInputStream input = null;

        try {

            // Generates the content file name
            final String uniqueName = generateUniqueName();

            // Files repository.
            final java.io.File repository = new java.io.File(UPLOADED_FILES_DIR);

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
}
