/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

/**
 *
 * Provides a sequence of arabic numbers: 1, 2, 3...
 *
 * @author Alex Bertram
 */
public class ArabicNumberSequence implements LabelSequence {

    private int number = 1;

    @Override
    public String next() {
        return Integer.toString(number++);
    }
}
