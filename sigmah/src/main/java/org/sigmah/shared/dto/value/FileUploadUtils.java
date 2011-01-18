package org.sigmah.shared.dto.value;

import java.util.Date;

import org.sigmah.shared.dto.reminder.MonitoredPointDTO;

import com.allen_sauer.gwt.log.client.Log;

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

    // ! \\
    // A tag code is used to parse the response of the HTTP request.
    //
    // Each code which is printed is this response should not contain entirely
    // another code to avoid collisions in regular expressions.
    // Bad example: code 1 = BAD ; code 2 = ITSBAD
    // ! \\

    /**
     * HTTP response start tag for the response code.
     */
    public static final String TAG_START_CODE = "START:";

    /**
     * HTTP response end tag for the response code.
     */
    public static final String TAG_END_CODE = "?END";

    /**
     * HTTP response start tag for the monitored point.
     */
    public static final String TAG_START_MONITORED_POINT = "MPST:";

    /**
     * HTTP response separator.
     */
    public static final String TAG_SEPARATOR_MONITORED_POINT = "%~%~%~%";

    /**
     * HTTP response end tag for the monitored point.
     */
    public static final String TAG_END_MONITORED_POINT = "?MPEN";

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
     * The url of an image.
     */
    public static final String IMAGE_URL = "url";

    /**
     * The expected date of a monitored point.
     */
    public static final String MONITORED_POINT_DATE = "mpdate";

    /**
     * The label of a monitored point.
     */
    public static final String MONITORED_POINT_LABEL = "mplabel";

    /**
     * Parses the HTTP response from the server after a file upload. The
     * response contains the id of the just saved file if the upload worked
     * fine, or an error code if it didn't.
     * 
     * @param results
     *            The HTTP response.
     * @return The id of the just saved file or an error code.
     */
    public static String parseUploadResultCode(String results) {

        int beginIndex = results.indexOf(TAG_START_CODE);
        if (beginIndex != -1) {
            int endIndex = results.lastIndexOf(TAG_END_CODE);
            return results.substring(beginIndex + TAG_START_CODE.length(), endIndex);
        } else {
            return null;
        }
    }

    /**
     * Parses the HTTP response from the server after a file upload and
     * retrieves the generated monitored point if any.
     * 
     * @param results
     *            The HTTP response.
     * @return The monitored point or <code>null</code>.
     */
    public static MonitoredPointDTO parseUploadResultMonitoredPoint(String results) {

        MonitoredPointDTO point = null;

        int beginIndex = results.indexOf(TAG_START_MONITORED_POINT);
        if (beginIndex != -1) {

            if (Log.isDebugEnabled()) {
                Log.debug("[parseUploadResultMonitoredPoint] Tag '" + TAG_START_MONITORED_POINT + "' found.");
            }

            try {

                int endIndex = results.lastIndexOf(TAG_END_MONITORED_POINT);

                if (endIndex == -1) {
                    if (Log.isDebugEnabled()) {
                        Log.debug("[parseUploadResultMonitoredPoint] Tag '" + TAG_END_MONITORED_POINT + "' not found.");
                    }
                }

                final String content = results.substring(beginIndex + TAG_START_MONITORED_POINT.length(), endIndex);

                if (Log.isDebugEnabled()) {
                    Log.debug("[parseUploadResultMonitoredPoint] Substring '" + content + "'.");
                }

                final String[] tokens = content.split(TAG_SEPARATOR_MONITORED_POINT);

                if (Log.isDebugEnabled()) {
                    Log.debug("[parseUploadResultMonitoredPoint] Splited tokens (" + tokens.length + ") '" + tokens
                            + "'.");
                }

                point = new MonitoredPointDTO();

                if (Log.isDebugEnabled()) {
                    Log.debug("[parseUploadResultMonitoredPoint] Point id -> '" + tokens[0] + "'.");
                }

                point.setId(Integer.valueOf(tokens[0]));

                if (Log.isDebugEnabled()) {
                    Log.debug("[parseUploadResultMonitoredPoint] Point label -> '" + tokens[1] + "'.");
                }

                point.setLabel(tokens[1]);

                if (Log.isDebugEnabled()) {
                    Log.debug("[parseUploadResultMonitoredPoint] Point date -> '" + tokens[2] + "'.");
                }

                point.setExpectedDate(new Date(Long.valueOf(tokens[2])));

                if (Log.isDebugEnabled()) {
                    Log.debug("[parseUploadResultMonitoredPoint] Point label -> '" + point.getId()
                            + "' ; point id -> '" + point.getLabel() + "' ; point date -> '" + point.getExpectedDate()
                            + "'.");
                }
            } catch (Throwable e) {
                // digest exception.
                if (Log.isDebugEnabled()) {
                    Log.debug("[parseUploadResultMonitoredPoint] Error while parsing the http result.", e);
                }
                point = null;
            }
        } else {
            if (Log.isDebugEnabled()) {
                Log.debug("[parseUploadResultMonitoredPoint] Tag '" + TAG_START_MONITORED_POINT + "' not found.");
            }
        }

        return point;
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
