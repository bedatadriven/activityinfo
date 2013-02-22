package org.activityinfo.client.page.entry.admin;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Iterator;
import java.util.Map;

import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.common.collect.Maps;

/**
 * Collection of {@link ComboBox} built with reference to an
 * {@link AdminFieldSetPresenter}. Note that this is not a widget itself, these
 * AdminComboBoxes must still be added to a container.
 */
public class AdminComboBoxSet implements Iterable<AdminComboBox> {

    public interface ComboBoxFactory {
        AdminComboBox create(AdminLevelDTO level,
            ListStore<AdminEntityDTO> store);
    }

    private final Map<Integer, AdminComboBox> comboBoxes = Maps.newHashMap();

    public AdminComboBoxSet(final AdminFieldSetPresenter presenter,
        ComboBoxFactory comboBoxFactory) {

        for (final AdminLevelDTO level : presenter.getAdminLevels()) {
            final int levelId = level.getId();

            final AdminComboBox comboBox = comboBoxFactory.create(level,
                presenter.getStore(levelId));
            comboBox.setEnabled(presenter.isLevelEnabled(levelId));
            comboBoxes.put(levelId, comboBox);

            comboBox
                .addSelectionChangeListener(new Listener<SelectionChangedEvent>() {
                    @Override
                    public void handleEvent(SelectionChangedEvent be) {
                        AdminEntityDTO selected = (AdminEntityDTO) be
                            .getSelectedItem();
                        presenter.setSelection(levelId, selected);
                    }

                });

            presenter.addListener(AdminLevelSelectionEvent.TYPE,
                new Listener<AdminLevelSelectionEvent>() {
                    @Override
                    public void handleEvent(AdminLevelSelectionEvent event) {
                        comboBoxes.get(event.getLevelId()).setValue(
                            event.getValue());
                    }
                });

            presenter.addListener(LevelStateChangeEvent.TYPE,
                new Listener<LevelStateChangeEvent>() {
                    @Override
                    public void handleEvent(LevelStateChangeEvent event) {
                        comboBoxes.get(event.getLevelId()).setEnabled(
                            event.isEnabled());
                    }
                });
        }
    }

    public boolean validate() {
        boolean valid = true;
        for (AdminComboBox comboBox : comboBoxes.values()) {
            if (!comboBox.validate()) {
                valid = false;
            }
        }
        return valid;
    }

    @Override
    public final Iterator<AdminComboBox> iterator() {
        return comboBoxes.values().iterator();
    }
}
