package org.activityinfo.client.page.report;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;

import org.activityinfo.client.Application;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.command.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.common.action.ActionToolBar;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.shared.dto.ReportParameterDTO;
import org.activityinfo.shared.dto.ReportTemplateDTO;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/*
 * @author Alex Bertram
 */

public class ReportPreview extends ContentPanel implements ReportPreviewPresenter.View {
    private ActionToolBar toolBar;
    private Html previewHtml;


    private class DateValue extends BaseModelData {

        public DateValue(Date value, String label) {
            setValue(value);
            setLabel(label);
        }

        public String getLabel() {
            return get("label");
        }

        public void setLabel(String label) {
            set("label", label);
        }

        public void setValue(Date value) {
            set("value", value);
        }

        public Date getValue() {
            return get("value");
        }

    }

    private Map<String, Field> fields = new HashMap<String,Field>();

    public ReportPreview() {
    }

    public void init(ReportPreviewPresenter presenter, ReportTemplateDTO template) {

        toolBar = new ActionToolBar();
        toolBar.setListener(presenter);

        setHeading(template.getTitle());

        for(ReportParameterDTO param : template.getParameters())  {
            Field<?> f = createParameterField(param);
            fields.put(param.getName(),f);

            toolBar.add(new LabelToolItem(param.getLabel()));
            toolBar.add(f);
        }

        toolBar.addRefreshButton();
        toolBar.addEditButton();

        setTopComponent(toolBar);

        previewHtml = new Html();
        previewHtml.addStyleName("report");
        add(previewHtml);



        setScrollMode(Style.Scroll.AUTO);
    }

    public void setActionEnabled(String actionId, boolean enabled) {
        toolBar.setActionEnabled(actionId, enabled);

    }

    private Field<?> createParameterField(ReportParameterDTO param) {

        Field<?> field;

        switch(param.getType()) {
        case ReportParameterDTO.TYPE_DATE:
            field = createDateParameterField(param);
            break;
        default:
            throw new IllegalArgumentException();
        }

        field.setFieldLabel(param.getLabel());
        field.setName(param.getName());

        return field;
    }

    private Field<?> createDateParameterField(ReportParameterDTO param) {

        if(param.getDateUnit() == ReportParameterDTO.UNIT_DAY) {

            DateField dateField = new DateField();
            dateField.setAllowBlank(false);
            dateField.addListener(Events.Change, new Listener<FieldEvent>() {

                @Override
                public void handleEvent(FieldEvent be) {
                  //  onParamChanged();
                }
            });

            return dateField;

        } else {

            ComboBox<DateValue> combo = new ComboBox<DateValue>();
            combo.setValueField("value");
            combo.setDisplayField("label");
            combo.setStore(getDateStore(param.getDateUnit()));
            combo.setValue(combo.getStore().getAt(0));
            combo.setEditable(false);
            combo.setForceSelection(true);
            combo.setAllowBlank(false);
            combo.addSelectionChangedListener(new SelectionChangedListener<DateValue>() {

                @Override
                public void selectionChanged(
                        SelectionChangedEvent<DateValue> se) {

                   // onParamChanged();

                }

            });
            return combo;
        }
    }

    public Map<String, Object> getParameters() {

        Map<String, Object> values = new HashMap<String, Object>();

        for(Map.Entry<String,Field> entry : fields.entrySet()) {

            Object value = entry.getValue().getValue();

            if(value instanceof DateValue) {
                values.put(entry.getKey(),((DateValue) value).getValue());
            } else {
                values.put(entry.getKey(), entry.getValue().getValue());
            }
        }


        return values;
    }

    private ListStore<DateValue> getDateStore(int dateUnit) {

        ListStore<DateValue> store = new ListStore<DateValue>();

        if(dateUnit == ReportParameterDTO.UNIT_MONTH) {
            DateWrapper month = new DateWrapper();
            DateTimeFormat format = DateTimeFormat.getFormat("MMM yyyy");

            for(int i=36; i!=0; i--) {
                store.add(new DateValue(month.asDate(), format.format(month.asDate())));
                month = month.addMonths(-1);
            }

        } else {
            throw new Error("not implemented");
        }


        return store;
    }

    public void setPreviewHtml(String html) {
        previewHtml.setHtml(html);

    }

    public AsyncMonitor getLoadingMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.loading());
    }


}
