/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator;

import com.google.inject.Inject;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.shared.dao.SiteTableDAO;
import org.sigmah.shared.report.model.ReportElement;

/**
 * This is the base class for generators of element that
 * take the essential form of a list of sites. For example,
 * we have the table (grid) of sites, narrative description
 * of sites, or a map of sites.
 *
 * @author Alex Bertram
 * @param <ElementT>
 */
public abstract class ListGenerator<ElementT extends ReportElement>
        extends BaseGenerator<ElementT> {


    protected final SiteTableDAO siteDAO;

    @Inject
    public ListGenerator(PivotDAO pivotDAO, SiteTableDAO siteDAO) {
        super(pivotDAO);

        this.siteDAO = siteDAO;
    }

}
