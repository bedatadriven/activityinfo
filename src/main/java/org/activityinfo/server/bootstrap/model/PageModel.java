

package org.activityinfo.server.bootstrap.model;

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

import com.sun.jersey.api.view.Viewable;

public abstract class PageModel {

    private static final String SUFFIX = "PageModel";

    public static <T extends PageModel> String getTemplateName(Class<T> pageModelClass) {
        String className = pageModelClass.getSimpleName();
        assert className.endsWith(SUFFIX) : "Page Model classes should end in '" + SUFFIX + "'";

        return "/page/" + className.substring(0, className.length() - SUFFIX.length()) + ".ftl";
    }

    public String getTemplateName() {
        return getTemplateName(getClass());
    }
    
    public Viewable asViewable() {
        return new Viewable(getTemplateName(), this);
    }
}
