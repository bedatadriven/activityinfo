package org.activityinfo.server.report.output;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class LocalStorageProvider implements StorageProvider {

    private String folder;

    public LocalStorageProvider(String folder) {
        this.folder = folder.replace('\\', '/');
    }

    @Override
    public TempStorage allocateTemporaryFile(String mimeType, String suffix)
        throws IOException {
        String path = folder + "/img" + Long.toString((new Date()).getTime())
            + suffix;
        OutputStream stream = new FileOutputStream(path);

        return new TempStorage("file://" + path, stream);
    }

}
