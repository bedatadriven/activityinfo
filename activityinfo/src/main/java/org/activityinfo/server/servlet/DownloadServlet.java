package org.activityinfo.server.servlet;

import com.google.inject.Singleton;
import com.google.inject.Inject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * @author Alex Bertram
 */

@Singleton
public class DownloadServlet extends HttpServlet {

    private ServletContext context;
    private SimpleDateFormat dateFormat;

    private static int BUFFER_SIZE = 4096;

    @Inject
    public DownloadServlet(ServletContext context) {
        this.context = context;
        dateFormat = (SimpleDateFormat) DateFormat.getDateInstance();
        dateFormat.applyPattern("yyyy_MM_dd");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String tempFile = request.getQueryString();
        String tempPath = context.getRealPath("/temp/" + tempFile);

        String ext = tempFile.substring(tempFile.lastIndexOf("."));

        String friendlyName = "ActivityInfo" + dateFormat.format(new Date()) + ext;

        FileInputStream in = new FileInputStream(tempPath);

        response.setContentType(context.getMimeType(tempPath));
        response.addHeader("Content-Disposition", "attachment; filename=" + friendlyName);

        OutputStream out = response.getOutputStream();

        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];
        do {
            bytesRead = in.read(buffer);
            if(bytesRead > 0)
                out.write(buffer, 0, bytesRead);
        } while(bytesRead > 0);
        
    }
}
