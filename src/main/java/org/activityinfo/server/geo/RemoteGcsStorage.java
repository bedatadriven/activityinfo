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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletResponse;

import com.google.common.io.Resources;

public class RemoteGcsStorage implements GeometryStorage {

    @Override
    public InputStream openWkb(int adminLevelId) throws IOException {
        URL url = new URL("http://commondatastorage.googleapis.com/aigeo/"
            + adminLevelId + ".wkb.gz");
        return new GZIPInputStream(url.openStream());
    }

    @Override
    public void serveJson(int adminLevelId, boolean gzip,
        HttpServletResponse response)
        throws IOException {
        URL url = new URL("http://commondatastorage.googleapis.com/aigeo/"
            + adminLevelId + ".json.gz");
        response.setHeader("Content-Encoding", "gzip");
        Resources.copy(url, response.getOutputStream());
    }
}
