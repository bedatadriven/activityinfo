/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.kml;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.sigmah.server.authentication.Authenticator;
import org.sigmah.server.authentication.ServerSideAuthProvider;
import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.database.hibernate.dao.UserDAO;
import org.sigmah.server.database.hibernate.entity.DomainFilters;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.util.html.HtmlWriter;
import org.sigmah.server.util.xml.XmlBuilder;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.DimensionType;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.sigmah.shared.command.Filter;

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

    @Inject
    private DispatcherSync dispatcher;
    
    @Inject 
    ServerSideAuthProvider authProvider;
    
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        int activityId =Integer.valueOf(req.getParameter("activityId"));
        
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

        authProvider.set(new AuthenticatedUser("", user.getId(), user.getEmail()));

        res.setContentType("application/vnd.google-earth.kml+xml");

        try {
            writeDocument(user, res.getWriter(), activityId);

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
        	user = userDAO.findUserByEmail(emailPass[0]);
        } catch (NoResultException e) {
        	return null;
        }
        
        Authenticator checker = injector.getInstance(Authenticator.class);
        if (!checker.check(user, emailPass[1])) {
            return null;
        }
        return user;

    }

    protected void writeDocument(User user, PrintWriter out, int actvityId) throws TransformerConfigurationException, SAXException, CommandException {

        // TODO: rewrite using FreeMarker

        EntityManager em = injector.getInstance(EntityManager.class);
        DomainFilters.applyUserFilter(user, em);

        XmlBuilder xml = new XmlBuilder(new StreamResult(out));

        SchemaDTO schema = dispatcher.execute(new GetSchema());
        		
        List<SiteDTO> sites = querySites(user, schema, actvityId);

        xml.startDocument();

        KMLNamespace kml = new KMLNamespace(xml);

        kml.startKml();

        ActivityDTO activity = schema.getActivityById(actvityId); 
        kml.startDocument();
     //   kml.name(activity.getName());
    //   kml.open(true);

     //   kml.startFolder();
    //    kml.name(activity.getName());
    //    kml.open(false);
        
        for (SiteDTO pm : sites) {

            if (pm.hasLatLong()) {

                kml.startPlaceMark();
                kml.name(pm.getLocationName());

                kml.startSnippet();
                xml.cdata(renderSnippet(activity, pm));
                xml.close(); // Snippet

                kml.startDescription();
                xml.cdata(renderDescription(activity, pm));
                xml.close();  // Description

                kml.startTimeSpan();
                if(pm.getDate1() != null){
                	kml.begin(pm.getDate1().atMidnightInMyTimezone());
                    kml.end(pm.getDate2().atMidnightInMyTimezone());
                    xml.close(); // Timespan
                }
                

                kml.startPoint();
                kml.coordinates(pm.getLongitude(), pm.getLatitude());
                xml.close();  // Point

                xml.close();  // Placemark
            }
        }

   //     xml.close(); //

        xml.close(); // Document
        xml.close(); // kml
        xml.endDocument();


    }

    private String renderSnippet(ActivityDTO activity, SiteDTO pm) {
        return activity.getName() + " à " + pm.getLocationName() + " (" + pm.getPartnerName() + ")";
    }

    private List<SiteDTO> querySites(User user, SchemaDTO schema, int activityId) {

    	Filter  filter = new Filter();
    	filter.addRestriction(DimensionType.Activity, activityId);
    	
    	
//        List<SiteOrder> order = new ArrayList<SiteOrder>();
//        order.add(SiteOrder.ascendingOn(SiteTableColumn.database_name.property()));
//        order.add(SiteOrder.ascendingOn(SiteTableColumn.activity_name.property()));
//        order.add(SiteOrder.ascendingOn(SiteTableColumn.date2.property()));

        return dispatcher.execute(new GetSites(filter)).getData();
    }

    private String renderDescription(ActivityDTO activity, SiteDTO data) {
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