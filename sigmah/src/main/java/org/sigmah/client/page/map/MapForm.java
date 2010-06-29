/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import org.sigmah.shared.report.model.ReportElement;

public interface MapForm {

    public ReportElement getMapElement();


    public boolean validate();
}
