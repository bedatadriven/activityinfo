/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
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

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.generator.map.TileProvider;
import org.sigmah.server.report.generator.map.TiledMap;
import org.sigmah.server.util.ColorUtil;
import org.sigmah.server.util.mapping.GoogleStaticMapsApi;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.map.GoogleBaseMap;
import org.sigmah.shared.map.TileBaseMap;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.report.content.BubbleLayerLegend;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.IconLayerLegend;
import org.sigmah.shared.report.content.IconMapMarker;
import org.sigmah.shared.report.content.MapLayerLegend;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.content.PieChartLegend;
import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.model.MapReportElement;

import com.google.inject.Inject;

/**
 * Renders a MapElement and its generated MapContent as an image
 * using Java 2D
 *
 * @author Alex Bertram
 */
public class ImageMapRenderer {
	
	private static final Logger LOGGER = Logger.getLogger(ImageMapRenderer.class);


	/**
	 * Provides tile images from a URL
	 * 	
	 * @author alex
	 *
	 */
    private final class RemoteTileProvider implements TileProvider {
        private TileBaseMap baseMap;

        private RemoteTileProvider(TileBaseMap baseMap) {
            this.baseMap = baseMap;
        }

        @Override
		public Image getImage(int zoom, int tileX, int tileY) {
            try {
                return ImageIO.read(new URL(baseMap.getTileUrl(zoom, tileX, tileY)));
            } catch (IOException e) {
                return null;
            }
        }
    }
	
	private final String mapIconRoot;

	private Map<String, BufferedImage> iconImages = new HashMap<String, BufferedImage>();

    @Inject
    public ImageMapRenderer(@MapIconPath String mapIconPath) {
        this.mapIconRoot = mapIconPath;
    }
    
    public String getMapIconRoot() {
    	return mapIconRoot;
    }

    public void renderToFile(MapReportElement element, File file) throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        render(element, os);
        os.close();
    }

    public void render(MapReportElement element, OutputStream os) throws IOException {
        BufferedImage image = new BufferedImage(element.getWidth(), element.getHeight(), ColorSpace.TYPE_RGB);
		Graphics2D g2d = image.createGraphics();

        render(element, g2d);

        ImageIO.write(image,"PNG", os);
    }
    
    public void render(MapReportElement element, Graphics2D g2d) {

        drawBasemap(g2d, element);

        // Draw markers

        for(MapMarker marker : element.getContent().getMarkers()) {
            if(marker instanceof IconMapMarker) {
                drawIcon(g2d, (IconMapMarker) marker);
            } else if( marker instanceof PieMapMarker) {
                drawPieMarker(g2d, (PieMapMarker) marker);
            } else if( marker instanceof BubbleMapMarker) {
                drawBubbleMarker(g2d, (BubbleMapMarker) marker);
            }
        }
    }

    public static void drawPieMarker(Graphics2D g2d, PieMapMarker marker) {

        // Determine the total area
        Rectangle area = new Rectangle();
        area.setRect(marker.getX() - marker.getRadius(),
                     marker.getY() - marker.getRadius(),
                     marker.getRadius() * 2,
                     marker.getRadius() *2 );

        // Get total value of all slices
        double total = 0.0D;
        for (PieMapMarker.SliceValue slice : marker.getSlices()) {
            total += slice.getValue();
        }

        // Draw each pie slice
        double curValue = 0.0D;
        int startAngle = 0;
        int i=0;
        for (PieMapMarker.SliceValue slice : marker.getSlices()) {
            // Compute the start and stop angles
            startAngle = (int)(curValue * 360 / total);
            int arcAngle = (int)(slice.getValue() * 360 / total);

            // Ensure that rounding errors do not leave a gap between the first and last slice
            if (i++ == marker.getSlices().size()-1) {
                arcAngle = 360 - startAngle;
            }

            // Set the color and draw a filled arc
            g2d.setColor(bubbleFillColor(ColorUtil.colorFromString(slice.getColor())));
            g2d.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);

            curValue += slice.getValue();
        }

        if(marker.getLabel() != null) {
            drawLabel(g2d, marker);
        }

    }
    


    private static void drawLabel(Graphics2D g2d, BubbleMapMarker marker) {
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, (int) (marker.getRadius() * 2f * 0.8f));

        // measure the bounds of the string so we can center it within
        // the bubble.
        Rectangle2D rect = font.getStringBounds(marker.getLabel(), g2d.getFontRenderContext());
        LineMetrics lm = font.getLineMetrics(marker.getLabel(), g2d.getFontRenderContext());

        g2d.setColor(ColorUtil.colorFromString(marker.getLabelColor()));
        g2d.setFont(font);
        g2d.drawString(marker.getLabel(),
                (int)(marker.getX() - (rect.getWidth() / 2)),
                (int)(marker.getY() + (lm.getAscent() / 2)));
    }

    private void drawIcon(Graphics2D g2d, IconMapMarker marker) {
        BufferedImage image = getIconImage(marker);
        if(image != null) {
             g2d.drawImage(image, null,
                marker.getX() - marker.getIcon().getAnchorX(),
                marker.getY() - marker.getIcon().getAnchorY());
        }
    }

	private BufferedImage getIconImage(IconMapMarker marker) {
		return getIconImage(marker.getIcon().getName());
	}
	
	protected <T extends ImageResult> T renderSlice(ImageCreator<T> imageCreator, Color color, int size) {
		T result = imageCreator.create(size, size);
		Graphics2D g2d = result.getGraphics();
		g2d.setPaint(color);
		g2d.fillRect(0,0,size,size);
//		
//		Ellipse2D.Double ellipse = new Ellipse2D.Double(
//                size/2, size/2, size/2, size/2);
//		
//	  	g2d.setPaint(color);
//		g2d.fill(ellipse);
//		
		return result;
	}

	private BufferedImage getIconImage(String name) {
		BufferedImage image = iconImages.get(name);
        if(image == null) {
            try {
                image = ImageIO.read(new File(mapIconRoot + "/" + name + ".png"));
                iconImages.put(name, image);
            } catch (IOException e) {
            	LOGGER.debug("Exception reading icon '" + name + "'", e);
            }
        }
		return image;
	}

    public void drawBasemap(Graphics2D g2d, MapReportElement element) {
    	AiLatLng center = element.getCenter() != null ? element.getCenter() : element.getContent().getCenter(); 
        TiledMap map = new TiledMap(element.getWidth(), element.getHeight(),
        		center,
                element.getContent().getZoomLevel());

        // Draw white backgrond first, in case we run out of tiles
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0,0,element.getWidth(),element.getHeight());

        BaseMap baseMap = element.getContent().getBaseMap();
        try {
	        if(baseMap instanceof TileBaseMap) {
		        drawTiledBaseMap(g2d, map, baseMap);
	        } else if(baseMap instanceof GoogleBaseMap) {
	        	drawGoogleBaseMap(g2d, map, (GoogleBaseMap)baseMap);
	        }
        } catch(Exception e) {
        	LOGGER.debug("Exception drawing basemap", e);
        }
    }

	private void drawTiledBaseMap(Graphics2D g2d, TiledMap map, BaseMap baseMap) {
		TileProvider tileProvider = new RemoteTileProvider((TileBaseMap) baseMap);
		map.drawLayer(g2d, tileProvider);
	}

	private void drawGoogleBaseMap(Graphics2D g2d, TiledMap map, GoogleBaseMap baseMap) throws IOException {
		
		// the google maps static api imposes a limit to the image sizes we can request, 
		// so we have to acquire the map imagery in batches
		
		for(int x=0;x<map.getWidth();x+=GoogleStaticMapsApi.MAX_WIDTH) {
			for(int y=0;y<map.getHeight();y+=GoogleStaticMapsApi.MAX_HEIGHT) {
				int sliceWidth = Math.min(GoogleStaticMapsApi.MAX_WIDTH, map.getWidth()-x);
				int sliceHeight = Math.min(GoogleStaticMapsApi.MAX_HEIGHT, map.getHeight()-y);
				
				AiLatLng sliceCenter = map.fromPixelToLatLng(x + (sliceWidth/2), y + (sliceHeight/2));
				
				BufferedImage image = ImageIO.read(GoogleStaticMapsApi.buildRequest()
						.setBaseMap(baseMap)
						.setCenter(sliceCenter)
						.setWidth(sliceWidth)
						.setHeight(sliceHeight)
						.setZoom(map.getZoom())
						.url());
				
				g2d.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
			}
		}
	}
	

    public static Color bubbleFillColor(Color colorRgb) {
        return new Color(colorRgb.getRed(), colorRgb.getGreen(), colorRgb.getBlue(), 179); //179=0.7*255
    }

    public static Color bubbleStrokeColor(int colorRgb) {
        return new Color(colorRgb).darker();
    }

    public static void drawBubbleMarker(Graphics2D g2d, BubbleMapMarker marker) {
        drawBubble(g2d, ColorUtil.colorFromString(marker.getColor()), marker.getX(), marker.getY(), marker.getRadius());
        if(marker.getLabel() != null) {
            drawLabel(g2d, marker);
        }
    }
    
    public <T extends ImageResult> T createLegendSymbol(MapLayerLegend<?> legend, ImageCreator<T> creator) {
    	if(legend instanceof BubbleLayerLegend) {
    		return new BubbleLegendRenderer((BubbleLayerLegend) legend).createImage(creator);
    	} else if(legend instanceof IconLayerLegend) {
    		return createIconImage(creator, (IconLayerLegend)legend);
    	} else if(legend instanceof PieChartLegend) {
    		return new PieChartLegendRenderer((PieChartLegend) legend).createImage(creator);
    	} else {
    		throw new IllegalArgumentException();
    	}
    }

    private <T extends ImageResult> T createIconImage(ImageCreator<T> creator, IconLayerLegend legend) {
    	BufferedImage icon = getIconImage(legend.getDefinition().getIcon());
    	T result = creator.create(icon.getWidth(), icon.getHeight());
    	result.getGraphics().drawImage(icon, 0, 0, null);
    	
    	return result;
    }
    
    public static void drawBubble(Graphics2D g2d, Color colorRgb, int x, int y, int radius) {
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
