package org.sigmah.shared.dto.value;

/**
 * Constants to manage files upload.
 * 
 * @author tmi
 * 
 */
public final class FileUploadUtils {

    /**
     * Provides only static methods.
     */
    protected FileUploadUtils() {
    }

    /**
     * Max size for each uploaded file (20 Mb).
     */
    public static final int MAX_UPLOAD_FILE_SIZE = 20971520;

    /**
     * HTTP response start tag.
     */
    public static final String TAG_START = "START:";

    /**
     * HTTP response separator.
     */
    public static final String TAG_SEPARATOR = "/";

    /**
     * HTTP response end tag.
     */
    public static final String TAG_END = "?END";

    /**
     * Error code: empty file.
     */
    public static final String EMPTY_DOC_ERROR_CODE = "-1";

    /**
     * Error code: file too big.
     */
    public static final String TOO_BIG_DOC_ERROR_CODE = "-2";

    /**
     * If empty file can be uploaded.
     */
    public static final String CHECK_EMPTY = "empty";

    /**
     * Uploaded file id.
     */
    public static final String DOCUMENT_ID = "id";

    /**
     * Uploaded file content.
     */
    public static final String DOCUMENT_CONTENT = "file";

    /**
     * If the notification mechanism is active.
     */
    public static final String NOTIFY = "notify";

    /**
     * Uploaded file name.
     */
    public static final String DOCUMENT_NAME = "name";

    /**
     * Version d'un document.
     */
    public static final String DOCUMENT_VERSION = "version";

    /**
     * Uploaded file author.
     */
    public static final String DOCUMENT_AUTHOR = "author";

    /**
     * Flexible element id to which the uploaded file belongs.
     */
    public static final String DOCUMENT_FLEXIBLE_ELEMENT = "element";

    /**
     * Project id to which the uploaded file belongs.
     */
    public static final String DOCUMENT_PROJECT = "project";

    /**
     * Comments of a version.
     */
    public static final String DOCUMENT_COMMENTS = "comments";

    /**
     * Parses the HTTP response from the server after a file upload. The
     * response contains the id of the just saved file if the upload worked
     * fine, or an error code if it didn't.
     * 
     * @param results
     *            The HTTP response.
     * @return The id of the just saved file or an error code.
     */
    public static String parseUploadResult(String results) {

        int beginIndex = results.indexOf(TAG_START);
        if (beginIndex != -1) {
            int endIndex = results.lastIndexOf(TAG_END);
            return results.substring(beginIndex + TAG_START.length(), endIndex);
        } else {
            return null;
        }
    }

    /**
     * Returns if a returned code can be considered as an uploading error.
     * 
     * @param code
     *            The returned code.
     * @return If the code can be considered as an error.
     */
    public static boolean isError(String code) {
        if (code == null) {
            return true;
        } else if (TOO_BIG_DOC_ERROR_CODE.equals(code)) {
            return true;
        } else if (EMPTY_DOC_ERROR_CODE.equals(code)) {
            return true;
        } else {
            return false;
        }
    }
}
