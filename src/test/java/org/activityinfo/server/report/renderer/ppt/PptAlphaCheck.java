

package org.activityinfo.server.report.renderer.ppt;

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
import java.io.InputStream;

import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.junit.Ignore;
import org.junit.Test;

public class PptAlphaCheck {


    @Test
    @Ignore("learning test only")
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
