/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

/**
 *
 * Provides a sequence of latin letters: A...Z, AA..AZ
 *
 * @author Alex Bertram
 */
public class LatinAlphaSequence implements LabelSequence {

    int nextNumber = 0;

    @Override
    public String next() {
        int number = nextNumber ++;
        StringBuilder label = new StringBuilder();
        while(number >= 26) {
            int digit = number / 26;
            label.append((char)(65+digit));
            number -= digit * 26;
        }
        return label.toString();
    }
}
