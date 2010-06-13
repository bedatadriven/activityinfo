package org.activityinfo.client.page;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.dispatch.monitor.NullAsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.mock.MockEventBus;
import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class NavigationHandlerTest {


    private MockEventBus eventBus = new MockEventBus();

    private Frame rootFrameSet = createMock("rootFrameSet", Frame.class);

    private FrameId outerFrameSetId = new FrameId("outerFrameSet");
    private Frame outerFrameSet = createMock("outerFrameSet", Frame.class);

    private PageId firstPageId = new PageId("page1");
    private Page firstPage = createMock("firstPage", Frame.class);

    private PageId secondPageId = new PageId("page2");
    private Page secondPage = createMock("secondPage", Page.class);

    private PageLoader pageLoader = createMock("pageLoader", PageLoader.class);

    private PageState innerPlaceWithOuterFrame = new MockPageState(firstPageId, Arrays.asList(outerFrameSetId, firstPageId));
    private PageState secondInnerPlaceWithSameOuterFrame = new MockPageState(secondPageId, Arrays.asList(outerFrameSetId, secondPageId));

    private PageState firstPlace = new MockPageState(firstPageId, Arrays.asList(firstPageId));
    private PageState secondPlace = new MockPageState(secondPageId, Arrays.asList(secondPageId));

    private NavigationHandler pageManager;


    @Before
    public void setUp() {

        expect(outerFrameSet.getPageId()).andReturn(outerFrameSetId).anyTimes();
        expect(firstPage.getPageId()).andReturn(firstPageId).anyTimes();

        pageManager = new NavigationHandler(eventBus, rootFrameSet );
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

        eventBus.assertNotFired(NavigationHandler.NavigationAgreed);
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

        Assert.assertEquals(1, eventBus.getEventCount(NavigationHandler.NavigationAgreed));
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


    private class MockPageState implements PageState {
        private final PageId pageId;
        private final List<PageId> viewPath;

        private MockPageState(PageId pageId, List<PageId> viewPath) {
            this.pageId = pageId;
            this.viewPath = viewPath;
        }
        public PageId getPageId() { return pageId;  }
        public String serializeAsHistoryToken() { return null; }
        public List<PageId> getEnclosingFrames() { return viewPath; }
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


    private void theRootPageIs(Page page) {
        expect(rootFrameSet.getActivePage()).andReturn(page).anyTimes();
    }

    private void theOuterFrameSetIsShowing(Page page) {
        expect(outerFrameSet.getActivePage()).andReturn(page).anyTimes();
    }

    private void requestNavigationTo(PageState place) {
        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, place));
    }

    private void thereIsNoActivePageIn(Frame rootFrameSet) {
        expect(rootFrameSet.getActivePage()).andReturn(null).anyTimes();
    }

    private void expectShutdownCallTo(Page page) {
        page.shutdown();
    }

    private void expectNavigate(Frame frameSet) {
        expect(frameSet.navigate(isA(PageState.class))).andReturn(true);
    }

    private Map<PageId, Capture<AsyncCallback<Page>>> loadCallbacks = new HashMap();

    private void expectAndCaptureLoadRequestFor(PageId pageId) {
        Capture<AsyncCallback<Page>> capture = new Capture<AsyncCallback<Page>>();
        loadCallbacks.put(pageId, capture);

        pageLoader.load(eq(pageId), isA(PageState.class), capture(capture));
        expectLastCall().anyTimes();
    }

    private void pageLoadFinishes(PageId pageId, Page page) {
        loadCallbacks.get(pageId).getValue().onSuccess(page);
    }

    private void expectPageToBeSetTo(Frame frameSet, Page page) {
        frameSet.setActivePage(page);
    }

    private void expectLoadingPlaceHolderToBeShown(Frame frame) {
        expect(frame.showLoadingPlaceHolder(isA(PageId.class), isA(PageState.class)))
                .andReturn(new NullAsyncMonitor());
    }

    private void expectLoadingPlaceHolderToBeShownTwiceIn(Frame frame) {
        expect(frame.showLoadingPlaceHolder(isA(PageId.class), isA(PageState.class)))
                .andReturn(new NullAsyncMonitor())
                .times(2);
    }

    private void navigationRequestWillBeRefusedBy(Page page) {
        navigationRequestWillBeAnsweredBy(page, false);
    }

    private void expectAndAgreeToNavigationRequestionOn(Page page) {
        navigationRequestWillBeAnsweredBy(page, true);
    }

    private void navigationRequestWillBeAnsweredBy(Page page, final boolean allowed) {
        page.requestToNavigateAway(isA(PageState.class), isA(NavigationCallback.class));
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
