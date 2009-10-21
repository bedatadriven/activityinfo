/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.shared.report;

import com.google.gwt.user.client.rpc.RemoteService;
import org.activityinfo.shared.report.model.ReportElement;
import org.activityinfo.shared.command.RenderElement;

/**
 * Defines a service for exporting ReportElements to the client.
 *
 */
public interface ExportService extends RemoteService {

    public String export(String authToken, ReportElement element, RenderElement.Format format);
    
}
