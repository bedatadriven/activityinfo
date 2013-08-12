package org.activityinfo.server.endpoint.odk;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({
    TestDatabaseModule.class,
    MockHibernateModule.class,
})
public class FormParserTest {

    @Inject
    private FormParser parser;

    @Test
    public void testParseSiteForm() throws Exception {
        URL url = this.getClass().getResource("/org/activityinfo/server/endpoint/odk/form-instance.xml");
        // if it turns out the complete xform is send on submit, use this url
        // URL url = this.getClass().getResource("/org/activityinfo/server/endpoint/odk/form.xml");
        String xml = Files.toString(new File(url.getPath()), Charsets.UTF_8);
        SiteFormData data = parser.parse(xml);
        assertEquals("uuid:23b56e39-ef50-4510-b85f-c454cd5465c1", data.getInstanceID());
        assertEquals(927, data.getActivity());
        assertEquals(274, data.getPartner());
        assertEquals("Some location", data.getLocationname());
        // assertEquals("", data.getGps());
        assertEquals("2012-07-05", new SimpleDateFormat("yyyy-MM-dd").format(data.getDate1()));
        assertEquals("2013-08-07", new SimpleDateFormat("yyyy-MM-dd").format(data.getDate2()));
        assertEquals("Some comment", data.getComments());

        assertEquals(2, data.getIndicators().size());
        assertEquals(4410, data.getIndicators().get(0).getId());
        assertEquals("1.1", data.getIndicators().get(0).getValue());
        assertEquals((Double) 1.1, data.getIndicators().get(0).getValueAsDouble());
        assertEquals(4411, data.getIndicators().get(1).getId());
        assertEquals("2.5", data.getIndicators().get(1).getValue());
        assertEquals((Double) 2.5, data.getIndicators().get(1).getValueAsDouble());

        assertEquals(1, data.getAttributegroups().size());
        assertEquals(991, data.getAttributegroups().get(0).getId());
        assertEquals("2419 2420", data.getAttributegroups().get(0).getValue());
        assertEquals(2, data.getAttributegroups().get(0).getValues().size());
        assertEquals((Integer) 2419, data.getAttributegroups().get(0).getValues().get(0));
        assertEquals((Integer) 2420, data.getAttributegroups().get(0).getValues().get(1));
    }
}
