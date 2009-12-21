package org.activityinfo.server.endpoint.wfs;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.activityinfo.server.domain.Activity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 *
 * An implementation of the Web Feature Service (WFS) Specification that allows
 * access to ActivityInfo directly from ArcGIS and other GIS software.
 *
 * This implementation relies heavily on FreeMarker, see /war/ftl/wfs for the
 * response templates.
 *
 * @author Alex Bertram
 */
@Singleton
public class WfsServlet extends HttpServlet {

    private final Injector injector;
    private final Configuration templateCfg;

    @Inject
    public WfsServlet(Injector injector, Configuration templateCfg) {
        this.injector = injector;
        this.templateCfg = templateCfg;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        this.log("WFS Get request: " + req.getQueryString());

        try {

            String requestType = req.getParameter("REQUEST");

            if(req.getQueryString() == null || "GetCapabilities".equalsIgnoreCase(requestType)) {

                writeCapabilities(req, response);


            } else if("DescribeFeatureType".equalsIgnoreCase(requestType) ) {

                List<String> featureTypes = new ArrayList<String>();

                for(String typeName : req.getParameterValues("typeName")) {
                    featureTypes.add(typeName);
                }

                writeFeatureDescription(featureTypes, response);

            } else if("GetFeature".equalsIgnoreCase(requestType)) {

                String outputFormat = req.getParameter("OUTPUTFORMAT");
                String resultType = req.getParameter("RESULTTYPE");
                String maxFeatures = req.getParameter("MAXFEATURES");

                String propertyNameList = req.getParameter("PROPERTYNAME");


                String typeName = req.getParameter("TYPENAME");
                List<String> types = new ArrayList<String>();
                if(typeName != null) {
                    types.addAll(Arrays.asList(typeName.split(",")));
                }
                


            }
        } catch(Exception e) {
            writeException(e, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");

        try {

            if("text/xml".equals(req.getContentType())) {

                Document document;
                DocumentBuilderFactory factory =
                        DocumentBuilderFactory.newInstance();
                try
                {

                    DocumentBuilder builder = factory.newDocumentBuilder();
                    document = builder.parse(req.getInputStream());

                }catch (SAXException sxe)
                {
                    Exception e = sxe;
                    if (sxe.getException() != null)
                        e = sxe.getException();
                    e.printStackTrace();
                    return;
                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }

                String requestType = document.getDocumentElement().getNodeName();

                if(requestType.equals("GetCapabilities")) {

                    writeCapabilities(req, resp);


                } else if(requestType.equals("DescribeFeatureType")) {
                    List<String> featureTypes = new ArrayList<String>();
                    NodeList elements = document.getElementsByTagName("TypeName");

                    for(int i = 0; i!=elements.getLength(); ++i) {
                        featureTypes.add(elements.item(i).getTextContent());
                    }

                    writeFeatureDescription(featureTypes, resp);

                } else if(requestType.equals("GetFeatures")) {

                    getFeatures(document, resp);

                }

            }
        }catch(Exception e) {
            writeException(e, resp);
        }

    }



    protected void writeCapabilities( HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException {

        EntityManager em = injector.getInstance(EntityManager.class);

        Capabilities capabilities = new Capabilities();
        capabilities.setPostUrl("http://" + request.getServerName() + ":" + request.getServerPort() +
                request.getRequestURI());
        capabilities.setGetUrl(capabilities.getPostUrl() + "?");
        capabilities.setActivities(em.createQuery("select a from Activity a").getResultList());

        Template tpl = templateCfg.getTemplate("wfs/Capabilities.ftl");

        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        tpl.process(capabilities, response.getWriter());
    }

    protected void writeFeatureDescription(List<String> featureTypes, HttpServletResponse response) throws IOException, TemplateException {

        EntityManager em = injector.getInstance(EntityManager.class);
        List<Activity> activities = new ArrayList<Activity>();

        for(String name : featureTypes) {
            if(name.startsWith("activity")) {
                int activityId = Integer.parseInt(name.substring("activity".length()));
                activities.add(em.find(Activity.class, activityId ));
            }
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("activities", activities);

        Template tpl = templateCfg.getTemplate("wfs/FeatureType.ftl");

        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        tpl.process(model, response.getWriter());
    }

    private void getFeatures(Document document, HttpServletResponse resp) {

        Element reqElement = document.getDocumentElement();

        boolean countOnly = "hits".equals(reqElement.getAttribute("resultType"));

    }

    private void writeException(Exception e, HttpServletResponse response) {


        response.setContentType("text/plain");
        try {
            e.printStackTrace(response.getWriter());
        } catch (IOException e1) {

        }
    }


}
