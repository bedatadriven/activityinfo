/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model.labeling;

import java.io.Serializable;

public interface LabelSequence extends Serializable {

    /**
     *
     * @return The next label in the sequence
     */
    String next();

}
