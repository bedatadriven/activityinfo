package org.sigmah.server.endpoint.gwtrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.shared.dto.value.FileUploadUtils;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Servlet responsible for the uploaded files.
 * 
 * @author rca
 * @author tmi
 * 
 */
@Singleton
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = -199302354477098512L;

    private static final Log log = LogFactory.getLog(UploadServlet.class);

    /**
     * Encode a string to UTF-8.
     * 
     * @param string
     *            The string to encode.
     * @return The encoded string.
     */
    private static String encodeString(String string) {

        if (string == null) {
            return new String("".getBytes(), Charset.forName("UTF-8"));
        }

        return new String(string.getBytes(), Charset.forName("UTF-8"));
    }

    private Injector injector;

    @Inject
    public UploadServlet(Injector injector) {
        this.injector = injector;
    }

    /**
     * Reads the uploaded file properties and content from the HTTP request to
     * store it in the server side. <br/>
     * <br/>
     * This method works two different ways : <br/>
     * If the parameter {@link FileUploadUtils#DOCUMENT_ID} is specified, the
     * method adds the uploaded file as a new version of the file with this id.
     * Else the uploaded file is considered as a new file. <br/>
     * 
     * <ol>
     * <li><b>Adds a new file</b>.<br/>
     * The following parameters must be specified in the HTTP request:
     * <ul>
     * <li>{@link FileUploadUtils#DOCUMENT_CONTENT} : (required) the content.</li>
     * <li>{@link FileUploadUtils#DOCUMENT_NAME} : (required) the file name.</li>
     * <li>{@link FileUploadUtils#DOCUMENT_AUTHOR} : (required) the adder.</li>
     * <li>{@link FileUploadUtils#DOCUMENT_FILES_LIST} : (required) the id of
     * the flexible element where the file is displayed.</li>
     * </ul>
     * <br/>
     * <br/>
     * <li><b>Adds e new version to an existing file</b>.<br/>
     * The following parameters must be specified in the HTTP request:
     * <ul>
     * <li>{@link FileUploadUtils#DOCUMENT_ID} : (required) the existing file
     * id.</li>
     * <li>{@link FileUploadUtils#DOCUMENT_CONTENT} : (required) the content.</li>
     * <li>{@link FileUploadUtils#DOCUMENT_AUTHOR} : (required) the adder.</li>
     * <li>{@link FileUploadUtils#DOCUMENT_VERSION} : (required) the version
     * number.</li>
     * </ul>
     * </ol>
     * <br/>
     * <br/>
     * If the upload works fine, the file id is returned in the HTTP response as
     * following
     * <code>{@link FileUploadUtils#TAG_START}id{@link FileUploadUtils#TAG_END}</code>
     * . <br/>
     * Else an error code is returned as following
     * <code>{@link FileUploadUtils#TAG_START}error_code{@link FileUploadUtils#TAG_END}</code>
     * .
     * 
     * @param request
     *            HTTP request.
     * @param response
     *            HTTP response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        if (log.isDebugEnabled()) {
            log.debug("[doPost] Starts upload.");
        }

        try {

            response.setContentType("text/plain");

            // Manages only multipart forms.
            if (ServletFileUpload.isMultipartContent(request)) {

                boolean addIt = true;

                // Checks uploaded file size.
                if (request.getContentLength() > FileUploadUtils.MAX_UPLOAD_FILE_SIZE) {

                    if (log.isDebugEnabled()) {
                        log.debug("[doPost] File too big to be uploaded (size: " + request.getContentLength() + ").");
                    }

                    // HTTP response.
                    final StringBuilder responseBuilder = new StringBuilder();

                    responseBuilder.append(FileUploadUtils.TAG_START);
                    responseBuilder.append(FileUploadUtils.TOO_BIG_DOC_ERROR_CODE);
                    responseBuilder.append(FileUploadUtils.TAG_END);

                    try {

                        response.getWriter().write(responseBuilder.toString());

                        addIt = false;
                    } catch (IOException e) {

                        if (log.isDebugEnabled()) {
                            log.debug("[doPost] HTTP response I/O error.");
                        }
                        throw e;
                    }
                }

                if (addIt) {

                    // Creates a new file upload handler.
                    final ServletFileUpload upload = new ServletFileUpload();

                    // Map to store the fields of the HTTP form (name -> value).
                    final HashMap<String, String> properties = new HashMap<String, String>();

                    // Uploaded file content.
                    byte[] data = null;

                    try {

                        // Reads HTTP request elements.
                        final FileItemIterator iterator = upload.getItemIterator(request);

                        while (iterator.hasNext()) {

                            // Gets the next HTTP request element.
                            final FileItemStream item = iterator.next();

                            // Field name.
                            final String name = item.getFieldName();

                            // Field value.
                            final InputStream stream = item.openStream();

                            if (item.isFormField()) {

                                // If the field belongs to the HTTP form, stores
                                // it in the map.
                                final String value = encodeString(Streams.asString(stream));
                                properties.put(name, value);

                                if (log.isDebugEnabled()) {
                                    log.debug("[doPost] Reads form field data ; name: " + name + "; value: " + value
                                            + ".");
                                }
                            } else {

                                // Else it's the uploaded file content.
                                if (log.isDebugEnabled()) {
                                    log.debug("[doPost] Reads file content from the field ; name: " + name + ".");
                                }

                                // Stream to read the file content.
                                final OutputStream outputStream;
                                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                outputStream = byteArrayOutputStream;

                                // Stores the file content in a bytes array.
                                int c = stream.read();
                                while (c != -1) {
                                    outputStream.write(c);
                                    c = stream.read();
                                }

                                outputStream.close();

                                data = byteArrayOutputStream.toByteArray();
                                stream.close();
                            }
                        }
                    }
                    // HTTP request I/O error.
                    catch (FileUploadException e) {

                        if (log.isDebugEnabled()) {
                            log.debug("[doPost] Error while reading the HTTP request elements.", e);
                        }
                        throw new ServletException("Error while reading the HTTP request elements.", e);
                    }

                    // If the uploaded file content is empty.
                    if (data.length == 0) {

                        if (log.isDebugEnabled()) {
                            log.debug("[doPost] Empty file.");
                        }

                        // If empty files can be uploaded.
                        final String checkEmpty = properties.get(FileUploadUtils.CHECK_EMPTY);

                        // If empty files upload is forbidden, throws an
                        // exception.
                        if (checkEmpty != null && "true".equalsIgnoreCase(checkEmpty)) {

                            // HTTP response.
                            final StringBuilder responseBuilder = new StringBuilder();

                            responseBuilder.append(FileUploadUtils.TAG_START);
                            responseBuilder.append(FileUploadUtils.EMPTY_DOC_ERROR_CODE);
                            responseBuilder.append(FileUploadUtils.TAG_END);

                            try {

                                response.getWriter().write(responseBuilder.toString());
                                addIt = false;
                            } catch (IOException e) {

                                if (log.isDebugEnabled()) {
                                    log.debug("[doPost] HTTP response I/O error.");
                                }
                                throw e;
                            }
                        }

                        // Else, the empty file is uploaded.
                        else {
                            data = new byte[0];
                        }
                    }

                    if (addIt) {

                        // HTTP response.
                        final StringBuilder responseBuilder = new StringBuilder();

                        // Save the uploaded file
                        final String id = injector.getInstance(UploadManager.class).save(properties, data);

                        if (log.isDebugEnabled()) {
                            log.debug("[doPost] File id: " + id + ".");
                        }

                        // Returns the file id.
                        if (id != null) {

                            responseBuilder.append(FileUploadUtils.TAG_START + id + FileUploadUtils.TAG_END);

                            try {
                                response.getWriter().write(responseBuilder.toString());
                            } catch (IOException e) {

                                if (log.isDebugEnabled()) {
                                    log.debug("[doPost] HTTP response I/O error.");
                                }
                                throw e;
                            }

                        } else {

                            if (log.isDebugEnabled()) {
                                log.debug("[doPost] HTTP response I/O error.");
                            }
                            throw new ServletException("Unable to returns the file id.");
                        }
                    }
                }
            }
        }
        // Catches unknown errors.
        catch (Throwable e) {

            if (log.isDebugEnabled()) {
                log.debug("[doPost] Error while uploading a file.", e);
            }
            throw new ServletException("Error while uploading a file.", e);
        }
    }
}
