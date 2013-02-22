

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

import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.model.ReportElement;

/**
 * Generates and returns to the client the content of an element.
 *
 * Returns: {@link org.activityinfo.shared.report.content.Content}
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GenerateElement<T extends Content> implements Command<T> {

    private ReportElement element;

    protected GenerateElement() {
    }

    public GenerateElement(ReportElement element) {
        this.element = element;
    }

    public ReportElement getElement() {
        return element;
    }

    public void setElement(ReportElement element) {
        this.element = element;
    }

	@Override
	public String toString() {
		return "GenerateElement [element=" + element + "]";
	}   
}
