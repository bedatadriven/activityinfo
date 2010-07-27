/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.SigmahInjector;
import org.sigmah.client.page.*;

/**
 * This version of the MapLoader uses SigmahInjector instead of the AppInjector.
 * @author Alex Bertram (akbertram@gmail.com)
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahMapLoader implements PageLoader {

    private final SigmahInjector injector;

    @Inject
    public SigmahMapLoader(SigmahInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer) {
        this.injector = injector;
        
        pageManager.registerPageLoader(Maps.Maps, this);
        placeSerializer.registerStatelessPlace(Maps.Maps, new MapPageState());
    }

    @Override
    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {

                if(Maps.Maps.equals(pageId)) {

                    MapForm form= injector.getSingleMapForm();
                    MapPage page = new MapPage(form);
                    MapPresenter presenter = new MapPresenter(pageId,
                            injector.getEventBus(),
                            injector.getService(),
                            page);

                    callback.onSuccess(presenter);
                }
            }
        });

    }
}