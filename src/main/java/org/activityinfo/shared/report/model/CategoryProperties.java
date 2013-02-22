

package org.activityinfo.shared.report.model;

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

import java.io.Serializable;

/*
 * @author Alex Bertram
 */
public class CategoryProperties implements Serializable {

    private String label;
    private Integer color;

    public CategoryProperties() {
        
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public void setColor(int r, int g, int b) {
       this.color = ((r & 0xFF) << 16) |
                    ((g & 0xFF) << 8)  |
                    ((b & 0xFF) << 0);
    }

    public static CategoryProperties Color(int r, int g, int b) {
        CategoryProperties props = new CategoryProperties();
        props.setColor(r, g, b);
        return props;
    }
}
