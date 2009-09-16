package org.activityinfo.server.report.renderer.ppt;

import org.junit.Test;
import org.activityinfo.server.report.ParseTest;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.ddf.EscherProperties;

import java.io.InputStream;
import java.io.IOException;
/*
 * @author Alex Bertram
 */

public class PptAlphaCheck {


    @Test
    public void testAlphaValues() throws IOException {


        InputStream in = PptAlphaCheck.class.getResourceAsStream("/alpha.ppt");
        SlideShow ppt = new SlideShow( new HSLFSlideShow(in) );

        Slide[] slides = ppt.getSlides();
        Shape[] shapes = slides[0].getShapes();

        for(int i=0; i!=shapes.length; ++i) {
            System.out.println(String.format("slide %d, opacity = %d", i,
                    shapes[i].getEscherProperty(EscherProperties.FILL__FILLOPACITY)));
           
            
        }

    }
}
