/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.entry.place.DataEntryPlaceParser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DataEntryLoader implements PageLoader {
    private final Provider<DataEntryPage> dataEntryPageProvider;
    
    @Inject
    public DataEntryLoader(
    		NavigationHandler pageManager, 
    		PageStateSerializer placeSerializer,
    		Provider<DataEntryPage> dataEntryPageProvider) {
    	
        this.dataEntryPageProvider = dataEntryPageProvider;
        
        pageManager.registerPageLoader(DataEntryPage.PAGE_ID, this);       
        placeSerializer.registerParser(DataEntryPage.PAGE_ID, new DataEntryPlaceParser());
    }

    @Override
    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onSuccess() {
                DataEntryPage dataEntryPage = dataEntryPageProvider.get();
                dataEntryPage.navigate(pageState);
				callback.onSuccess(dataEntryPage);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    protected void loadSiteGrid(final PageState place, final AsyncCallback<Page> callback) {
//				SitePageState sitePlace = (SitePageState) place;
//            	if (sitePlace != null) {
//                	setDefaultActivityId(sitePlace, schema);
//            	}
//                ActivityDTO activity = schema.getActivityById(sitePlace.getActivityId());
//
//                AbstractSiteGrid abstractSiteGrid = null;
//                AbstractSiteEditor abstractSiteEditor = null;
//                SiteEditor siteEditor = null;
//                SiteTreeEditor siteTreeEditor = null;
//                
//                if (place instanceof DataEntryPageState) {
//                	SiteGrid grid = new SiteGrid(true);
//                	siteEditor = new SiteEditor(injector.getEventBus(), 
//                			injector.getService(),
//                			injector.getStateManager(), 
//                			grid);
//                	abstractSiteGrid = grid;
//                	abstractSiteEditor = siteEditor;
//                }
//                
//                if (place instanceof SiteTreeGridPageState) {
//                	SiteTreeGrid grid = new SiteTreeGrid(true);
//                	siteTreeEditor = new SiteTreeEditor(injector.getEventBus(), 
//                			injector.getService(),
//                			injector.getStateManager(),
//                			grid);
//                	abstractSiteGrid = grid;
//                	abstractSiteEditor = siteTreeEditor;
//                }
//            	abstractSiteEditor.bindFilterPanel(filterPanelSet);
//
//                addDetailsOrReportingTabs(activity, abstractSiteGrid, abstractSiteEditor);
//                
//                SiteMap map = new SiteMap(injector.getEventBus(), injector.getService(), activity);
//                abstractSiteEditor.addSubComponent(map);
//                abstractSiteGrid.addSidePanel(I18N.CONSTANTS.map(), IconImageBundle.ICONS.map(), map);
//
//                if (siteEditor != null) {
//                	siteEditor.go((DataEntryPageState) place, activity);
//                } else if (siteTreeEditor != null) {
//                	siteTreeEditor.go((SiteTreeGridPageState) place, activity);
//                }
            	

   //}
           

//			private void setDefaultActivityId(SitePageState sitePlace, SchemaDTO schema) {
//                if (sitePlace.getActivityId() == 0) {
//                	if (schema.getFirstActivity() != null) {
//                		sitePlace.setActivityId(schema.getFirstActivity().getId());
//                	}
//                }
//			}
//
//			private void addDetailsOrReportingTabs(ActivityDTO activity, AbstractSiteGrid abstractSiteGrid, AbstractSiteEditor editor) {
//				if (activity.getReportingFrequency() == ActivityDTO.REPORT_MONTHLY) {
//                    MonthlyGrid monthlyGrid = new MonthlyGrid(activity);
//                    MonthlyTab monthlyTab = new MonthlyTab(monthlyGrid);
//                    MonthlyPresenter monthlyPresenter = new MonthlyPresenter(
//                            injector.getEventBus(),
//                            injector.getService(),
//                            injector.getStateManager(),
//                            activity, monthlyGrid);
//                    editor.addSubComponent(monthlyPresenter);
//                    abstractSiteGrid.addSouthTab(monthlyTab);
//                } else {
//                    DetailsTab detailsTab = new DetailsTab();
//                    DetailsPresenter detailsPresenter = new DetailsPresenter(
//                            injector.getEventBus(),
//                            activity,
//                            injector.getMessages(),
//                            detailsTab);
//                    abstractSiteGrid.addSouthTab(detailsTab);
//                    editor.addSubComponent(detailsPresenter);
//                }
//			}

    }

}
