package org.activityinfo.server.report.generator;

import org.activityinfo.shared.report.content.PivotTableData.Axis;
import org.activityinfo.shared.report.content.YearCategory;
import org.activityinfo.shared.report.model.DateDimension;
import org.activityinfo.shared.report.model.DateUnit;

import java.util.Collection;

/**
 * While we don't normally want to include gobs of empty cells, 
 * for axes that are continuous like date dimensions, we also
 * don't want gaps in the middle. 
 * 
 * This class traverses the header hierarchy and fills in where necessary.
 * I.e.:
 * 
 * <pre>
 * Jan | Mar | Apr | July
 * </pre>
 * 
 * becomes
 * 
 * <pre>
 * Jan | Feb | Mar | Apr | May | June | July
 * </pre>
 * 
 * 
 * @author Alex Bertram
 *
 */
public class DateAxisCompleter implements PivotTableAxisCompleter {
	
	public DateAxisCompleter() {
	
	}


	protected boolean isMonthAxis(Axis axis) {
		if(axis == null)
			return false;
		if(axis.getDimension() == null)
			return false;
		if(!(axis.getDimension() instanceof DateDimension))
			return false;
		
		DateDimension dim = (DateDimension)axis.getDimension();
		
		return dim.getUnit() == DateUnit.MONTH;
	}
	
	protected boolean isYearAxis(Axis axis) {
		if(axis == null)
			return false;
		if(axis.getDimension() == null)
			return false;
		if(!(axis.getDimension() instanceof DateDimension))
			return false;
		
		DateDimension dim = (DateDimension)axis.getDimension();
		
		return dim.getUnit() == DateUnit.YEAR;
	}

    @Override
    public void complete(Axis axis) {

        if(!axis.isLeaf()) {
            Collection<Axis> children = axis.getChildren();
            Axis firstChild = children.iterator().next();

            if(firstChild.getDimension() instanceof DateDimension) {
                DateDimension dim = (DateDimension) firstChild.getDimension();

                if(dim.getUnit() == DateUnit.YEAR) {

                    completeYears(axis, dim);

                } else if(dim.getUnit() == DateUnit.MONTH) {



                }

            }
        }

    }

    protected void completeYears(Axis axis, DateDimension dim) {

        Integer firstYear = ((YearCategory)axis.firstChild().getCategory()).getYear();
        Integer lastYear = ((YearCategory)axis.lastChild().getCategory()).getYear();

        for(int y = firstYear+1; y<lastYear; ++y) {

            YearCategory category = new YearCategory(y);

            if(axis.getChild(category) == null) {
                axis.addChild(dim, category, Integer.toString(y), null);
            }
        }
    }


	

	
}
