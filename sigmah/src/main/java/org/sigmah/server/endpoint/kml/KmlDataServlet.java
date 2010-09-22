/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.kml;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.apache.commons.codec.binary.Base64;
import org.sigmah.server.auth.Authenticator;
import org.sigmah.server.dao.SiteTableDAO;
import org.sigmah.server.dao.hibernate.SiteTableDAOHibernate;
import org.sigmah.server.domain.DomainFilters;
import org.sigmah.server.domain.SiteData;
import org.sigmah.server.report.generator.SiteDataBinder;
import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.server.util.KMLNamespace;
import org.sigmah.server.util.XmlBuilder;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.handler.GetSchemaHandler;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dao.SiteOrder;
import org.sigmah.shared.dao.SiteTableColumn;
import org.sigmah.shared.dao.UserDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.exception.CommandException;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Serves a KML (Google Earth) file containing the locations of all activities
 * that are visible to the user.
 * <p/>
 * Users are authenticated using Basic HTTP authentication, and will see a prompt
 * for their username (email) and password when they access from Google Earth.
 *
 * @author Alex Bertram
 */
@Singleton
public class KmlDataServlet extends javax.servlet.http.HttpServlet {

    @Inject
    private Injector injector;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        // Get Authorization header
        String auth = req.getHeader("Authorization");

        // Do we allow that user?

        User user = authenticate(auth);
        if (user == null) {
            // Not allowed, or no password provided so report unauthorized
            res.setHeader("WWW-Authenticate", "BASIC realm=\"Utilisateurs authorises\"");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        res.setContentType("application/vnd.google-earth.kml+xml");

        try {
            writeDocument(user, res.getWriter());

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (CommandException e) {
            e.printStackTrace();
        }

    }

    // This method checks the user information sent in the Authorization
    // header against the database of users maintained in the users Hashtable.

    protected User authenticate(String auth) throws IOException {
        if (auth == null) {
            return null;
        }// no auth

        if (!auth.toUpperCase().startsWith("BASIC ")) {
            return null;
        }// we only do BASIC

        // Get encoded user and password, comes after "BASIC "
        String emailpassEncoded = auth.substring(6);

        // Decode it, using any base 64 decoder

        byte[] emailpassDecodedBytes = Base64.decodeBase64(emailpassEncoded.getBytes());
        String emailpassDecoded = new String(emailpassDecodedBytes, Charset.defaultCharset());
        String[] emailPass = emailpassDecoded.split(":");

        if (emailPass.length != 2) {
            return null;
        }

        // look up the user in the database
        UserDAO userDAO = injector.getInstance(UserDAO.class);
        User user = null; 
        try {
        	userDAO.findUserByEmail(emailPass[0]);
        } catch (NoResultException e) {
        	return null;
        }
        
        Authenticator checker = injector.getInstance(Authenticator.class);
        if (!checker.check(user, emailPass[1])) {
            return null;
        }
        return user;

    }

    protected void writeDocument(User user, PrintWriter out) throws TransformerConfigurationException, SAXException, CommandException {

        // TODO: rewrite using FreeMarker

        EntityManager em = injector.getInstance(EntityManager.class);
        DomainFilters.applyUserFilter(user, em);

        XmlBuilder xml = new XmlBuilder(new StreamResult(out));

        GetSchemaHandler schemaHandler = injector.getInstance(GetSchemaHandler.class);
        SchemaDTO schema = (SchemaDTO) schemaHandler.execute(new GetSchema(), user);

        List<SiteData> sites = querySites(user, schema);

        xml.startDocument();

        KMLNamespace kml = new KMLNamespace(xml);

        kml.startKml();

        kml.startDocument();
        kml.name("ActivityInfo");
        kml.open(true);

        int lastDatabaseId = -1;
        int lastActivityId = -1;
        ActivityDTO activity = null;

        for (SiteData pm : sites) {

            if (pm.hasLatLong()) {

                if (pm.getActivityId() != lastActivityId && lastActivityId != -1) {
                    xml.close();
                }

                if (pm.getDatabaseId() != lastDatabaseId) {
                    if (lastDatabaseId != -1) {
                        xml.close();
                    }
                    kml.startFolder();
                    kml.name(schema.getDatabaseById(pm.getDatabaseId()).getName());
                    kml.open(true);
                    lastDatabaseId = pm.getDatabaseId();
                }

                if (pm.getActivityId() != lastActivityId) {
                    kml.startFolder();
                    kml.name(schema.getActivityById(pm.getActivityId()).getName());
                    kml.open(false);

                    activity = schema.getActivityById(pm.getActivityId());
                    lastActivityId = activity.getId();
                }

                kml.startPlaceMark();
                kml.name(pm.getLocationName());

                kml.startSnippet();
                xml.cdata(renderSnippet(activity, pm));
                xml.close(); // Snippet

                kml.startDescription();
                xml.cdata(renderDescription(activity, pm));
                xml.close();  // Description

                kml.startTimeSpan();
                kml.begin(pm.getDate1());
                kml.end(pm.getDate2());
                xml.close(); // Timespan

                kml.startPoint();
                kml.coordinates(pm.getLongitude(), pm.getLatitude());
                xml.close();  // Point

                xml.close();  // Placemark
            }
        }

        if (lastActivityId != -1) {
            xml.close();
        }
        if (lastDatabaseId != -1) {
            xml.close();
        }

        xml.close(); // Document
        xml.close(); // kml
        xml.endDocument();


    }

    private String renderSnippet(ActivityDTO activity, SiteData pm) {
        return activity.getName() + " à " + pm.getLocationName() + " (" + pm.getPartnerName() + ")";
    }

    private List<SiteData> querySites(User user, SchemaDTO schema) {

        List<SiteOrder> order = new ArrayList<SiteOrder>();
        order.add(SiteOrder.ascendingOn(SiteTableColumn.database_name.property()));
        order.add(SiteOrder.ascendingOn(SiteTableColumn.activity_name.property()));
        order.add(SiteOrder.ascendingOn(SiteTableColumn.date2.property()));

        SiteTableDAO siteDAO = injector.getInstance(SiteTableDAOHibernate.class);
        return siteDAO.query(user, new Filter(), order, new SiteDataBinder(), SiteTableDAO.RETRIEVE_ALL, 0, -1);

    }

    private String renderDescription(ActivityDTO activity, SiteData data) {
        HtmlWriter html = new HtmlWriter();

        html.startTable();

        for (IndicatorDTO indicator : activity.getIndicators()) {
            if (data.getIndicatorValue(indicator.getId()) != null) {
                html.startTableRow();

                html.tableCell(indicator.getName());
                html.tableCell(Double.toString(data.getIndicatorValue(indicator.getId())));
                html.tableCell(indicator.getUnits());

                html.endTableRow();
            }
        }

        html.endTable();
        return html.toString();
    }

}