package org.sigmah.client.page;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.inject.Root;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  Coordinates navigation between pages.
 *
 *  PageManager listens for NavigationEvents, fired either by an individual component, or
 * from the HistoryManager, and
 */
@Singleton
public class NavigationHandler {

    private final EventBus eventBus;
    private final Frame root;
    private final Map<PageId, PageLoader> pageLoaders = new HashMap<PageId, PageLoader>();

    public static final EventType NavigationRequested = new EventBus.NamedEventType("NavigationRequested");
    public static final EventType NavigationAgreed = new EventBus.NamedEventType("NavigationAgreed");

    private NavigationAttempt activeNavigation;

    @Inject
    public NavigationHandler(final EventBus eventBus, final @Root Frame root) {
        this.eventBus = eventBus;
        this.root = root;

        eventBus.addListener(NavigationRequested, new Listener<NavigationEvent>() {
            @Override
            public void handleEvent(NavigationEvent be) {
                onNavigationRequested(be);
            }
        });
        Log.debug("PageManager: connected to EventBus and listening.");
    }

    private void onNavigationRequested(NavigationEvent be) {
        if(activeNavigation ==null || !activeNavigation.getPlace().equals(be.getPlace())) {
            activeNavigation = new NavigationAttempt(be.getPlace());
            activeNavigation.go();
        }
    }

    public void registerPageLoader(PageId pageId, PageLoader loader) {
        pageLoaders.put(pageId, loader);
        Log.debug("PageManager: Registered loader for pageId '" + pageId + "'");
    }

    private PageLoader getPageLoader(PageId pageId) {
        PageLoader loader = pageLoaders.get(pageId);
        if (loader == null) {
            throw new Error("PageManager: no loader for " + pageId);
        }
        return loader;
    }


    /**
     * Encapsulates a single navigation attempt.
     */
    public class NavigationAttempt {
        private final PageState place;

        private Iterator<PageId> pageHierarchyIt;
        private Frame frame;
        private Page currentPage;
        private PageId targetPage;

        private AsyncMonitor loadingPlaceHolder;

        public NavigationAttempt(PageState place) {
            this.place = place;
        }

        public PageState getPlace() {
            return place;
        }

        public void go() {
            startAtRoot();
            confirmPageChange();
        }

        private void startAtRoot() {
            assertViewPathIsNotEmpty();
            pageHierarchyIt = place.getEnclosingFrames().iterator();
            currentPage = root;
            descend();
        }

        private void descend() {
            assertPageIsFrame(currentPage);
            frame = (Frame) currentPage;
            targetPage = pageHierarchyIt.next();
            currentPage = frame.getActivePage();
        }


        /**
         * After each asynchronous call we need to check that the user has not
         * requested to navigate elsewhere.
         *
         * For example, a page loader may make an asynchronous call, which means that an additional
         * JavaScript fragment has to be downloaded from the server and parsed before we can continue.
         * During that time, the user may have grown tired of waiting and hit the back button or
         * chosen another place to go to.
         */
        private boolean isStillActive() {
            return this == activeNavigation;
        }

        protected void confirmPageChange() {
            if (thereIsNoCurrentPage()) {
                proceedWithNavigation();

            } else if (targetPageIsAlreadyActive()) {
                // ok, no change required.
                // descend if necessary

                if (hasChildPage()) {
                    descend();
                    confirmPageChange();
                } else {
                    proceedWithNavigation();
                }
            } else {
                askPermissionToChange();
            }
        }

        /**
         * We need to give the current page an opportunity to cancel the navigation.
         * For example, the user may have made changes to the page and we don't want
         * to navigate away until we're sure that they can be saved.
         */
        private void askPermissionToChange() {
            currentPage.requestToNavigateAway(place, new NavigationCallback() {
                @Override
                public void onDecided(boolean allowed) {
                    if (allowed) {
                        if(isStillActive()) {
                            proceedWithNavigation();
                        }
                    } else {
                        Log.debug("Navigation to '" + place.toString() + "' refused by " + currentPage.toString());
                    }
                }
            });
        }

        private boolean hasChildPage() {
            return pageHierarchyIt.hasNext();
        }

        private boolean targetPageIsAlreadyActive() {
            return currentPage.getPageId().equals(targetPage);
        }

        private boolean thereIsNoCurrentPage() {
            return currentPage == null;
        }

        private void proceedWithNavigation() {
            if(isStillActive()) {
                fireAgreedEvent();
                startAtRoot();
                changePage();
            }
        }

        private void fireAgreedEvent() {
            eventBus.fireEvent(new NavigationEvent(NavigationAgreed, place));
        }

        protected void changePage() {
            /*
               * First see if this view is already the active view,
               * in wehich case we can just descend in the path
               */
            if (!thereIsNoCurrentPage() &&
                 targetPageIsAlreadyActive() &&
                  currentPage.navigate(place)) {

                changeChildPageIfNecessary();

            } else {
                shutDownCurrentPageIfThereIsOne();
                showPlaceHolder();
                schedulePageLoadAfterEventProcessing();
            }
        }

        private void shutDownCurrentPageIfThereIsOne() {
            if (!thereIsNoCurrentPage()) {
                currentPage.shutdown();
            }
        }

        private void showPlaceHolder() {
            loadingPlaceHolder = frame.showLoadingPlaceHolder(targetPage, place);
        }

        /**
         * Schedules the loadPage() after all UI events in the browser have had
         * a chance to run. This assures that the loading placeholder has a chance to
         * be added to the page.
         */
        private void schedulePageLoadAfterEventProcessing() {
            if(GWT.isClient()) {
                DeferredCommand.addCommand(new Command() {
                    @Override
                    public void execute() {
                        if(isStillActive()) {
                            loadPage();
                        }
                    }
                });
            } else {
                loadPage();
            }
        }

        /**
         * Delegates the creation of the Page component to a registered
         * page loader.
         */
        private void loadPage() {
            PageLoader loader = getPageLoader(targetPage);
            loader.load(targetPage, place, new AsyncCallback<Page>() {
                @Override
                public void onFailure(Throwable caught) {
                    onPageFailedToLoad(caught);
                }
                @Override
                public void onSuccess(Page page) {
                    if(isStillActive()) {
                        onPageLoaded(page);
                    }
                }
            });
        }

        private void onPageFailedToLoad(Throwable caught) {
            loadingPlaceHolder.onConnectionProblem();
            Log.error("PageManager: could not load page " + targetPage, caught);
        }

        private void onPageLoaded(Page page) {
            makeCurrent(page);
            changeChildPageIfNecessary();
        }

        private void makeCurrent(Page page) {
            frame.setActivePage(page);
            currentPage = page;
        }

        private void changeChildPageIfNecessary() {
            if (hasChildPage()) {
                descend();
                changePage();
            }
        }

        private void assertViewPathIsNotEmpty() {
            assert place.getEnclosingFrames().size() != 0 : "PageState " + place.toString() + " has an empty viewPath!";
        }

        private void assertPageIsFrame(Page page) {
            assert page instanceof Frame :
                    "Cannot load page " + pageHierarchyIt.next() + " into " + page.toString() + " because " +
                            page.getClass().getName() + " does not implement the PageFrame interface.";
        }
    }
}
