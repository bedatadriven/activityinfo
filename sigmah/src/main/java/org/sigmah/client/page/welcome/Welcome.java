/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.welcome;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.charts.ChartPageState;
import org.sigmah.client.page.common.GalleryView;
import org.sigmah.client.page.entry.place.DataEntryPlace;
import org.sigmah.client.page.map.MapPageState;
import org.sigmah.client.page.table.PivotPageState;

import com.google.inject.Inject;

public class Welcome implements Page {


    private GalleryView view;
    public static final PageId PAGE_ID = new PageId("welcome");

    @Inject
    public Welcome(GalleryView view) {

        this.view = view;
        this.view.setHeading(I18N.CONSTANTS.welcomeMessage());
        this.view.setIntro(I18N.CONSTANTS.selectCategory());

//        this.view.add(I18N.CONSTANTS.dataEntry(), I18N.CONSTANTS.dataEntryDescription(), 
//        		"form.png", new DashboardPageState());

        this.view.add(I18N.CONSTANTS.dataEntry(), I18N.CONSTANTS.dataEntryDescription(), 
        		"form.png", new DataEntryPlace());

        this.view.add(I18N.CONSTANTS.siteLists(), I18N.CONSTANTS.siteListsDescriptions(),
                "grid.png", new DataEntryPlace());

        this.view.add(I18N.CONSTANTS.pivotTables(), I18N.CONSTANTS.pivotTableDescription(),
                "pivot.png", new PivotPageState());

        this.view.add(I18N.CONSTANTS.charts(), I18N.CONSTANTS.chartsDescription(),
                "charts/time.png", new ChartPageState());

        this.view.add(I18N.CONSTANTS.maps(), I18N.CONSTANTS.mapsDescription(),
                "map.png", new MapPageState());

    }

    @Override
	public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
	public Object getWidget() {
        return view;
    }

    @Override
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
	public String beforeWindowCloses() {
        return null;
    }

    @Override
	public void shutdown() {

    }

    @Override
	public boolean navigate(PageState place) {
        return true;
    }
}