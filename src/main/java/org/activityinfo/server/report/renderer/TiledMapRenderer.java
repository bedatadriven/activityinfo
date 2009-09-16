package org.activityinfo.server.report.renderer;

import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.TableElement;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class TiledMapRenderer  {


	protected BufferedImage generateMapImage(TableElement element, TableData data, int width, int height) throws IOException {

//		/*
//		 * Create our initial list of markers
//		 */
//
//		Extents extents = MarkerUtil.
//
//
//		for(TableData.Row row : data.getRows()) {
//			if(row.hasXY) {
//				Marker<TableData.Row> marker = new Marker<TableData.Row>(row);
//
//				if(marker.getLatLng() != null) {
//					extents.grow(marker.getLatLng());
//				}
//
//				markers.add(marker);
//			}
//		}
//
//		/*
//		 * Dimension the map and get a projection together
//		 */
//
//		LatLng center = extents.center();
//
//		int zoom = TileMath.zoomLevelForExtents(extents, width, height);
//
//		if(zoom < 6)
//			zoom = 6;
//
//		TiledMap map = new TiledMap(width, height, center, zoom);
//
//        final int radius = 10;
//
//        SimpleClusterer clusterer = new SimpleClusterer();
//        List<Marker> markers = clusterer.cluster(map, data.getRows(), radius);
//
//		/*
//		 * Sort and number the markers
//		 */
//
//		Collections.sort(markers, new Comparator<Marker<TableData.Row>>() {
//
//			@Override
//			public int compare(Marker<TableData.Row> m1, Marker<TableData.Row> m2) {
//
//				Point c1 = m1.center();
//				Point c2 = m2.center();
//
//				double y1 = Math.round(c1.getY() / (radius*1.5));
//				double y2 = Math.round(c2.getY() / (radius*1.5));
//
//				if(y1>y2) {
//					return 1;
//				} else if(y1<y2) {
//					return -1;
//				} else {
//					return Double.compare(c1.getX(), c2.getX());
//				}
//			}
//		});
//
//		int symbol = 0;
//		for(Marker<TableData.Row> marker : markers) {
//
//			marker.setSymbol(Integer.toString(symbol++));
//
//			for(TableData.Row row : marker.getSites()) {
//
//				row.setMarker(marker);
//
//			}
//		}
//
//
//
//		/*
//		 * Create our map
//		 */
//
//		BufferedImage image = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
//		Graphics2D g2d = image.createGraphics();
//
//		map.drawLayer(g2d, );
//
//		Font font = new Font("Arial", Font.BOLD, 10);
//		g2d.setFont(font);
//		FontMetrics metrics = g2d.getFontMetrics(font);
//
//		for(Marker<TableData.Row> marker : markers) {
//			Point px = marker.center();
//
//			g2d.setColor(Color.BLUE);
//			g2d.setPaint(Color.BLUE);
//			g2d.fill(new Ellipse2D.Double(px.x - radius, px.y - radius, radius*2, radius*2));
//
//			int txtHeight = metrics.getHeight();
//			int txtWidth = metrics.stringWidth(marker.createSymbol());
//
//			g2d.setColor(Color.WHITE);
//			g2d.drawString(marker.createSymbol(), px.x - txtWidth/2, px.y + txtHeight / 2 );
//
//		}
//
////		for(Marker<TableData.Row> marker : origMarkers) {
////			Point px = marker.center();
////
////			g2d.setColor(Color.RED);
////			g2d.draw(new Ellipse2D.Double(px.x - radius, px.y - radius, radius*2, radius*2));
////
////			String label = "" ;
////
////			int txtHeight = metrics.getHeight();
////			int txtWidth = metrics.stringWidth(label);
////
////
////		}
//
//		return image;
        return null;


    }
}
