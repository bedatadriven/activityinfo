/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import com.google.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.sigmah.shared.Cookies;

/**
 * Serve the Sigmah main page (that handles both the login and the application).
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Singleton
public class SigmahHostController extends HttpServlet {
    public static final String ENDPOINT = "Sigmah/";
    public static final String DEFAULT_LOCALE = "fr";

    private String getLocale(Cookie[] cookies) {
        for (final Cookie cookie : cookies) {
            if (Cookies.LOCALE_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return DEFAULT_LOCALE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String localeMetaTag = "<meta name='gwt:property' content='locale="+getLocale(req.getCookies())+"'>";
        
        try {
            final BufferedReader inputStream = new BufferedReader(new FileReader(new File(SigmahHostController.class.getResource("SigmahHostController.html").toURI())));
            final OutputStream outputStream = resp.getOutputStream();

            final Charset utf8 = Charset.forName("UTF-8");
            boolean edited = false;

            String line = inputStream.readLine();
            while(line != null) {
                if(!edited && line.equals("<!--Locale-->")) {
                    line = localeMetaTag;
                    edited = true;
                }

                outputStream.write(line.getBytes(utf8));
                line = inputStream.readLine();
            }

            inputStream.close();
            outputStream.close();
        } catch (URISyntaxException ex) {
            Logger.getLogger(SigmahHostController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
