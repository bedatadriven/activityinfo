/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report;

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
