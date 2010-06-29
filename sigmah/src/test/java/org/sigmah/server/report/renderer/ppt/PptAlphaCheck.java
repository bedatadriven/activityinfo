package org.sigmah.server.report.renderer.ppt;

import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
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
