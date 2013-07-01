package org.activityinfo.client.filter;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.dto.AttributeDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.util.CollectionUtil;

import com.google.gwt.user.client.Event;

public class AttributeGroupFilterWidget extends FilterWidget {
    public static final DimensionType DIMENSION_TYPE = DimensionType.Attribute;

    private AttributeGroupFilterDialog dialog;
    private AttributeGroupDTO group;

    public AttributeGroupFilterWidget(AttributeGroupDTO group) {
        super();
        this.group = group;
        this.dimensionSpan.setInnerText(group.getName());
        this.stateSpan.setInnerText(I18N.CONSTANTS.all());
    }

    @Override
    public void choose(Event event) {
        if (dialog == null) {
            dialog = new AttributeGroupFilterDialog(group);
        }
        dialog.show(getBaseFilter(), getValue(), new SelectionCallback<Set<Integer>>() {
            @Override
            public void onSelected(Set<Integer> selection) {
                boolean selectionChanged =
                    CollectionUtil.notContainsEqual(getValue().getRestrictions(DimensionType.Attribute), selection);

                if (selectionChanged) {
                    Filter newValue = new Filter();
                    if (CollectionUtil.isNotEmpty(selection)) {
                        newValue.addRestriction(DimensionType.Attribute, selection);
                    }
                    setValue(newValue);
                }
            }
        });
    }

    public AttributeGroupDTO getGroup() {
        return group;
    }

    public void clear() {
        setValue(new Filter(), false);
        updateView();
    }

    @Override
    public void updateView() {
        if (getValue().isRestricted(DimensionType.Attribute)) {
            setState(I18N.CONSTANTS.loading());
            List<String> attributeNames = getSelectedAttributeNames();
            setState(FilterResources.MESSAGES.filteredAttributeList(attributeNames));
        } else {
            setState(I18N.CONSTANTS.all());
        }
    }

    public List<String> getSelectedAttributeNames() {
        List<String> attributeNames = new ArrayList<String>();
        if (getValue().isRestricted(DimensionType.Attribute)) {
            for (Integer id : getValue().getRestrictions(DimensionType.Attribute)) {
                AttributeDTO attr = group.getAttributeById(id);
                if (attr != null) {
                    attributeNames.add(attr.getName());
                }
            }
            Collections.sort(attributeNames);
        }
        return attributeNames;
    }

    public List<Integer> getAttributeIdsByName(List<String> attributeNames) {
        List<Integer> ids = new ArrayList<Integer>();
        for (String attributeName : attributeNames) {
            for (AttributeDTO attr : getGroup().getAttributes()) {
                if (attributeName.trim().equalsIgnoreCase(attr.getName().trim())) {
                    ids.add(attr.getId());
                }
            }
        }
        return ids;
    }

    public void setSelection(Collection<Integer> selection) {
        Filter newValue = new Filter();
        if (CollectionUtil.isNotEmpty(selection)) {
            // only add relevant ids to filter
            List<Integer> attributeIds = group.getAttributeIds();
            for (Integer selectedId : selection) {
                if (attributeIds.contains(selectedId)) {
                    newValue.addRestriction(DimensionType.Attribute, selectedId);
                }
            }
        }
        setValue(newValue, false);
    }
}
