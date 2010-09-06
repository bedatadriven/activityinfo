package org.sigmah.server.endpoint.file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.endpoint.file.FileManager.DonwloadableFile;
import org.sigmah.shared.dto.value.FileUploadUtils;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Servlet responsible for the downloaded files.
 * 
 * @author tmi
 * 
 */
@Singleton
public class FileDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 2157619582348655060L;

    private static final Log log = LogFactory.getLog(FileDownloadServlet.class);

    /**
     * To get the download manager.
     */
    private final Injector injector;
    
    @Inject
    public FileDownloadServlet(Injector injector) {
        this.injector = injector;
    }
    
    /**
     * Download a version of a file.
     * 
     * The following parameters must be specified in the HTTP request:
     * <ul>
     * <li>{@link FileUploadUtils#DOCUMENT_ID} : (required) the id of the file.</li>
     * <li>{@link FileUploadUtils#DOCUMENT_VERSION} : (optional) the version
     * number of the file. If not specified, the last version is downloaded.</li>
     * </ul>
     * 
     * @param request
     *            HTTP request.
     * @param response
     *            HTTP response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (log.isDebugEnabled()) {
            log.debug("[doGet] Starts download.");
        }

        try {

            // Gets the request parameters.
            final String id = request.getParameter(FileUploadUtils.DOCUMENT_ID);
            final String version = request.getParameter(FileUploadUtils.DOCUMENT_VERSION);

            // Checks the parameters completion.
            if (id == null || "".equals(id)) {
                log.error("[doGet] Missing required parameter id.");
                throw new ServletException("Missing required parameter id.");
            }

            if (log.isDebugEnabled()) {
                log.debug("[doGet] Download file: id=" + id + " ; version= " + version);
            }

            // Gets the physical file for the given id/version.
            final DonwloadableFile file = injector.getInstance(FileManager.class).getFile(id, version);

            // Download error.
            if (file == null) {
                log.error("[doGet] File or version cannot be found.");
                throw new ServletException("File or version cannot be found.");
            }

            // Prepares HTTP response.
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            final BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file.getPhysicalFile()));

            try {

                // Writes file content to the HTTP response.
                final ServletOutputStream outputStream = response.getOutputStream();

                int b = inputStream.read();
                while (b != -1) {
                    outputStream.write(b);
                    b = inputStream.read();
                }

                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                log.error("[doGet] HTTP response I/O error.");
                throw e;
            }
        }
        // Catches unknown errors.
        catch (Throwable e) {
            log.error("[doGet] Error while donwloading a file.", e);
            throw new ServletException("Error while donwloading a file.", e);
        }
    }
}
