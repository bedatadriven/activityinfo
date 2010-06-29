/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

/**
 *
 * NOTE: The PlaceManager must be invoked after PageManager
 *
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
@Singleton
public class HistoryManager {

    private final EventBus eventBus;
    private final PageStateSerializer placeSerializer;


    @Inject
    public HistoryManager(EventBus eventBus, PageStateSerializer placeSerializer) {
        this.placeSerializer = placeSerializer;
        this.eventBus = eventBus;

        this.eventBus.addListener(AppEvents.Init, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                fireInitialPage();
            }
        });
        
        this.eventBus.addListener(NavigationHandler.NavigationAgreed, new Listener<NavigationEvent>() {
            @Override
            public void handleEvent(NavigationEvent be) {
                onNavigationCompleted(be.getPlace());
            }
        });

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                onBrowserMovement(event.getValue());
            }
        });

        Log.trace("HistoryManager plugged in");
    }


    private void fireInitialPage() {

        if(History.getToken().length() != 0) {
            Log.debug("HistoryManager: firing initial placed based on intial token: " + History.getToken());
            History.fireCurrentHistoryState();

//        } else if(Cookies.getCookie("lastPlace") != null) {
//
//            GWT.log("HistoryManager: firing initial placed based on cookie: " + Cookies.getCookie("lastPlace"), null);
//
//            PageState place = placeSerializer.deserialize(Cookies.getCookie("lastPlace"));
//            if(place != null) {
//
//                 eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, place));
//            } else {
//                eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new WelcomePageState()));
//            }
//        }
        } else {
            History.newItem("welcome", true);
        }
    }


    private void onNavigationCompleted(PageState place) {

        String token = placeSerializer.serialize(place);

        /*
         * If it's a duplicate, we're not totally interested
         */
        if(!token.equals(History.getToken())) {

            /*
             * Lodge in the browser's history
             */
            History.newItem(token, false);

            /*
             * ... And save as a cookie so we know where to pick up next time
             */

            Cookies.setCookie("lastPlace", token,
                    (new DateWrapper()).addDays(30).asDate() );


        }
    }
    
    private void onBrowserMovement(String token) {

        Log.debug("HistoryManager: Browser movement observed (" + token + "), firing NavigationRequested") ;
        
        PageState place = placeSerializer.deserialize(token);
        if(place != null) {

            eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, place));

        } else {
            Log.debug("HistoryManager: Could not deserialize '" + token + "', no action taken.");
        }
    }

}
