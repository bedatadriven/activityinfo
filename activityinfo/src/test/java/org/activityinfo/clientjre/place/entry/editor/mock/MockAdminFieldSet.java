package org.activityinfo.clientjre.place.entry.editor.mock;

import com.extjs.gxt.ui.client.store.ListStore;
import org.activityinfo.client.page.entry.editor.AdminFieldSetPresenter;
import org.activityinfo.shared.dto.AdminEntityModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockAdminFieldSet implements AdminFieldSetPresenter.View {

    AdminFieldSetPresenter presenter;

    Map<Integer, AdminEntityModel> values =
            new HashMap<Integer, AdminEntityModel>();
    Set<Integer> enabled =
            new HashSet<Integer>();
    Map<Integer, ListStore<AdminEntityModel>> stores =
            new HashMap<Integer, ListStore<AdminEntityModel>>();

    @Override
    public void bindPresenter(AdminFieldSetPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setStore(int levelId, ListStore<AdminEntityModel> store) {
        stores.put(levelId, store);
    }

    public ListStore<AdminEntityModel> getStore(int levelId) {
        return stores.get(levelId);
    }

    @Override
    public void setEnabled(int levelId, boolean value) {
        if(value) {
            enabled.add(levelId);
        } else {
            enabled.remove(levelId);
        }
    }

    public boolean getEnabled(int levelId) {
        return enabled.contains(levelId);

    }

    @Override
    public void setValue(int levelId, AdminEntityModel value) {
        values.put(levelId, value);
        System.out.println("level " + levelId + " set to " +  (value == null ? "null" : value.getName()));
    }

    public void setValueAndFire(int levelId, AdminEntityModel value) {
        setValue(levelId, value);
        presenter.onSelectionChanged(levelId, value);
    }

    public AdminEntityModel getValue(int levelId) {
        return values.get(levelId);

    }

}
