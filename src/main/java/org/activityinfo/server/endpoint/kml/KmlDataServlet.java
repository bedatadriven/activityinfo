

package org.activityinfo.server.endpoint.kml;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.activityinfo.server.authentication.BasicAuthentication;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.DomainFilters;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.html.HtmlWriter;
import org.activityinfo.server.util.xml.XmlBuilder;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DimensionType;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

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

    private final DispatcherSync dispatcher;
    private final BasicAuthentication authenticator;
    
    private final Provider<EntityManager> entityManager;
    @Inject
    public KmlDataServlet(
    		Provider<EntityManager> entityManager,
    		BasicAuthentication authenticator,
			DispatcherSync dispatcher){
    	
    	this.entityManager = entityManager;
    	this.authenticator = authenticator;
		this.dispatcher = dispatcher;
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        int activityId =Integer.valueOf(req.getParameter("activityId"));
        
        // Get Authorization header
        String auth = req.getHeader("Authorization");

        // Do we allow that user?
        User user = authenticator.doAuthentication(auth);
        
        if (user == null) {
            // Not allowed, or no password provided so report unauthorized
            res.setHeader("WWW-Authenticate", "BASIC realm=\"Utilisateurs authorises\"");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        
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

    

    protected void writeDocument(User user, PrintWriter out, int actvityId) throws TransformerConfigurationException, SAXException, CommandException {

        // TODO: rewrite using FreeMarker

        DomainFilters.applyUserFilter(user, entityManager.get());

        XmlBuilder xml = new XmlBuilder(new StreamResult(out));

        SchemaDTO schema = dispatcher.execute(new GetSchema());
        		
        List<SiteDTO> sites = querySites(user, schema, actvityId);

        xml.startDocument();

        KMLNamespace kml = new KMLNamespace(xml);

        kml.startKml();

        ActivityDTO activity = schema.getActivityById(actvityId); 
        kml.startDocument();
        
        kml.startStyle().at("id", "noDirectionsStyle");
        kml.startBalloonStyle();
        kml.text("$[description]");
        xml.close();
        xml.close();
        
        for (SiteDTO pm : sites) {

            if (pm.hasLatLong()) {

                kml.startPlaceMark();
                kml.styleUrl("#noDirectionsStyle");
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