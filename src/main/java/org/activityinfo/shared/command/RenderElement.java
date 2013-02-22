package org.activityinfo.shared.command;

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

import org.activityinfo.shared.command.result.UrlResult;
import org.activityinfo.shared.report.model.ReportElement;

/**
 * 
 * Renders a {@link org.activityinfo.shared.report.model.ReportElement} in the
 * specified format, saves the file to the server, and returns the name of the
 * temporary file that can be used to initiate a download.
 * 
 * See also: {@link org.activityinfo.server.endpoint.gwtrpc.DownloadServlet}
 * 
 * @author Alex Bertram
 */
public class RenderElement implements Command<UrlResult> {

    public enum Format {
        PNG,
        Excel,
        Excel_Data,
        PowerPoint,
        PDF,
        Word,
        HTML
    }

    private Format format;
    private String filename;
    private ReportElement element;

    public RenderElement() {
    }

    public RenderElement(ReportElement element, Format format) {
        this.element = element;
        this.format = format;
    }

    /**
     * 
     * @return The format into which to render the element.
     */
    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * 
     * @return The element to be rendered
     */
    public ReportElement getElement() {
        return element;
    }

    public void setElement(ReportElement element) {
        this.element = element;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
