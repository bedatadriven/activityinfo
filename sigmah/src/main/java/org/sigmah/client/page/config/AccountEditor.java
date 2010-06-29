package org.sigmah.client.page.config;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

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
