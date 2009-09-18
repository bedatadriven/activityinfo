package org.activityinfo.clientjre.place.entry;

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.event.SiteEvent;
import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.page.entry.DetailsPresenter;
import org.activityinfo.clientjre.mock.MockEventBus;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.SiteModel;
import org.activityinfo.shared.i18n.UIConstants;
import org.junit.Test;
import static org.easymock.EasyMock.*;

public class DetailsTest {

    @Test
    public void testSiteSelected() {

        // Testing Data
        Schema schema = DummyData.PEAR();
        ActivityModel activity = schema.getActivityById(91);
        SiteModel site = DummyData.PEAR_Sites().get(4);

        // Collaborator: EventBus
        MockEventBus eventBus = new MockEventBus();


        // Collaborator: View
        DetailsPresenter.View view = createNiceMock(DetailsPresenter.View.class);
        view.setHtml(isA(String.class));
        replay(view);

        // Collbaroator: messages
        UIConstants messages = createNiceMock(UIConstants.class);
        replay(messages);

        // Class under test
        DetailsPresenter presenter = new DetailsPresenter(eventBus, activity, messages, view);

        // VERIFY that when a site is selected, the html is set
        eventBus.fireEvent(new SiteEvent(AppEvents.SiteSelected, this, site));

        verify(view);

    }
}
