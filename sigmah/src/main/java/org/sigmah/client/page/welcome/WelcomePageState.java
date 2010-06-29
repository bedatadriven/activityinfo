package org.sigmah.client.page.welcome;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import java.util.Arrays;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class WelcomePageState implements PageState {

    public PageId getPageId() {
        return Welcome.Welcome;
    }

    public String serializeAsHistoryToken() {
        return null;
    }

    public List<PageId> getEnclosingFrames() {
        return Arrays.asList(Welcome.Welcome);
    }
    
}
