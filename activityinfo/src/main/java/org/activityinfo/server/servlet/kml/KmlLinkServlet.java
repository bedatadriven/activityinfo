package org.activityinfo.server.servlet.kml;

import org.activityinfo.server.util.KMLNamespace;
import org.activityinfo.server.util.XmlBuilder;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;

import com.google.inject.Singleton;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
@Singleton
public class KmlLinkServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/vnd.google-earth.kml+xml");

        //resp.setContentType("text/xml");
        
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