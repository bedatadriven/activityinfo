package org.activityinfo.clientjre;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.command.monitor.NullAsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.*;
import org.activityinfo.clientjre.mock.MockEventBus;
import org.activityinfo.clientjre.mock.MockTimer;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PageManagerTest {

    private static final FrameSetId OuterFrameSet = new FrameSetId("frameset");
    private static final PageId InnerPage = new PageId("page");
    private static final PageId OtherInnerPageId = new PageId("otherpage");

    private class MockLoader implements PageLoader {

        private PagePresenter payload;
        public AsyncCallback callback;

        private MockLoader() {
        }

        public MockLoader(PagePresenter payload) {
            this.payload = payload;
        }

        @Override
        public void load(PageId pageId, Place initialPlaceHint, AsyncCallback<PagePresenter> callback) {
            this.callback = callback;
            if(payload!=null)
                callback.onSuccess(payload);
        }
    }

    private class MockPlace implements Place {
        private final PageId pageId;
        private final List<ViewPath.Node> viewPath;

        private MockPlace(PageId pageId, List<ViewPath.Node> viewPath) {
            this.pageId = pageId;
            this.viewPath = viewPath;
        }

        @Override
        public PageId getPageId() {
            return pageId;
        }

        @Override
        public String pageStateToken() {
            return null;
        }

        @Override
        public List<ViewPath.Node> getViewPath() {
            return viewPath;
        }
    }

    @Test
    public void testHierachialLoad() {

        /*
           * Class under test is PageManager!
           */

        EventBus eventBus = new MockEventBus();

        PagePresenter innerPage = createMock("innerPage", FrameSetPresenter.class);
        replay(innerPage);

        // the outer frame should receive a request to see if the current page is the same, and when that's
        // not possible, a reference to the page presenter
        FrameSetPresenter outerFrameSet = createMock("outerFrameSet", FrameSetPresenter.class);
        expect(outerFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(null).anyTimes();
        outerFrameSet.showLoadingPlaceHolder(eq(ViewPath.DefaultRegion), isA(PageId.class), isA(Place.class));
        expectLastCall().andReturn(new NullAsyncMonitor());
        outerFrameSet.setActivePage(ViewPath.DefaultRegion, innerPage);
        replay(outerFrameSet);

        // the root application frame is being created and set
        FrameSetPresenter rootFrameSet = createMock("rootFrameSet", FrameSetPresenter.class);
        expect(rootFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(null).anyTimes();
        rootFrameSet.showLoadingPlaceHolder(eq(ViewPath.DefaultRegion), isA(PageId.class), isA(Place.class));
        expectLastCall().andReturn(new NullAsyncMonitor());
        rootFrameSet.setActivePage(eq(ViewPath.DefaultRegion), isA(PagePresenter.class));
        replay(rootFrameSet);

        PageManager pageMgr = new PageManager(eventBus, new MockTimer(), rootFrameSet);
        pageMgr.registerPageLoader(OuterFrameSet, new MockLoader(outerFrameSet));
        pageMgr.registerPageLoader(InnerPage, new MockLoader(innerPage));


        // VERIFY that the page is properly installed
        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                new MockPlace(InnerPage, ViewPath.make(OuterFrameSet, InnerPage))));

        verify(rootFrameSet);
        verify(outerFrameSet);
        verify(innerPage);

    }

    @Test
    public void testRequestInnerNavAway() {



        // collaborator: eventBus
        MockEventBus eventBus = new MockEventBus();


        // collaborate: inner page
        // we should expect a request to navigate away
        PagePresenter innerPage = createMock("innerPage", FrameSetPresenter.class);
        Capture<NavigationCallback> navigateAwayCallback = new Capture<NavigationCallback>();
        expect(innerPage.getPageId()).andReturn(InnerPage).anyTimes();
        innerPage.requestToNavigateAway(EasyMock.<Place>anyObject(), capture(navigateAwayCallback));
        expectLastCall().anyTimes();
        innerPage.shutdown();
        expectLastCall().anyTimes();
        replay(innerPage);

        // collaborator: outer frame
        FrameSetPresenter outerFrameSet = createMock("outerFrameSet", FrameSetPresenter.class);
        expect(outerFrameSet.getPageId()).andReturn(OuterFrameSet).anyTimes();
        expect(outerFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(innerPage).anyTimes();
        replay(outerFrameSet);

        // the root application frame is being created and set
        FrameSetPresenter rootFrameSet = createMock("rootFrameSet", FrameSetPresenter.class);
        expect(rootFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(outerFrameSet).anyTimes();
        replay(rootFrameSet);

        // CLASS UNDER TEST  !!!
        PageManager pageMgr = new PageManager(eventBus,  new MockTimer(), rootFrameSet);
        pageMgr.registerPageLoader(OuterFrameSet, new MockLoader(outerFrameSet));
        pageMgr.registerPageLoader(InnerPage, new MockLoader(innerPage));


        // VERIFY that the inner page can cancel the navigation
        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                new MockPlace(OtherInnerPageId, ViewPath.make(OuterFrameSet, OtherInnerPageId))));

        verify(rootFrameSet);
        verify(outerFrameSet);
        verify(innerPage);

        navigateAwayCallback.getValue().onDecided(false);

        Assert.assertEquals(0, eventBus.getEventCount(PageManager.NavigationAgreed));

        // VERIFY that upon approval from inner page, the NavigationAgreed event is fired


        reset(outerFrameSet);
        expect(outerFrameSet.getPageId()).andReturn(OuterFrameSet).anyTimes();
        expect(outerFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(innerPage).anyTimes();
        
        outerFrameSet.showLoadingPlaceHolder(eq(ViewPath.DefaultRegion), isA(PageId.class), isA(Place.class));
        expectLastCall().andReturn(new NullAsyncMonitor());
        expect(outerFrameSet.navigate(isA(Place.class))).andReturn(true);
        replay(outerFrameSet);

        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                new MockPlace(OtherInnerPageId, ViewPath.make(OuterFrameSet, OtherInnerPageId))));

        navigateAwayCallback.getValue().onDecided(true);

        Assert.assertEquals(1, eventBus.getEventCount(PageManager.NavigationAgreed));


    }


    @Test
    public void testSequence() {

        // collaborator: eventBus
        MockEventBus eventBus = new MockEventBus();

        // collaborator: first page requested
        PagePresenter firstPage = createMock("firstPage", PagePresenter.class);
        expect(firstPage.getPageId()).andReturn(InnerPage).anyTimes();
        replay(firstPage);

        MockLoader firstLoader = new MockLoader(); // don't call back immediately

        // collaborator: second page requested
        PagePresenter secondPage = createMock("secondPage", PagePresenter.class);
        expect(secondPage.getPageId()).andReturn(OtherInnerPageId).anyTimes();
        replay(secondPage);

        MockLoader secondLoader = new MockLoader();

        // the root application frame is being created and set
        FrameSetPresenter rootFrameSet = createMock("rootFrameSet", FrameSetPresenter.class);
        expect(rootFrameSet.getActivePage(ViewPath.DefaultRegion)).andReturn(null).anyTimes();
        expect(rootFrameSet.showLoadingPlaceHolder(eq(ViewPath.DefaultRegion), eq(InnerPage), isA(Place.class) ))
                .andReturn(new NullAsyncMonitor());
        expect(rootFrameSet.showLoadingPlaceHolder(eq(ViewPath.DefaultRegion), eq(OtherInnerPageId), isA(Place.class)))
                .andReturn(new NullAsyncMonitor());
        rootFrameSet.setActivePage(eq(ViewPath.DefaultRegion), eq(secondPage));
        replay(rootFrameSet);

        // CLASS UNDER TEST  !!!
        PageManager pageMgr = new PageManager(eventBus,  new MockTimer(), rootFrameSet);
        pageMgr.registerPageLoader(InnerPage, firstLoader);
        pageMgr.registerPageLoader(OtherInnerPageId, secondLoader);

        // VERIFY that a new navigation request cancels and supercedes an existing
        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                new MockPlace(InnerPage, ViewPath.make(InnerPage))));

        eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
                new MockPlace(OtherInnerPageId, ViewPath.make(OtherInnerPageId))));

        secondLoader.callback.onSuccess(secondPage);
        firstLoader.callback.onSuccess(firstPage);

        verify(rootFrameSet);

    }


}
