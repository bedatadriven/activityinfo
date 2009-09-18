package org.activityinfo.server.report.renderer.image;

import org.activityinfo.server.report.generator.map.TileProvider;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.map.LocalBaseMap;
import org.activityinfo.shared.map.BaseMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import com.google.inject.Inject;
/*
 * @author Alex Bertram
 */

public class ImageMapRenderer {


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

    public void renderToFile(File file, MapElement element) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        render(element, os);
        os.close();
    }

    public void render(MapElement element, OutputStream os) throws IOException {

        BufferedImage image = new BufferedImage(element.getWidth(), element.getHeight(), ColorSpace.TYPE_RGB);
		Graphics2D g2d = image.createGraphics();

        render(g2d, element);

        ImageIO.write(image,"PNG", os);

    }

    public void render(Graphics2D g2d, MapElement element) {

        drawBasemap(g2d, element);

        // Draw markers

        Map<String, BufferedImage> iconImages = new HashMap<String, BufferedImage>();

        for(MapMarker marker : element.getContent().getMarkers()) {
            if(marker.getIcon() != null) {
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
            drawBubble(g2d, marker.getColor(), marker.getX(), marker.getY(), marker.getRadius());
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
