package org.activityinfo.server.util.html;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HtmlTag {

    private String name;
    private StringBuilder innerText = new StringBuilder(0);
    private boolean closed;
    private Map<String, HtmlAttribute> attributes = new HashMap<String, HtmlAttribute>();

    public HtmlTag(String name) {
        this.name = name;

    }

    public String getName() {
        return this.name;
    }

    protected HtmlAttribute getAttribute(String name) {
        HtmlAttribute attrib = attributes.get(name);

        if (attrib == null) {
            attrib = new HtmlAttribute(name);
            attributes.put(name, attrib);
        }
        return attrib;
    }

    public HtmlTag at(String name, String value) {
        getAttribute(name).setValue(value);
        return this;
    }

    public HtmlTag at(String name, int value) {
        getAttribute(name).setValue(value);
        return this;
    }

    public HtmlTag styleName(String className) {
        if (className != null) {
            getAttribute("class").append(className, ' ');
        }
        return this;
    }

    public HtmlTag styleName(String className, int suffix) {
        return styleName(className + suffix);
    }

    public HtmlTag styleNameIf(String className, boolean condition) {
        if (condition) {
            styleName(className);
        }
        return this;
    }

    public Collection<HtmlAttribute> getAttributes() {
        return attributes.values();
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public HtmlTag text(String text) {
        innerText.append(text);
        return this;
    }

    public HtmlTag text(Collection<?> elements, String delimeter) {

        boolean first = true;
        for (Object element : elements) {
            if (!first) {
                innerText.append(delimeter);
            }
            innerText.append(element.toString());

            first = false;
        }
        return this;
    }

    public HtmlTag nbsp() {
        innerText.append("&nbsp;");
        return this;
    }

    public HtmlTag close() {
        closed = true;
        return this;
    }

    public String getInnerText() {
        if (innerText.length() == 0) {
            return null;
        } else {
            return innerText.toString();
        }
    }

    public boolean isClosed() {
        return closed;
    }

}
