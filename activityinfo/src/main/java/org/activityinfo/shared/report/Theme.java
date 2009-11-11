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

/**
 * @author Alex Bertram
 */
public class Theme {

    public static String[] getColors() {
        String[] colors = new String[8];
        colors[0] = "#1F497D"; // 31, 73, 125
        colors[1] = "#EEECE1"; // 238, 236, 225

        return colors;
    }

    public static String[] getAccents() {
        String[] accents = new String[6];
        accents[0] = "#4F81BD"; // 79, 129, 189
        accents[1] = "#C0504D"; // 192, 80, 77
        accents[2] = "#98BB59"; // 155, 187, 89
        accents[3] = "#8064A2"; // 128, 100, 162
        accents[4] = "#4BACC6"; // 75, 172, 198
        accents[5] = "#F79646"; // 247, 150, 70
        return accents;
    }

    public static String getColor(int index) {
        return getColors()[index];
    }
}
