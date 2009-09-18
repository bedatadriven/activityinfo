package org.activityinfo.server.report.generator;

import org.activityinfo.server.dao.hibernate.PivotDAO;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.Location;
import org.activityinfo.shared.domain.SiteColumn;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.Filter;
import org.activityinfo.shared.report.model.ReportElement;
import org.hibernate.criterion.*;

import com.google.inject.Inject;

/**
 * This is the base class for generators of element that 
 * take the essential form of a list of sites. For example, 
 * we have the table (grid) of sites, narrative description 
 * of sites, or a map of sites.
 * 
 * @author Alex Bertram
 *
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
