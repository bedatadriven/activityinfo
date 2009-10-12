package org.activityinfo.client.page;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.inject.Root;
import org.activityinfo.client.util.ITimer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Singleton
public class PageManager {

	private final EventBus eventBus;
	private final FrameSetPresenter root;
	private final Map<PageId, PageLoader> pageLoaders = new HashMap<PageId, PageLoader>();
    private final ITimer timer;

    public static final EventType NavigationRequested = new EventBus.NamedEventType("NavigationRequested");
    public static final EventType NavigationAgreed = new EventBus.NamedEventType("NavigationAgreed");

    private int activeRequestId = 0;

    @Inject
    public PageManager(final EventBus eventBus, ITimer timer, final @Root FrameSetPresenter root) {
		this.eventBus = eventBus;
        this.timer = timer;
        this.root = root;

		eventBus.addListener(NavigationRequested, new Listener<NavigationEvent>() {

			@Override
			public void handleEvent(NavigationEvent be) {

                // keep track of the sequence in which requests are received
                // so that don't load pages that have been superceeded by subsequent
                // requests
                activeRequestId++;
 
                recursivelyAskPagesIfItsOkToBeChanged(activeRequestId, root, be.getPlace(),
                        be.getPlace().getViewPath().iterator());

			}
		});

        GWT.log("PageManager: connected to EventBus and listening.", null);
	}


    public void registerPageLoader(PageId pageId, PageLoader loader) {
		pageLoaders.put(pageId, loader);
	}

    protected void recursivelyAskPagesIfItsOkToBeChanged(final int requestId, final FrameSetPresenter frame,
                                                    final Place place,
                                                    final Iterator<ViewPath.Node> path) {

		final ViewPath.Node node = path.next();

        final PagePresenter activePage = frame.getActivePage(node.regionId);


        if(activePage == null) {
            // ok, no problems here
            onNavigationAgreed(requestId, place);

        } else if(activePage.getPageId().equals(node.pageId)) {
            // ok, no change required.
            // descend if necessary

            if(path.hasNext()) {
                recursivelyAskPagesIfItsOkToBeChanged(requestId, (FrameSetPresenter) activePage, place, path);
            } else {

                // this is the last one, we're good to go
                if(requestId == activeRequestId)
                    onNavigationAgreed(requestId, place);
            }
        }  else {
            // need to change this page. ask permission

            activePage.requestToNavigateAway(place, new NavigationCallback() {
                @Override
                public void onDecided(boolean allowed) {
                    if(allowed) {
                        if(requestId == activeRequestId)
                            onNavigationAgreed(requestId, place);
                    } else {
                        GWT.log("Navigation to '" + place.toString() + "' refused by " + activePage.toString(), null);
                    }
                }
            });
        }
    }

    private void onNavigationAgreed(int requestId, Place place) {


        eventBus.fireEvent(new NavigationEvent(NavigationAgreed,place));

        List<ViewPath.Node> viewPath = place.getViewPath();

        if(viewPath.size() != 0) {
            FrameSetPresenter frame = root;
            recursivelyChangePages(requestId, frame, place, viewPath.iterator());
        }
    }



	protected void recursivelyChangePages(final int requestId, final FrameSetPresenter frame,  final Place place, final Iterator<ViewPath.Node> path) {

		final ViewPath.Node node = path.next();

        PagePresenter activePage = frame.getActivePage(node.regionId);


		/*
		 * First see if this view is already the active view,
		 * in wehich case we can just descend in the path
		 */
		if(activePage != null && 
           activePage.getPageId().equals(node.pageId) &&
           activePage.navigate(place)) {
			
			if(path.hasNext()) {
                recursivelyChangePages(requestId, (FrameSetPresenter)frame.getActivePage(node.regionId), place, path);
			}
			
		} else {

            if(activePage != null) {
                activePage.shutdown();
            }

            // Display a place holder, this may take a bit of time
            final AsyncMonitor placeHolder = frame.showLoadingPlaceHolder(node.regionId, node.pageId, place);

            // wrap this next call in a timer to assure that the browser
            // shows our place holder

            timer.schedule(1, new ITimer.Callback() {

                public void run() {


                    // verify that this request has not been superceeded
                    if(requestId != activeRequestId)
                        return;

                    // obtain the loader for this page

                    PageLoader loader = pageLoaders.get(node.pageId);
                    if(loader == null) {
                        GWT.log("PageManager: no loader for " + node.pageId, null);
                        return;
                    }

                    loader.load(node.pageId, place, new AsyncCallback<PagePresenter>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            placeHolder.onConnectionProblem();
                            GWT.log("PageManager: could not load page " + node.pageId, caught);
                        }

                        @Override
                        public void onSuccess(PagePresenter page) {

                            // verify that this request has not been superceeded
                            if(requestId != activeRequestId)
                                return;

                            frame.setActivePage(node.regionId, page);

                            if(path.hasNext()) {
                                assert page instanceof FrameSetPresenter :
                                        "Cannot load page " + path.next().pageId + " into " + page.toString() + " because " +
                                        page.getClass().getName() + " does not implement the PageFrame interface.";

                                recursivelyChangePages(requestId, (FrameSetPresenter)page, place, path);
                            }
                        }

                    });
                }
            });

		}	
	}
}
