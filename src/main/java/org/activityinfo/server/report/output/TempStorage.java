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

import java.io.OutputStream;

public class TempStorage {

    private String url;
    private OutputStream outputStream;

    public TempStorage(String url, OutputStream stream) {
        this.url = url;
        this.outputStream = stream;
    }

    /**
     * 
     * @return the publicly-accessible URL for this temporary resource
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @return the output stream to which the contents of the file should be
     *         written. The stream MUST be closed by the caller.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
