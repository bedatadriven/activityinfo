package org.activityinfo.server.report.renderer.ppt;

import com.google.inject.Inject;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.generator.map.IconRectCalculator;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapElement;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.ShapeTypes;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
/*
 * @author Alex Bertram
 */

public class PPTMapRenderer extends ImageMapRenderer {

    @Inject
    public PPTMapRenderer(@MapIconPath String mapIconPath) {
        super(mapIconPath);
    }

    @Override
    public void render(MapElement element, OutputStream stream) throws IOException {


        //create a new empty slide show
        SlideShow ppt = new SlideShow();
        Dimension pageSize = computePageSize(element);
        ppt.setPageSize(pageSize);

        //add first slide
        Slide slide = ppt.createSlide();

        // calculate map offset
        int offsetX = ((int)pageSize.getWidth() - element.getWidth())/2;
        int offsetY = ((int)pageSize.getHeight() - element.getHeight())/2;

        // create the group for the map
//        ShapeGroup group = new ShapeGroup();
//        group.setAnchor(new Rectangle(offsetX, offsetY, element.getWidth(), element.getHeight()));
//        slide.addShape(group);

        // add the map background image
        int basemapPictureIndx = ppt.addPicture(renderBasemap(element), Picture.PNG);
        Picture basemap = new Picture(basemapPictureIndx);
        basemap.setAnchor(new Rectangle(offsetX, offsetY, element.getWidth(), element.getHeight()));
        basemap.setLineColor(Color.BLACK);
        basemap.setLineWidth(1);
        slide.addShape(basemap);

        // keep a list of map icons
        Map<String, Integer> iconPictureIndex = new HashMap<String, Integer>();

        // Add the indicator markers to the slide as shapes
        for(MapMarker marker : element.getContent().getMarkers()) {

            if(marker.getIcon() != null) {

                Integer iconIndex = iconPictureIndex.get(marker.getIcon().getName());
                if(iconIndex == null) {
                    try {
                        iconIndex = ppt.addPicture(
                                new File(mapIconRoot + "/" + marker.getIcon().getName() + ".png"),
                                Picture.PNG);
                    } catch (IOException e) {
                        iconIndex = -1;
                    }
                    iconPictureIndex.put(marker.getIcon().getName(), iconIndex);
                }
                if(iconIndex != -1) {
                    IconRectCalculator rectCtor = new IconRectCalculator(marker.getIcon());
                    Picture icon = new Picture(iconIndex);
                    icon.setAnchor(rectCtor.iconRect(
                            offsetX + marker.getX(),
                            offsetY + marker.getY()));
                    slide.addShape(icon);
                }
            } else {

                AutoShape shape = new AutoShape(ShapeTypes.Ellipse);
                shape.setAnchor(new Rectangle(
                        offsetX + marker.getX() - marker.getRadius(),
                        offsetY + marker.getY() - marker.getRadius(),
                        marker.getRadius()*2,
                        marker.getRadius()*2));

                shape.setFillColor(new Color(marker.getColor()));
                shape.setEscherProperty(EscherProperties.FILL__FILLOPACITY, 49087);
                shape.setLineColor(bubbleStrokeColor(marker.getColor()));
                slide.addShape(shape);
            }
        }

        // write to stream
        ppt.write(stream);

    }

    private byte[] renderBasemap(MapElement element) throws IOException {

        BufferedImage image = new BufferedImage(element.getWidth(), element.getHeight(), ColorSpace.TYPE_RGB);
        Graphics2D g2d = image.createGraphics();

        drawBasemap(g2d, element);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ImageIO.write(image, "PNG", out);

        return out.toByteArray();
    }

    private Dimension computePageSize(MapElement element) {
        // standard sizes
        Dimension[] stdSizes = new Dimension[] {
                new Dimension(720, 540), // Onscreen Show (4:5)
                new Dimension(720, 405), // Onscreen Show (16:9)
                new Dimension(780, 540), // A4 Portrait
                new Dimension(540, 780)
        };

        for(int i=0; i!=stdSizes.length; ++i) {
            if(stdSizes[i].getWidth() > element.getWidth() &&
                    stdSizes[i].getHeight() > element.getHeight()) {

                return stdSizes[i];

            }
        }

        return new Dimension(element.getWidth(), element.getHeight());
    }
}
