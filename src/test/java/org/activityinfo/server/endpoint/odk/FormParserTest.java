package org.activityinfo.server.endpoint.odk;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;

import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({
    TestDatabaseModule.class,
    MockHibernateModule.class
})
public class FormParserTest {

    @Inject
    private FormParser parser;

    // @formatter:off
    private static String xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<data id=\"siteform\">" +
            "<meta>" +
                "<instanceID>uuid:23b56e39-ef50-4510-b85f-c454cd5465c1</instanceID>" +
            "</meta>" +
            "<activity>927</activity>" +
            "<partner>274</partner>" +
            "<locationname>Some location</locationname>" +
            "<gps>52.144802074999994 5.377899974999999</gps>" +
            "<date1>2012-07-05</date1>" +
            "<date2>2013-08-07</date2>" +
            "<I4410>1.1</I4410>" +
            "<I4411>2.5</I4411>" +
            "<AG991>2419 2420</AG991>" +
            "<comments>Some comment</comments>" +
        "</data>";
 // @formatter:on

    @Test
    public void testParseSiteForm() throws Exception {
        // URL url = FormParserTest.class.getResource("/org/activityinfo/server/endpoint/odk/form-instance.xml");
        // String xml = Files.toString(new File(url.getPath()), Charsets.UTF_8);

        SiteFormData data = parser.parse(xml);

        assertEquals("uuid:23b56e39-ef50-4510-b85f-c454cd5465c1", data.getInstanceID());
        assertEquals(927, data.getActivity());
        assertEquals(274, data.getPartner());
        assertEquals("Some location", data.getLocationname());
        // assertEquals("", data.getGps());
        assertEquals("2012-07-05", new SimpleDateFormat("yyyy-MM-dd").format(data.getDate1()));
        assertEquals("2013-08-07", new SimpleDateFormat("yyyy-MM-dd").format(data.getDate2()));
        assertEquals("Some comment", data.getComments());

        assertEquals((Double) 52.144802074999994D, (Double) data.getLatitude());
        assertEquals((Double) 5.377899974999999, (Double) data.getLongitude());

        assertEquals(2, data.getIndicators().size());
        assertEquals(4410, data.getIndicators().get(0).getId());
        assertEquals("1.1", data.getIndicators().get(0).getValue());
        assertEquals((Double) 1.1, data.getIndicators().get(0).getDoubleValue());
        assertEquals(4411, data.getIndicators().get(1).getId());
        assertEquals("2.5", data.getIndicators().get(1).getValue());
        assertEquals((Double) 2.5, data.getIndicators().get(1).getDoubleValue());

        assertEquals(1, data.getAttributegroups().size());
        assertEquals(991, data.getAttributegroups().get(0).getId());
        assertEquals("2419 2420", data.getAttributegroups().get(0).getValue());
        assertEquals(2, data.getAttributegroups().get(0).getValueList().size());
        assertEquals((Integer) 2419, data.getAttributegroups().get(0).getValueList().get(0));
        assertEquals((Integer) 2420, data.getAttributegroups().get(0).getValueList().get(1));
    }
}
