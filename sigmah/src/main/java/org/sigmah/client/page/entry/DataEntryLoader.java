/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.callback.Got;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.page.*;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.PartnerFilterPanel;
import org.sigmah.client.page.common.nav.NavigationPanel;
import org.sigmah.client.page.common.widget.VSplitFilteredFrameSet;
import org.sigmah.client.page.common.widget.VSplitFrameSet;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.SchemaDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DataEntryLoader implements PageLoader {

    private final AppInjector injector;
    private AdminFilterPanel adminPanel;
    private DateRangePanel datePanel;
    private PartnerFilterPanel partnerPanel;   

    @Inject
    public DataEntryLoader(AppInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Frames.DataEntryFrameSet, this);
        pageManager.registerPageLoader(SiteEditor.ID, this);
        placeSerializer.registerParser(SiteEditor.ID, new SiteGridPageState.Parser());
        
        adminPanel = new AdminFilterPanel(injector.getService());
        adminPanel.setHeading(I18N.CONSTANTS.filterByGeography()); 
        adminPanel.setIcon(IconImageBundle.ICONS.filter()); 	
		
        datePanel = new DateRangePanel();
        partnerPanel = new PartnerFilterPanel(injector.getService());
    }

    @Override
    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {

        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onSuccess() {
                if (Frames.DataEntryFrameSet.equals(pageId)) {
                    loadFrame(pageState, callback);
                } else if (SiteEditor.ID.equals(pageId)) {
                    loadSiteGrid(pageState, callback);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

                callback.onFailure(throwable);
            }
        });

    }

    private void loadFrame(PageState place, AsyncCallback<Page> callback) {

        NavigationPanel navPanel = new NavigationPanel(injector.getEventBus(),
                injector.getDataEntryNavigator());
    
       
        VSplitFilteredFrameSet frameSet = new VSplitFilteredFrameSet(Frames.DataEntryFrameSet, navPanel);
        frameSet.addFilterPanel(adminPanel);
        frameSet.addFilterPanel(datePanel);
        frameSet.addFilterPanel(partnerPanel);
        callback.onSuccess(frameSet);
    }

    protected void loadSiteGrid(final PageState place, final AsyncCallback<Page> callback) {
    
        injector.getService().execute(new GetSchema(), null, new Got<SchemaDTO>() {
        	
            @Override
            public void got(SchemaDTO schema) {
                SiteGridPageState sgPlace = (SiteGridPageState) place;
                if (sgPlace.getActivityId() == 0) {
                    sgPlace.setActivityId(schema.getFirstActivity().getId());
                }

                ActivityDTO activity = schema.getActivityById(sgPlace.getActivityId());

                SiteGridPage grid = new SiteGridPage(true, adminPanel, datePanel, partnerPanel);
                SiteEditor editor = new SiteEditor(injector.getEventBus(), injector.getService(),
                        injector.getStateManager(), grid);

                if (activity.getReportingFrequency() == ActivityDTO.REPORT_MONTHLY) {
                    MonthlyGrid monthlyGrid = new MonthlyGrid(activity);
                    MonthlyTab monthlyTab = new MonthlyTab(monthlyGrid);
                    MonthlyPresenter monthlyPresenter = new MonthlyPresenter(
                            injector.getEventBus(),
                            injector.getService(),
                            injector.getStateManager(),
                            activity, monthlyGrid);
                    editor.addSubComponent(monthlyPresenter);
                    grid.addSouthTab(monthlyTab);
                } else {

                    DetailsTab detailsTab = new DetailsTab();
                    DetailsPresenter detailsPresenter = new DetailsPresenter(
                            injector.getEventBus(),
                            activity,
                            injector.getMessages(),
                            detailsTab);
                    grid.addSouthTab(detailsTab);
                    editor.addSubComponent(detailsPresenter);
                }
                //  if(Maps.isLoaded()) {     load the maps api on render in SiteMap
                SiteMap map = new SiteMap(injector.getEventBus(), injector.getService(),
                        activity);
                editor.addSubComponent(map);
                grid.addSidePanel(I18N.CONSTANTS.map(), IconImageBundle.ICONS.map(), map);

                //  }
                editor.go((SiteGridPageState) place, activity);
                callback.onSuccess(editor);

            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

}
