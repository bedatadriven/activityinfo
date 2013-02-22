

package org.activityinfo.server.report.generator;

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

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.inject.Inject;

/**
 * This is the base class for generators of element that
 * take the essential form of a list of sites. For example,
 * we have the table (grid) of sites, narrative description
 * of sites, or a map of sites.
 *
 * @author Alex Bertram
 * @param <ElementT>
 */
public abstract class ListGenerator<ElementT extends ReportElement>
        extends BaseGenerator<ElementT> {


    @Inject
    public ListGenerator(DispatcherSync dispatcher) {
        super(dispatcher);
    }

}
