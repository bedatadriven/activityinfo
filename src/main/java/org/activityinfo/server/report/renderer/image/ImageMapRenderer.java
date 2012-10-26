/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.server.geo.AdminGeo;
import org.activityinfo.server.geo.AdminGeometryProvider;
import org.activityinfo.server.geo.ClasspathGeometryProvider;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.generator.map.TileProvider;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.server.util.ColorUtil;
import org.activityinfo.server.util.logging.LogSlow;
import org.activityinfo.server.util.mapping.GoogleStaticMapsApi;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.map.TileBaseMap;
import org.activityinfo.shared.report.content.AdminOverlay;
import org.activityinfo.shared.report.content.AdminMarker;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.MapReportElement;

import com.google.code.appengine.awt.BasicStroke;
import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.Rectangle;
import com.google.code.appengine.awt.color.ColorSpace;
import com.google.code.appengine.awt.geom.Ellipse2D;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Renders a MapElement and its generated MapContent
 */
public class ImageMapRenderer {
	
	private static final Logger LOGGER = Logger.getLogger(ImageMapRenderer.class.getName());


	/**
	 * Provides tile images from a URL
	 *
	 */
    private final class RemoteTileProvider implements TileProvider {
        private TileBaseMap baseMap;

        private RemoteTileProvider(TileBaseMap baseMap) {
            this.baseMap = baseMap;
        }

        @Override
		public String getImageUrl(int zoom, int tileX, int tileY) {
            return baseMap.getTileUrl(zoom, tileX, tileY);
        }
    }
	
	private final String mapIconRoot;

	private Map<String, BufferedImage> iconImages = new HashMap<String, BufferedImage>();

	private AdminGeometryProvider geometryProvider;
	
    @Inject
    public ImageMapRenderer(AdminGeometryProvider geometryProvider, @MapIconPath String mapIconPath) {
    	this.geometryProvider = geometryProvider;
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

        drawOverlays(element, g2d);
    }

	protected void drawOverlays(MapReportElement element, Graphics2D g2d) {
		TiledMap map = createTileMap(element);
		
		for(AdminOverlay overlay : element.getContent().getAdminOverlays()) {
			drawAdminOverlay(map, g2d, overlay);
		}
		
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

	@LogSlow(threshold = 50)
	protected void drawAdminOverlay(TiledMap map, Graphics2D g2d, AdminOverlay overlay) {
		List<AdminGeo> geometry = geometryProvider.getGeometry(overlay.getAdminLevelId());
		for(AdminGeo adminGeo : geometry) {
			AdminMarker polygon = overlay.getPolygon(adminGeo.getId());
            g2d.setColor(bubbleFillColor(ColorUtil.colorFromString(polygon.getColor())));
            fill(map, g2d, adminGeo.getGeometry());
		}		
	}

	private void fill(TiledMap map, Graphics2D g2d, Geometry geometry) {
		for(int i=0;i!=geometry.getNumGeometries();++i) {
			Polygon polygon = (Polygon) geometry.getGeometryN(i);
			Coordinate[] coordinates = polygon.getCoordinates();
			int x[] = new int[coordinates.length];
			int y[] = new int[coordinates.length];
			for(int j=0;j!=coordinates.length;++j) {
				Point point = map.fromLatLngToPixel(new AiLatLng(coordinates[j].y,coordinates[j].x));
				x[j] = point.getX();
				y[j] = point.getY();
			}
			g2d.fillPolygon(x, y, x.length);
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
      //  Font font = new Font(Font.SANS_SERIF, Font.BOLD, (int) (marker.getRadius() * 2f * 0.8f));

        // measure the bounds of the string so we can center it within
        // the bubble.
        
        // QUICK FIX: labeling set inappropriately as default for new map layers
        // and no method in UI to turn them off. 
        
        
//        Rectangle2D rect = font.getStringBounds(marker.getLabel(), g2d.getFontRenderContext());
//        LineMetrics lm = font.getLineMetrics(marker.getLabel(), g2d.getFontRenderContext());
//
//        g2d.setColor(ColorUtil.colorFromString(marker.getLabelColor()));
//        g2d.setFont(font);
//        g2d.drawString(marker.getLabel(),
//                (int)(marker.getX() - (rect.getWidth() / 2)),
//                (int)(marker.getY() + (lm.getAscent() / 2)));
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
	
	protected ItextGraphic renderSlice(ImageCreator imageCreator, Color color, int size) {
		ItextGraphic result = imageCreator.create(size, size);
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
                image = ImageIO.read(getImageFile(name));
                iconImages.put(name, image);
            } catch (IOException e) {
            	LOGGER.log(Level.WARNING, "Exception reading icon '" + name + "'", e);
            }
        }
		return image;
	}

	public File getImageFile(String name) {
		return new File(mapIconRoot + "/" + name + ".png");
	}

    public void drawBasemap(Graphics2D g2d, MapReportElement element) {

        // Draw white backgrond first, in case we run out of tiles
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0,0,element.getWidth(),element.getHeight());

        G2dTileHandler tileHandler = new G2dTileHandler(g2d);

    	drawBasemap(element, tileHandler);
    }

	protected void drawBasemap(MapReportElement element,
			TileHandler tileHandler) {
		TiledMap map = createTileMap(element);
        BaseMap baseMap = element.getContent().getBaseMap();
        try {
	        if(baseMap instanceof TileBaseMap) {
		        drawTiledBaseMap(tileHandler, map, baseMap);
	        } else if(baseMap instanceof GoogleBaseMap) {
	        	drawGoogleBaseMap(tileHandler, map, (GoogleBaseMap)baseMap);
	        }
        } catch(Exception e) {
        	LOGGER.log(Level.WARNING, "Exception drawing basemap", e);
        }
	}

	protected TiledMap createTileMap(MapReportElement element) {
		AiLatLng center = element.getCenter() != null ? element.getCenter() : element.getContent().getCenter(); 
        TiledMap map = new TiledMap(element.getWidth(), element.getHeight(),
        		center,
                element.getContent().getZoomLevel());
		return map;
	}

	private void drawTiledBaseMap(TileHandler handler, TiledMap map, BaseMap baseMap) {
		TileProvider tileProvider = new RemoteTileProvider((TileBaseMap) baseMap);
		map.drawLayer(handler, tileProvider);
	}

	
	@LogSlow(threshold = 100)
	protected void drawGoogleBaseMap(TileHandler tileHandler, TiledMap map, GoogleBaseMap baseMap) throws IOException {
		
		// the google maps static api imposes a limit to the image sizes we can request, 
		// so we have to acquire the map imagery in batches
		
		for(int x=0;x<map.getWidth();x+=GoogleStaticMapsApi.MAX_WIDTH) {
			for(int y=0;y<map.getHeight();y+=GoogleStaticMapsApi.MAX_HEIGHT) {
				int sliceWidth = Math.min(GoogleStaticMapsApi.MAX_WIDTH, map.getWidth()-x);
				int sliceHeight = Math.min(GoogleStaticMapsApi.MAX_HEIGHT, map.getHeight()-y);
				
				AiLatLng sliceCenter = map.fromPixelToLatLng(x + (sliceWidth/2), y + (sliceHeight/2));
				
				String tileUrl = GoogleStaticMapsApi.buildRequest()
						.setBaseMap(baseMap)
						.setCenter(sliceCenter)
						.setWidth(sliceWidth)
						.setHeight(sliceHeight)
						.setZoom(map.getZoom())
						.urlString();
				
				tileHandler.addTile(tileUrl, x, y, sliceWidth, sliceHeight);
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
