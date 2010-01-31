package org.activityinfo.clientjre;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.Place;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.dispatch.monitor.NullAsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.*;
import org.activityinfo.clientjre.mock.MockEventBus;
import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class PageManagerTest {


    private MockEventBus eventBus = new MockEventBus();

    private FrameSetPresenter rootFrameSet = createMock("rootFrameSet", FrameSetPresenter.class);

    private FrameSetId outerFrameSetId = new FrameSetId("outerFrameSet");
    private FrameSetPresenter outerFrameSet = createMock("outerFrameSet", FrameSetPresenter.class);

    private PageId firstPageId = new PageId("page1");
    private PagePresenter firstPage = createMock("firstPage", FrameSetPresenter.class);

    private PageId secondPageId = new PageId("page2");
    private PagePresenter secondPage = createMock("secondPage", PagePresenter.class);

    private PageLoader pageLoader = createMock("pageLoader", PageLoader.class);

    private Place innerPlaceWithOuterFrame = new MockPlace(firstPageId, ViewPath.make(outerFrameSetId, firstPageId));
    private Place secondInnerPlaceWithSameOuterFrame = new MockPlace(secondPageId, ViewPath.make(outerFrameSetId, secondPageId));

    private Place firstPlace = new MockPlace(firstPageId, ViewPath.make(firstPageId));
    private Place secondPlace = new MockPlace(secondPageId, ViewPath.make(secondPageId));

    private PageManager pageManager;


    @Before
    public void setUp() {

        expect(outerFrameSet.getPageId()).andReturn(outerFrameSetId).anyTimes();
        expect(firstPage.getPageId()).andReturn(firstPageId).anyTimes();

        pageManager = new PageManager(eventBus, rootFrameSet );
    }

    @Test
    public void verifyThatAPageWithOneOuterContainerIsCorrectlyLoaded() {

        thereIsNoActivePageIn(rootFrameSet);
        expectLoadingPlaceHolderToBeShown(rootFrameSet);
        expectAndCaptureLoadRequestFor(outerFrameSetId);
        expectPageToBeSetTo(rootFrameSet, outerFrameSet);
        thereIsNoActivePageIn(outerFrameSet);
        expectLoadingPlaceHolderToBeShown(outerFrameSet);
        expectAndCaptureLoadRequestFor(firstPageId);
        expectPageToBeSetTo(outerFrameSet, firstPage);

        replayAll();

        pageManager.registerPageLoader(outerFrameSetId, pageLoader);
        pageManager.registerPageLoader(firstPageId, pageLoader);

        requestNavigationTo(innerPlaceWithOuterFrame);
        pageLoadFinishes(outerFrameSetId, outerFrameSet);
        pageLoadFinishes(firstPageId, firstPage);

        verify(rootFrameSet, outerFrameSet, firstPage, pageLoader);
    }


    @Test
    public void innerPageCanCancelNavigation() {
        theRootPageIs(outerFrameSet);
        theOuterFrameSetIsShowing(firstPage);

        navigationRequestWillBeRefusedBy(firstPage);

        replayAll();

        requestNavigationTo(secondInnerPlaceWithSameOuterFrame);

        verify(outerFrameSet, firstPage);

        eventBus.assertNotFired(PageManager.NavigationAgreed);
    }

    @Test
    public void navigationProceedsUponApprovalOfInnerPage() {

        theRootPageIs(outerFrameSet);
        theOuterFrameSetIsShowing(firstPage);

        expectAndAgreeToNavigationRequestionOn(firstPage);
        expectNavigate(outerFrameSet);
        expectLoadingPlaceHolderToBeShown(outerFrameSet);
        expectAndCaptureLoadRequestFor(secondPageId);
        expectShutdownCallTo(firstPage);
        expectPageToBeSetTo(outerFrameSet, secondPage);

        replayAll();

        pageManager.registerPageLoader(secondPageId, pageLoader);
        requestNavigationTo(secondInnerPlaceWithSameOuterFrame);
        pageLoadFinishes(secondPageId, secondPage);
        
        verify(outerFrameSet, firstPage, pageLoader);

        Assert.assertEquals(1, eventBus.getEventCount(PageManager.NavigationAgreed));
    }

    @Test
    public void userCanNavigateToDifferentPageWhilePreviousRequestIsLoading() {

        thereIsNoActivePageIn(rootFrameSet);
        expectLoadingPlaceHolderToBeShownTwiceIn(rootFrameSet);
        expectAndCaptureLoadRequestFor(firstPageId);
        expectAndCaptureLoadRequestFor(secondPageId);
        expectPageToBeSetTo(rootFrameSet, secondPage);

        replayAll();

        pageManager.registerPageLoader(firstPageId, pageLoader);
        pageManager.registerPageLoader(secondPageId, pageLoader);
        requestNavigationTo(firstPlace);
        requestNavigationTo(secondPlace);
        pageLoadFinishes(secondPageId, secondPage);
        pageLoadFinishes(firstPageId, firstPage);

        verify(rootFrameSet, pageLoader);
    }


    private class MockPlace implements Place {
        private final PageId pageId;
        private final List<ViewPath.Node> viewPath;

        private MockPlace(PageId pageId, List<ViewPath.Node> viewPath) {
            this.pageId = pageId;
            this.viewPath = viewPath;
        }
        public PageId getPageId() { return pageId;  }
        public String pageStateToken() { return null; }
        public List<ViewPath.Node> getViewPath() { return viewPath; }
    }

    @Test
    public void duplicateNavigationRequestsAreIgnored() {
        thereIsNoActivePageIn(rootFrameSet);
        expectLoadingPlaceHolderToBeShown(rootFrameSet);
        expectAndCaptureLoadRequestFor(firstPageId);
        expectPageToBeSetTo(rootFrameSet, firstPage);

        replayAll();

        pageManager.registerPageLoader(firstPageId, pageLoader);
        requestNavigationTo(firstPlace);
        requestNavigationTo(firstPlace);
        pageLoadFinishes(firstPageId, firstPage);

        verify(rootFrameSet, pageLoader, firstPage);
    }


    private void theRootPageIs(PagePresenter page) {
        expect(rootFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(page).anyTimes();
    }

    private void theOuterFrameSetIsShowing(PagePresenter page) {
        expect(outerFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(page).anyTimes();
    }

    private void requestNavigationTo(Place place) {
        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, place));
    }

    private void thereIsNoActivePageIn(FrameSetPresenter rootFrameSet) {
        expect(rootFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(null).anyTimes();
    }

    private void expectShutdownCallTo(PagePresenter page) {
        page.shutdown();
    }

    private void expectNavigate(FrameSetPresenter frameSet) {
        expect(frameSet.navigate(isA(Place.class))).andReturn(true);
    }

    private Map<PageId, Capture<AsyncCallback<PagePresenter>>> loadCallbacks = new HashMap();

    private void expectAndCaptureLoadRequestFor(PageId pageId) {
        Capture<AsyncCallback<PagePresenter>> capture = new Capture<AsyncCallback<PagePresenter>>();
        loadCallbacks.put(pageId, capture);

        pageLoader.load(eq(pageId), isA(Place.class), capture(capture));
        expectLastCall().anyTimes();
    }

    private void pageLoadFinishes(PageId pageId, PagePresenter page) {
        loadCallbacks.get(pageId).getValue().onSuccess(page);
    }

    private void expectPageToBeSetTo(FrameSetPresenter frameSet, PagePresenter page) {
        frameSet.setActivePage(ViewPath.DefaultRegion, page);
    }

    private void expectLoadingPlaceHolderToBeShown(FrameSetPresenter frame) {
        expect(frame.showLoadingPlaceHolder(eq(ViewPath.DefaultRegion), isA(PageId.class), isA(Place.class)))
                .andReturn(new NullAsyncMonitor());
    }

    private void expectLoadingPlaceHolderToBeShownTwiceIn(FrameSetPresenter frame) {
        expect(frame.showLoadingPlaceHolder(eq(ViewPath.DefaultRegion), isA(PageId.class), isA(Place.class)))
                .andReturn(new NullAsyncMonitor())
                .times(2);
    }

    private void navigationRequestWillBeRefusedBy(PagePresenter page) {
        navigationRequestWillBeAnsweredBy(page, false);
    }

    private void expectAndAgreeToNavigationRequestionOn(PagePresenter page) {
        navigationRequestWillBeAnsweredBy(page, true);
    }

    private void navigationRequestWillBeAnsweredBy(PagePresenter page, final boolean allowed) {
        page.requestToNavigateAway(isA(Place.class), isA(NavigationCallback.class));
        expectLastCall().andAnswer(new IAnswer<Void>() {
            @Override
            public Void answer() throws Throwable {
                ((NavigationCallback)(getCurrentArguments()[1])).onDecided(allowed);
                return null;
            }
        });
    }

    private void replayAll() {
        replay(rootFrameSet);
        replay(pageLoader);
        replay(outerFrameSet);
        replay(firstPage);
        replay(secondPage);
    }

}
