package org.activityinfo.server.report.renderer.html;

import org.activityinfo.server.report.DummyPivotTableData;
import org.activityinfo.server.report.renderer.html.ImageStorageProvider;
import org.activityinfo.server.report.renderer.html.HtmlPivotTableRenderer;
import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.report.model.PivotTableElement;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotTableHtmlRendererTest {

    @Test
    public void test() throws IOException, SAXException, ParserConfigurationException {

        // input
        DummyPivotTableData testData = new DummyPivotTableData();
        PivotTableElement element = testData.Foobar1612Element();

        // collaborator: ImageStorageProvider
        ImageStorageProvider isp = createMock(ImageStorageProvider.class);
        replay(isp); // no calls expected

        // output stream  (we need well-formed html for the test)
        HtmlWriter writer = new HtmlWriter();

        // CLASS under TEST
        HtmlPivotTableRenderer renderer = new HtmlPivotTableRenderer();
        renderer.render(writer, isp, element);

        // VALIDATE
        // TODO
//        Document document;
//		DocumentBuilderFactory factory =
//			        DocumentBuilderFactory.newInstance();
//
//
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        builder.setEntityResolver(new EntityResolver() {
//            @Override
//            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//                return new InputSource(new StringReader(""));
//            }
//        });
//
//        document = builder.parse(new InputSource(new StringReader(writer.toString())));
//
        
    }



}
