/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.image;

import com.google.inject.Inject;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.generator.map.TileProvider;
import org.sigmah.server.report.generator.map.TiledMap;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.LocalBaseMap;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.IconMapMarker;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.model.MapElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Renders a MapElement and its generated MapContent as an image
 * using Java 2D
 *
 * @author Alex Bertram
 */
public class ImageMapRenderer {


    /**
     * Implements the TileProvider interface for
     * LocalBaseMap
     */
    private class LocalTileProvider implements TileProvider {
        private LocalBaseMap baseMap;

        private LocalTileProvider(LocalBaseMap baseMap) {
            this.baseMap = baseMap;
        }

        public Image getImage(int zoom, int tileX, int tileY) {
            try {
                return ImageIO.read(new File(baseMap.getLocalTilePath(zoom, tileX, tileY)));
            } catch (IOException e) {
                return null;
            }
        }
    }

    private class RemoteTileProvider implements TileProvider {
        private BaseMap baseMap;

        private RemoteTileProvider(BaseMap baseMap) {
            this.baseMap = baseMap;
        }

        public Image getImage(int zoom, int tileX, int tileY) {
            try {
                return ImageIO.read(new URL(baseMap.getTileUrl(zoom, tileX, tileY)));
            } catch (IOException e) {
                return null;
            }
        }
    }

    protected final String mapIconRoot;

    @Inject
    public ImageMapRenderer(@MapIconPath String mapIconPath) {
        this.mapIconRoot = mapIconPath;
    }

    public void renderToFile(MapElement element, File file) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        render(element, os);
        os.close();
    }

    public void render(MapElement element, OutputStream os) throws IOException {
        BufferedImage image = new BufferedImage(element.getWidth(), element.getHeight(), ColorSpace.TYPE_RGB);
		Graphics2D g2d = image.createGraphics();

        render(element, g2d);

        ImageIO.write(image,"PNG", os);
    }

    public void render(MapElement element, Graphics2D g2d) {

        drawBasemap(g2d, element);

        // Draw markers

        Map<String, BufferedImage> iconImages = new HashMap<String, BufferedImage>();

        for(MapMarker marker : element.getContent().getMarkers()) {
            if(marker instanceof IconMapMarker) {
                drawIcon(g2d, iconImages, (IconMapMarker) marker);
            } else if( marker instanceof PieMapMarker) {
                drawPieMarker(g2d, (PieMapMarker) marker);
            } else if( marker instanceof BubbleMapMarker) {
                drawBubbleMarker(g2d, (BubbleMapMarker) marker);
            }
        }
    }

    private void drawPieMarker(Graphics2D g2d, PieMapMarker marker) {

        // Determine the total area
        Rectangle area = new Rectangle();
        area.setRect(marker.getX() - marker.getRadius(),
                     marker.getY() - marker.getRadius(),
                     marker.getRadius() * 2,
                     marker.getRadius() *2 );

        // Get total value of all slices
        double total = 0.0D;
        for (PieMapMarker.Slice slice : marker.getSlices()) {
            total += slice.getValue();
        }

        // Draw each pie slice
        double curValue = 0.0D;
        int startAngle = 0;
        int i=0;
        for (PieMapMarker.Slice slice : marker.getSlices()) {
            // Compute the start and stop angles
            startAngle = (int)(curValue * 360 / total);
            int arcAngle = (int)(slice.getValue() * 360 / total);

            // Ensure that rounding errors do not leave a gap between the first and last slice
            if (i++ == marker.getSlices().size()-1) {
                arcAngle = 360 - startAngle;
            }

            // Set the color and draw a filled arc
            g2d.setColor(bubbleFillColor(slice.getColor()));
            g2d.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);

            curValue += slice.getValue();
        }

        if(marker.getLabel() != null) {
            drawLabel(g2d, marker);
        }

    }

    private void drawLabel(Graphics2D g2d, BubbleMapMarker marker) {
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, (int) (marker.getRadius() * 2f * 0.8f));

        // measure the bounds of the string so we can center it within
        // the bubble.
        Rectangle2D rect = font.getStringBounds(marker.getLabel(), g2d.getFontRenderContext());
        LineMetrics lm = font.getLineMetrics(marker.getLabel(), g2d.getFontRenderContext());

        g2d.setColor(new Color(marker.getLabelColor()));
        g2d.setFont(font);
        g2d.drawString(marker.getLabel(),
                (int)(marker.getX() - (rect.getWidth() / 2)),
                (int)(marker.getY() + (lm.getAscent() / 2)));
    }

    private void drawIcon(Graphics2D g2d, Map<String, BufferedImage> iconImages, IconMapMarker marker) {
        BufferedImage image = iconImages.get(marker.getIcon().getName());
        if(image == null) {
            try {
                image = ImageIO.read(new File(mapIconRoot + "/" + marker.getIcon().getName() + ".png"));
                iconImages.put(marker.getIcon().getName(), image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(image != null) {
             g2d.drawImage(image, null,
                marker.getX() - marker.getIcon().getAnchorX(),
                marker.getY() - marker.getIcon().getAnchorY());
        }
    }

    public void drawBasemap(Graphics2D g2d, MapElement element) {
        TiledMap map = new TiledMap(element.getWidth(), element.getHeight(),
                element.getContent().getExtents().center(),
                element.getContent().getZoomLevel());

        // Draw white backgrond first, in case we run out of tiles
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0,0,element.getWidth(),element.getHeight());


        TileProvider tileProvider;
        if(element.getContent().getBaseMap() instanceof LocalBaseMap) {
            tileProvider = new LocalTileProvider((LocalBaseMap) element.getContent().getBaseMap());
        } else {
            tileProvider = new RemoteTileProvider(element.getContent().getBaseMap());
        }

        // Draw tiled map background
        try {
            map.drawLayer(g2d, tileProvider);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Color bubbleFillColor(int colorRgb) {
        Color color = new Color(colorRgb);
        float[] rgb = color.getComponents(null);

        return new Color(rgb[0], rgb[1], rgb[2], 0.70f);
    }

    public Color bubbleStrokeColor(int colorRgb) {
        return new Color(colorRgb).darker();
    }

    public void drawBubbleMarker(Graphics2D g2d, BubbleMapMarker marker) {
        drawBubble(g2d, marker.getColor(), marker.getX(), marker.getY(), marker.getRadius());
        if(marker.getLabel() != null) {
            drawLabel(g2d, marker);
        }
    }


    public void drawBubble(Graphics2D g2d, int colorRgb, int x, int y, int radius) {
        Ellipse2D.Double ellipse = new Ellipse2D.Double(
                x - radius,
                y - radius,
                radius * 2 , radius * 2);

        g2d.setPaint(bubbleFillColor(colorRgb));
        g2d.fill(ellipse);

        g2d.setPaint(bubbleFillColor(colorRgb));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(ellipse);
    }

}
