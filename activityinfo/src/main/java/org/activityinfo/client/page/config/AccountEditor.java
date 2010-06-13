package org.activityinfo.client.page.config;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;

public class AccountEditor implements Page {
    public static final PageId Account = new PageId("account");

    @ImplementedBy(AccountPanel.class)
    public interface View {
		
	}
	
	private final View view;

    @Inject
	public AccountEditor(View view) {
		this.view = view;
	}
	
	@Override
	public PageId getPageId() {
		return Account;
	}

	@Override
	public Object getWidget() {
		return view;
	}

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public void shutdown() {

    }

    public boolean navigate(PageState place) {
        return false;
    }
}
