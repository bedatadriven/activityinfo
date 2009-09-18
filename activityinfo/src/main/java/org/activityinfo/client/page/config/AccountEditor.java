package org.activityinfo.client.page.config;

import org.activityinfo.client.Place;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;

import com.google.inject.Inject;
import com.google.inject.ImplementedBy;

public class AccountEditor implements PagePresenter {

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
		return Pages.Account;
	}

	@Override
	public Object getWidget() {
		return view;
	}

    @Override
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public void shutdown() {

    }

    public boolean navigate(Place place) {
        return false;
    }
}
