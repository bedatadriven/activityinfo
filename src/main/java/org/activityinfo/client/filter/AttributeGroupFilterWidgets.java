package org.activityinfo.client.filter;

import static org.activityinfo.client.filter.AttributeGroupFilterWidget.DIMENSION_TYPE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.Log;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.callback.SuccessCallback;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetAttributeGroupsDimension;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.AttributeGroupResult;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.util.CollectionUtil;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AttributeGroupFilterWidgets implements FilterPanel {
    private final Dispatcher service;
    private final ContentPanel panel;
    private ValueChangeHandler<Filter> valueChangeHandler;
    private SuccessCallback<Void> drawCallback;
    
    private Filter prevFilter = new Filter();;
    private AttributeGroupResult prevResult;

    private List<AttributeGroupDTO> groups;
    private List<AttributeGroupFilterWidget> widgets;
    private Multimap<String, AttributeGroupFilterWidget> duplicates;

    public AttributeGroupFilterWidgets(ContentPanel panel, Dispatcher service,
        ValueChangeHandler<Filter> valueChangeHandler, SuccessCallback<Void> drawCallback) {

        this.service = service;
        this.panel = panel;
        
        this.groups = Lists.newArrayList();
        this.widgets = Lists.newArrayList();
        this.duplicates = ArrayListMultimap.create();

        this.valueChangeHandler = valueChangeHandler;
        this.drawCallback = drawCallback;
    }

    public void draw(final Filter filter) {
        final Filter value = new Filter(filter);
        value.clearRestrictions(DIMENSION_TYPE);

        // prevents executing the same command needlessly
        if (prevFilter == null || !prevFilter.equals(filter)) {
            prevFilter = filter;

            Log.debug("AttributeGroupFilterWidgets called for filter " + filter);

            // retrieve all attributegroups for the current filter
            service.execute(new GetAttributeGroupsDimension(value), new AsyncCallback<AttributeGroupResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    GWT.log("Failed to load attributes", caught);
                }

                @Override
                public void onSuccess(final AttributeGroupResult attributeGroupResult) {
                    // result should be different from the last time, we checked the filter earlier, but we could have
                    // an exceptional case of the same result with a different filter, so check for equality again.
                    // if the result is indeed different from the last time, (re)draw the widgets
                    if (prevResult == null || !prevResult.equals(attributeGroupResult)) {
                        prevResult = attributeGroupResult;

                        Log.debug("AttributeGroupFilterWidgets drawing widgets for result: " + attributeGroupResult);

                        service.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                GWT.log("Failed to load schema", caught);
                            }

                            @Override
                            public void onSuccess(final SchemaDTO schema) {
                                // clean up old widgets
                                for (AttributeGroupFilterWidget widget : widgets) {
                                    panel.remove(widget);
                                }
                                duplicates.clear();

                                // decorate resultlist from schema
                                List<AttributeGroupDTO> pivotData = attributeGroupResult.getData();
                                groups = new ArrayList<AttributeGroupDTO>();
                                if (CollectionUtil.isNotEmpty(pivotData)) {
                                    for (AttributeGroupDTO pivotGroup : pivotData) {
                                        AttributeGroupDTO schemaGroup = schema.getAttributeGroupById(pivotGroup.getId());
                                        if (schemaGroup != null) {
                                            groups.add(schemaGroup);
                                        }
                                    }
                                }

                                // create new widgets, one for each attributegroup.
                                // remember the old selection
                                List<Integer> selection = getSelectedIds();

                                widgets = new ArrayList<AttributeGroupFilterWidget>();
                                for (AttributeGroupDTO group : groups) {
                                    // create
                                    AttributeGroupFilterWidget widget = new AttributeGroupFilterWidget(group);

                                    // set old selection
                                    widget.setSelection(selection);

                                    // what to do when value changes
                                    if (valueChangeHandler != null) {
                                        widget.addValueChangeHandler(valueChangeHandler);
                                    }

                                    // add widget to panel if a widget with the same name (ignoring case) hasn't
                                    // already been added
                                    if (isNoDuplicate(widget)) {
                                        widgets.add(widget);
                                        panel.add(widget);
                                    } else {
                                        // otherwise add to collection of duplicates
                                        duplicates.put(group.getName().toLowerCase(), widget);
                                    }
                                }

                                if (drawCallback != null) {
                                    drawCallback.onSuccess(null);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private boolean isNoDuplicate(AttributeGroupFilterWidget widget) {
        for (AttributeGroupFilterWidget alreadyAdded : widgets) {
            if (alreadyAdded.getGroup().getName().toLowerCase()
                .equals(widget.getGroup().getName().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> getSelectedIds() {
        List<Integer> list = new ArrayList<Integer>();
        for (AttributeGroupFilterWidget widget : widgets) {
            Set<Integer> selection = widget.getValue().getRestrictions(DIMENSION_TYPE);
            if (CollectionUtil.isNotEmpty(selection)) {

                // if the widget has at least one selection, check the duplicates if we need to add some more ids
                Collection<AttributeGroupFilterWidget> hiddenWidgets =
                    duplicates.get(widget.getGroup().getName().toLowerCase());
                if (CollectionUtil.isNotEmpty(hiddenWidgets)) {
                    // has duplicates, so collect the attribute-ids from the hidden widgets by the
                    // selected attribute-names of the visible widget
                    List<String> selectedAttributeNames = widget.getSelectedAttributeNames();
                    for (AttributeGroupFilterWidget hiddenWidget : hiddenWidgets) {
                        List<Integer> hiddenIds = hiddenWidget.getAttributeIdsByName(selectedAttributeNames);
                        if (CollectionUtil.isNotEmpty(hiddenIds)) {
                            selection.addAll(hiddenIds);
                        }
                    }
                }

                list.addAll(selection);
            }
        }
        return list;
    }

    public void clearFilter() {
        for (AttributeGroupFilterWidget widget : widgets) {
            widget.clear();
        }
    }

    @Override
    public Filter getValue() {
        Filter filter = new Filter();

        List<Integer> selectedIds = getSelectedIds();
        if (selectedIds.size() > 0) {
            filter.addRestriction(DIMENSION_TYPE, selectedIds);
        }

        return filter;
    }

    @Override
    public void applyBaseFilter(Filter filter) {
        draw(filter);
    }

    @Override
    /** only sets the selection. To (re)draw the widgets based on the possibly new filter, call applyBaseFilter or draw */
    public void setValue(Filter value) {
        setValue(value, false);
    }

    @Override
    /** only sets the selection. To (re)draw the widgets based on the possibly new filter, call applyBaseFilter or draw */
    public void setValue(Filter value, boolean fireEvents) {
        if (value.isRestricted(DIMENSION_TYPE)) {
            Collection<Integer> restriction = value.getRestrictions(DIMENSION_TYPE);
            for (AttributeGroupFilterWidget widget : widgets) {
                widget.setSelection(restriction);
            }
        }
    }

    @Override
    /** not implemented */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Filter> handler) {
        return null;
    }

    @Override
    /** not implemented */
    public void fireEvent(GwtEvent<?> event) {
    }

}
