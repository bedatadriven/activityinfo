package org.activityinfo.server.servlet.kml;

import com.google.inject.Singleton;
import org.activityinfo.server.util.KMLNamespace;
import org.activityinfo.server.util.XmlBuilder;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

/**
 * Serves a simple KML file containing a network link to {@link org.activityinfo.server.servlet.kml.KmlDataServlet}.
 *
 * This file will be downloaded to the users computer and can be saved locally, but will asssure
 * that all actual data comes live from the server.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
@Singleton
public class KmlLinkServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO: rewrite using FreeMarker

        resp.setContentType("application/vnd.google-earth.kml+xml");

        try {
            XmlBuilder xml = new XmlBuilder(new StreamResult(resp.getOutputStream()));

            xml.startDocument();

            String dataUrl = "http://" + req.getServerName() + ":" +
                    req.getServerPort() + "/" + req.getRequestURI() + "/data";

            KMLNamespace kml = new KMLNamespace(xml);

            kml.startKml();
            kml.startFolder();
            kml.name("ActivityInfo");
            kml.open(true);
            kml.startNetworkLink();
            kml.refreshVisibility(false);
            kml.flyToView(true);
            kml.startLink();
            kml.href( dataUrl );
            xml.close(); // Link
            xml.close(); // NetworkLink
            xml.close(); // Folder
            xml.close(); // KML

            xml.endDocument();

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

    }
}