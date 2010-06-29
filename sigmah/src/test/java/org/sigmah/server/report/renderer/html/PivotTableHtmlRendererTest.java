/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.html;

import org.junit.Test;
import org.sigmah.server.report.DummyPivotTableData;
import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.shared.report.model.PivotTableElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

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
