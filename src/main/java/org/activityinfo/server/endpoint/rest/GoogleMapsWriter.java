package org.activityinfo.server.endpoint.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.util.mapping.GooglePolylineEncoder;
import org.activityinfo.shared.util.mapping.PolylineEncoded;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jettison.json.JSONException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

public class GoogleMapsWriter  {

    private StringWriter stringWriter = new StringWriter();
    private GooglePolylineEncoder encoder;
    private JsonGenerator writer;

    public GoogleMapsWriter() throws IOException {
        encoder = new GooglePolylineEncoder();
        writer = Jackson.createJsonFactory(stringWriter);
    }

    public String write(List<AdminEntity> entities) throws JsonGenerationException, IOException {
        writer.writeStartObject();
        writer.writeNumberField("zoomFactor", encoder.getZoomFactor());
        writer.writeNumberField("numLevels", encoder.getNumLevels());
        writer.writeArrayFieldStart("entities");
        
        for(AdminEntity entity : entities) {
            write(entity.getId(), entity.getGeometry());
        }
        
        writer.writeEndArray();
        writer.writeEndObject();
        return stringWriter.toString();
    }

    public void write(int adminEntityId, Geometry geometry) throws IOException {
        try {
            writer.writeStartObject();
            writer.writeNumberField("id", adminEntityId);
            writer.writeArrayFieldStart("polygons");

            for (int i = 0; i != geometry.getNumGeometries(); ++i) {
                Polygon polygon = (Polygon) geometry.getGeometryN(i);
                writeLinearRing(polygon.getExteriorRing());
                for (int j = 0; j != polygon.getNumInteriorRing(); ++j) {
                    writeLinearRing(polygon.getInteriorRingN(j));
                }
            }

            writer.writeEndArray();
            writer.writeEndObject();
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }

    private void writeLinearRing(LineString ring) throws IOException, JSONException {
        PolylineEncoded encoded = encoder.dpEncode(ring.getCoordinates());
        writer.writeStartObject();
        writer.writeStringField("points", encoded.getPoints());
        writer.writeStringField("levels", encoded.getLevels());
        writer.writeEndObject();
    }

}
