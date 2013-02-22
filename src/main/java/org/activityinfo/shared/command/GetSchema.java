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

import org.activityinfo.shared.dto.SchemaDTO;

/**
 * Returns a {@link org.activityinfo.shared.dto.SchemaDTO} data transfer object
 * that includes the definitions of a databases visible to the authenticated
 * user.
 * 
 * @author Alex Bertram
 */
public class GetSchema implements Command<SchemaDTO> {

    private int newElement;
    private String foobar;
    private double thirdElement;
    private double forthElement;
    private int foofoo;

    public int getFoofoo() {
        return foofoo;
    }

    public void setFoofoo(int foofoo) {
        this.foofoo = foofoo;
    }

    public double getForthElement() {
        return forthElement;
    }

    public void setForthElement(double forthElement) {
        this.forthElement = forthElement;
    }

    public double getThirdElement() {
        return thirdElement;
    }

    public void setThirdElement(double thirdElement) {
        this.thirdElement = thirdElement;
    }

    public int getNewElement() {
        return newElement;
    }

    public void setNewElement(int newElement) {
        this.newElement = newElement;
    }

    public String getFoobar() {
        return foobar;
    }

    public void setFoobar(String foobar) {
        this.foobar = foobar;
    }

    @Override
    public String toString() {
        return "GetSchema";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof GetSchema;
    }
}
