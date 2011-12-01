/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report;

/**
 * @author Alex Bertram
 */
public final class Theme {
	
	private Theme() {}

    public static String[] getColors() {
        return new String[] {
        	"#1F497D", // 31, 73, 125
        	"#EEECE1" }; // 238, 236, 225

    }

    public static String[] getAccents() {
        return new String[] {
        	"#4F81BD", // 79, 129, 189
        	"#C0504D", // 192, 80, 77
        	"#98BB59", // 155, 187, 89
        	"#8064A2", // 128, 100, 162
        	"#4BACC6", // 75, 172, 198
        	"#F79646"}; // 247, 150, 70
    }

    public static String getColor(int index) {
        return getColors()[index];
    }
}
