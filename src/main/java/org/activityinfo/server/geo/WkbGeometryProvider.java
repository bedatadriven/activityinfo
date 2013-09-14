package org.activityinfo.server.geo;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import org.activityinfo.server.util.logging.LogSlow;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.InStream;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

import freemarker.log.Logger;

@Singleton
public class WkbGeometryProvider implements AdminGeometryProvider {

    private static final Logger LOGGER = Logger
        .getLogger(WkbGeometryProvider.class.getName());

    private GeometryFactory geometryFactory;
    private GeometryStorage storage;

    @Inject
    public WkbGeometryProvider(GeometryStorage storage) {
        this.geometryFactory = new GeometryFactory();
        this.storage = storage;
    }

    @Override
    @LogSlow(threshold = 200)
    public List<AdminGeo> getGeometries(int adminLevelId) {
        try {
            List<AdminGeo> list = Lists.newArrayList();
            DataInputStream in = new DataInputStream(
                storage.openWkb(adminLevelId));
            WKBReader wkbReader = new WKBReader(geometryFactory);
            int count = in.readInt();
            for (int i = 0; i != count; ++i) {
                int id = in.readInt();
                LOGGER.info("Reading geometry for admin entity " + id);
                Geometry geometry = wkbReader.read(new DataInputInStream(in));
                list.add(new AdminGeo(id, geometry));
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DataInputInStream implements InStream {
        private DataInput in;

        public DataInputInStream(DataInput in) {
            super();
            this.in = in;
        }

        @Override
        public void read(byte[] buf) throws IOException {
            in.readFully(buf);
        }
    }
}
