

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

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.ReportElement;

/**
 * Generates the <code>Content</code> for a given <code>ReportElement</code>.
 *
 * @see org.activityinfo.shared.report.model.ReportElement
 *
 * @param <T> The type of <code>ReportElement</code> accepted by the ContentGenerator implementor.
 */
public interface ContentGenerator<T extends ReportElement> {


    /**
     * Retrieves data and computes and shapes this data into a presentation-ready format.
     * The generated result is stored in <code>element.content</code> upon completion.
     *
     * @param user The user for whom this content is generated. Effects such things as locale and
     *   access restrictions.
     * @param element The definition of the element for which to generate content
     * @param inheritedFilter Any filter inherited from an enclosing report or other container that should
     *      be intersected with the elements <code>Filter</code> to obtain the effective filter.
     * @param range The overall <code>DateRange</code> for the <code>Report</code>, or null if there is no
     *      overall date range.
     */
    void generate(User user, T element, Filter inheritedFilter, DateRange range);
	
}
