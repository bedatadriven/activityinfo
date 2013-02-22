package org.activityinfo.shared.report;

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

/**
 * @author Alex Bertram
 */
public final class Theme {

    public static final String[] ACCENTS = new String[] {
        "#4F81BD", // 79, 129, 189
        "#C0504D", // 192, 80, 77
        "#98BB59", // 155, 187, 89
        "#8064A2", // 128, 100, 162
        "#4BACC6", // 75, 172, 198
        "#F79646" }; // 247, 150, 70

    private Theme() {
    }

    public static String[] getColors() {
        return new String[] {
            "#1F497D", // 31, 73, 125
            "#EEECE1" }; // 238, 236, 225

    }

    public static String[] getAccents() {
        return ACCENTS;
    }

    public static String getAccent(int index) {
        return ACCENTS[index % ACCENTS.length];
    }

    public static String getColor(int index) {
        return getColors()[index];
    }
}
