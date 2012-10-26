package org.activityinfo.server.geo;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import org.activityinfo.server.util.logging.LogSlow;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

import freemarker.log.Logger;

@Singleton
public class WkbGeometryProvider implements AdminGeometryProvider {

	private static final Logger LOGGER = Logger.getLogger(WkbGeometryProvider.class.getName());
	
	private GeometryFactory geometryFactory;
	private GeometryStorage storage;

	@Inject
	public WkbGeometryProvider(GeometryStorage storage) {
		this.geometryFactory = new GeometryFactory();
		this.storage = storage;
	}
	
	@Override
	@LogSlow(threshold = 200)
	public List<AdminGeo> getGeometry(int adminLevelId) {
		try {
			List<AdminGeo> list = Lists.newArrayList();
			DataInputStream in = new DataInputStream(storage.openWkb(adminLevelId));
			WKBReader wkbReader = new WKBReader(geometryFactory);
			int count = in.readInt();
			for(int i=0;i!=count;++i) {
				int id = in.readInt();
				LOGGER.info("Reading geometry for admin entity " + id);
				Geometry geometry = wkbReader.read(new InputStreamInStream(in));
				list.add(new AdminGeo(id, geometry));
			}
			return list;
		} catch(IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
