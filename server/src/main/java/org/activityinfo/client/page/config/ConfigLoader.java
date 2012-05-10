/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.config;

import java.util.Map;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateSerializer;
import org.activityinfo.client.page.config.design.DesignPresenter;
import org.activityinfo.client.page.config.link.IndicatorLinkPage;
import org.activityinfo.client.page.config.link.IndicatorLinkPlace;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.SchemaDTO;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ConfigLoader implements PageLoader {

    private final Dispatcher dispatch;
    private Map<PageId, Provider<? extends Page>> pageProviders = Maps.newHashMap();
	private NavigationHandler navigationHandler;

    @Inject
    public ConfigLoader(
    		Dispatcher dispatcher,
    		Provider<ConfigFrameSet> frameSet,
    		Provider<DbConfigPresenter> databaseConfigPage,
    		Provider<DbListPage> databaseListPage,
    		Provider<DbUserEditor> userPage,
    		Provider<DbPartnerEditor> partnerPage,
    		Provider<DbProjectEditor> projectPage,
    		Provider<LockedPeriodsPresenter> lockPage,
    		Provider<DesignPresenter> designPage,
    		Provider<DbTargetEditor> targetPage,
    		Provider<IndicatorLinkPage> linkPage,
    		NavigationHandler navigationHandler, 
    		PageStateSerializer placeSerializer) {
        
    	this.dispatch = dispatcher;
    	this.navigationHandler = navigationHandler;
    	

    	register(ConfigFrameSet.PAGE_ID, frameSet);
        register(DbConfigPresenter.PAGE_ID, databaseConfigPage);
        register(DbListPresenter.PAGE_ID, databaseListPage);
        register(DbUserEditor.PAGE_ID, userPage);
        register(DbPartnerEditor.PAGE_ID, partnerPage);
        register(DbProjectEditor.PAGE_ID, projectPage);
        register(LockedPeriodsPresenter.PAGE_ID, lockPage);
        register(DesignPresenter.PAGE_ID, designPage);
        register(DbTargetEditor.PAGE_ID, targetPage);
        register(IndicatorLinkPage.PAGE_ID, linkPage);

        placeSerializer.registerStatelessPlace(DbListPresenter.PAGE_ID, new DbListPageState());
        placeSerializer.registerParser(DbConfigPresenter.PAGE_ID, new DbPageState.Parser(DbConfigPresenter.PAGE_ID));
        placeSerializer.registerParser(DbUserEditor.PAGE_ID, new DbPageState.Parser(DbUserEditor.PAGE_ID));
        placeSerializer.registerParser(DbPartnerEditor.PAGE_ID, new DbPageState.Parser(DbPartnerEditor.PAGE_ID));
        placeSerializer.registerParser(DbProjectEditor.PAGE_ID, new DbPageState.Parser(DbProjectEditor.PAGE_ID));
        placeSerializer.registerParser(LockedPeriodsPresenter.PAGE_ID, new DbPageState.Parser(LockedPeriodsPresenter.PAGE_ID));
        placeSerializer.registerParser(DesignPresenter.PAGE_ID, new DbPageState.Parser(DesignPresenter.PAGE_ID));
        placeSerializer.registerParser(DbTargetEditor.PAGE_ID, new DbPageState.Parser(DbTargetEditor.PAGE_ID));
        placeSerializer.registerStatelessPlace(IndicatorLinkPage.PAGE_ID, new IndicatorLinkPlace());
    }


	private void register(PageId pageId,
			Provider<? extends Page> provider) {

		navigationHandler.registerPageLoader(pageId, this);
		pageProviders.put(pageId, provider);
	}


	@Override
    public void load(final PageId pageId, final PageState place, final AsyncCallback<Page> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
            @Override
            public void onSuccess() {
            	
            	final Page page = pageProviders.get(pageId).get();
            	
            	if(page == null) {
                    callback.onFailure(new Exception("ConfigLoader didn't know how to handle " + place.toString()));
            	} else if(page instanceof DbPage) {
            		dispatch.execute(new GetSchema(), null, new AsyncCallback<SchemaDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onFailure(caught);
						}

						@Override
						public void onSuccess(SchemaDTO result) {
		            		DbPageState dbPlace = (DbPageState)place;
		            		((DbPage)page).go(result.getDatabaseById(dbPlace.getDatabaseId()));
		            		callback.onSuccess(page);
						}
            			
            		});
            	} else {
            		page.navigate(place);
            		callback.onSuccess(page);
            	}
            }
        });

    }
}
