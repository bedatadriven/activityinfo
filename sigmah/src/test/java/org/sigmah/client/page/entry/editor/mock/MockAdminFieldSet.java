package org.sigmah.client.page.entry.editor.mock;

import com.extjs.gxt.ui.client.store.ListStore;
import org.sigmah.client.page.entry.editor.AdminFieldSetPresenter;
import org.sigmah.shared.dto.AdminEntityDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockAdminFieldSet implements AdminFieldSetPresenter.View {

    AdminFieldSetPresenter presenter;

    Map<Integer, AdminEntityDTO> values =
            new HashMap<Integer, AdminEntityDTO>();
    Set<Integer> enabled =
            new HashSet<Integer>();
    Map<Integer, ListStore<AdminEntityDTO>> stores =
            new HashMap<Integer, ListStore<AdminEntityDTO>>();

    @Override
    public void bindPresenter(AdminFieldSetPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setStore(int levelId, ListStore<AdminEntityDTO> store) {
        stores.put(levelId, store);
    }

    public ListStore<AdminEntityDTO> getStore(int levelId) {
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
    public void setValue(int levelId, AdminEntityDTO value) {
        values.put(levelId, value);
        System.out.println("level " + levelId + " set to " +  (value == null ? "null" : value.getName()));
    }

    public void setValueAndFire(int levelId, AdminEntityDTO value) {
        setValue(levelId, value);
        presenter.onSelectionChanged(levelId, value);
    }

    public AdminEntityDTO getValue(int levelId) {
        return values.get(levelId);

    }

}
