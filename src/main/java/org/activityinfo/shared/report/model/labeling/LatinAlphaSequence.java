/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model.labeling;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Provides a sequence of latin letters: A...Z, AA..AZ
 *
 * @author Alex Bertram
 */
@XmlRootElement
public class LatinAlphaSequence implements LabelSequence {

    private int nextNumber = 0;

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
