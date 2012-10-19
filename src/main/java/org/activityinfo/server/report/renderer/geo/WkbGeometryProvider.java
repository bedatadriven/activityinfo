package org.activityinfo.server.report.renderer.geo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.InputStreamInStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public abstract class WkbGeometryProvider implements AdminGeometryProvider {

	private GeometryFactory geometryFactory;

	public WkbGeometryProvider() {
		this.geometryFactory = new GeometryFactory();
	}
	
	@Override
	public List<AdminGeo> getGeometry(int adminLevelId) {
		try {
			List<AdminGeo> list = Lists.newArrayList();
			DataInputStream in = new DataInputStream(openInput(adminLevelId));
			WKBReader wkbReader = new WKBReader(geometryFactory);
			int count = in.readInt();
			for(int i=0;i!=count;++i) {
				int id = in.readInt();
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

	protected abstract InputStream openInput(int adminLevelId) throws IOException;
	
}
