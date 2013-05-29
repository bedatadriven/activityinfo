package org.activityinfo.server.endpoint.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.util.mapping.Extents;
import org.activityinfo.shared.util.mapping.Tile;
import org.activityinfo.shared.util.mapping.TileMath;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;

import com.google.code.appengine.awt.BasicStroke;
import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.color.ColorSpace;
import com.google.code.appengine.awt.geom.GeneralPath;
import com.google.code.appengine.awt.geom.Rectangle2D;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

public class AdminTileRenderer {

    private static final Logger LOGGER = Logger.getLogger(AdminTileRenderer.class.getName());

    private AdminLevel level;
    private Session session;

    private GeometryFactory gf = new GeometryFactory();

    private Envelope mapPixelBounds;

    @Inject
    public AdminTileRenderer(Session session, AdminLevel level) {
        this.session = session;
        this.level = level;
    }


    public byte[] render(int zoom, int x, int y) throws IOException {
        Extents extents = TileMath.tileBounds(zoom, x, y);

        mapPixelBounds = new Envelope(0, TileMath.TILE_SIZE, 0, TileMath.TILE_SIZE);

        Envelope envelope = new Envelope(
            extents.getMinLon(), extents.getMaxLon(),
            extents.getMinLat(), extents.getMaxLon());
        Geometry filter = gf.toGeometry(envelope);

        LOGGER.info("Creating Buffered Image...");

        BufferedImage image = new BufferedImage(TileMath.TILE_SIZE, TileMath.TILE_SIZE,
            ColorSpace.TYPE_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, TileMath.TILE_SIZE, TileMath.TILE_SIZE);

        LOGGER.info("Querying geometry...");

        Criteria criteria = session.createCriteria(AdminEntity.class);
        criteria.add(SpatialRestrictions.intersects("geometry", filter));
        criteria.add(Restrictions.eq("level", level));

        TiledMap map = new TiledMap(zoom, new Tile(x, y));
        
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        g2d.setFont(new Font("Arial", 0, 10));

        List<AdminEntity> entities = criteria.list();


        /*
         * Project Geometry onto this tile
         */
        LOGGER.info("Projecting geometry...");
        GeometryProjecter projector = new GeometryProjecter(map);

        Map<Integer, Geometry> projected = Maps.newHashMap();
        for (AdminEntity entity : entities) {
            Geometry transformed = projector.transform(entity.getGeometry());
            Geometry simplified = DouglasPeuckerSimplifier.simplify(transformed, 1.25);
            projected.put(entity.getId(), simplified);
        }

        /*
         * Draw outlines
         */
        LOGGER.info("Drawing geometry...");
        for (AdminEntity entity : entities) {
            Geometry geom = projected.get(entity.getId());
            g2d.draw(toPath(geom));
        }

        /*
         * Draw labels
         */
        LOGGER.info("Drawing geometry...");
        for (AdminEntity entity : entities) {
            Geometry geom = projected.get(entity.getId());
            Polygon polygon = largestPolygon(geom);

            if (polygon != null) {
                labelPolygon(g2d, polygon, entity.getName());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    private void labelPolygon(Graphics2D g2d, Polygon polygon, String name) {
        Rectangle2D labelBounds = g2d.getFont().getStringBounds(name, g2d.getFontRenderContext());
        Point centroid = polygon.getCentroid();
        
        Envelope labelEnv = labelGeometry(labelBounds, centroid);
        if (!labelEnv.intersects(mapPixelBounds)) {
            return;
        }
        
        if (!polygon.contains(gf.toGeometry(labelEnv))) {
            return;
        }

        g2d.drawString(name, (float) labelEnv.getMinX(), (float) labelEnv.getMaxY());
        
    }

    private Envelope labelGeometry(Rectangle2D labelBounds, Point centroid) {
        double width = labelBounds.getWidth() * 0.5;
        double height = labelBounds.getHeight() * 0.5;
        double cx = centroid.getX();
        double cy = centroid.getY();
        return new Envelope(cx - width, cx + width, cy - height, cy + height);
    }

    private Envelope labelGeometry() {
        return new Envelope();
    }

    public static Polygon largestPolygon(Geometry geometry) {
        if (geometry instanceof Polygon) {
            return (Polygon) geometry;
        } else if (geometry instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) geometry;
            double maxArea = 0;
            Polygon largestPolygon = null;
            for (int i = 0; i != gc.getNumGeometries(); ++i) {
                Polygon polygon = (Polygon) gc.getGeometryN(i);
                double area = polygon.getArea();
                if (area > maxArea) {
                    largestPolygon = polygon;
                    maxArea = area;
                }
            }
            return largestPolygon;
        }
        return null;
    }

    public static GeneralPath toPath(Geometry geometry) {
        GeneralPath path = new GeneralPath();
        for (int i = 0; i != geometry.getNumGeometries(); ++i) {
            Polygon polygon = (Polygon) geometry.getGeometryN(i);
            addRingToPath(path, polygon.getExteriorRing().getCoordinateSequence());
            for (int j = 0; j != polygon.getNumInteriorRing(); ++j) {
                addRingToPath(path, polygon.getInteriorRingN(j)
                    .getCoordinateSequence());
            }
        }
        return path;
    }

    private static void addRingToPath(GeneralPath path,
        CoordinateSequence coordinates) {

        for (int j = 0; j != coordinates.size(); ++j) {
            float x = (float) coordinates.getX(j);
            float y = (float) coordinates.getY(j);
            if (j == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();
    }

}
