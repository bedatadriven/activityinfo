package org.activityinfo.client.page.report;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import org.activityinfo.client.Application;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.ExportMenuButton;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.DateUtilGWTImpl;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.date.DateUtil;
import org.activityinfo.shared.dto.ReportDefinitionDTO;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.ReportFrequency;

/**
 * @author Alex Bertram
 */
public class ReportPreview extends ContentPanel implements ReportPreviewPresenter.View {
    private ReportPreviewPresenter presenter;

    private static class DateValue extends BaseModelData {

        public DateValue(DateRange value, String label) {
            setValue(value);
            setLabel(label);
        }

        public String getLabel() {
            return get("label");
        }

        public void setLabel(String label) {
            set("label", label);
        }

        public void setValue(DateRange value) {
            set("value", value);
        }

        public DateRange getValue() {
            return get("value");
        }
    }

    private ActionToolBar toolBar;
    private Html previewHtml;
    private DateUtil dateUtil;
    private DateRange dateRange;


    public ReportPreview() {
        dateUtil = new DateUtilGWTImpl();
        dateRange = new DateRange();
    }

    public void init(ReportPreviewPresenter presenter, ReportDefinitionDTO template) {

        toolBar = new ActionToolBar();
        this.presenter = presenter;
        toolBar.setListener(this.presenter);

        setHeading(template.getTitle());

        if (template.getFrequency() == ReportFrequency.Monthly) {
            addMonthlyFields();
        } else if (template.getFrequency() == ReportFrequency.Adhoc) {
            addAddHocFields();
        }

        toolBar.addRefreshButton();
        toolBar.addEditButton();

        // do initial load
        presenter.onUIAction(UIActions.refresh);

        ExportMenuButton export = new ExportMenuButton(RenderElement.Format.Word, presenter);
        toolBar.add(export);

        setTopComponent(toolBar);

        previewHtml = new Html();
        previewHtml.addStyleName("report");
        add(previewHtml);

        setScrollMode(Style.Scroll.AUTO);
    }

    public void setActionEnabled(String actionId, boolean enabled) {
        toolBar.setActionEnabled(actionId, enabled);

    }

    public DateRange getDateRange() {
        return dateRange;
    }

    private void addAddHocFields() {

        final DateField fromField = new DateField();
        final DateField toField = new DateField();

        Listener<FieldEvent> listener = new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                dateRange = new DateRange(fromField.getValue(), toField.getValue());
            }
        };

        fromField.addListener(Events.Change, listener);
        toField.addListener(Events.Change, listener);

        toolBar.add(new LabelToolItem(Application.CONSTANTS.fromDate()));
        toolBar.add(fromField);
        toolBar.add(new LabelToolItem(Application.CONSTANTS.toDate()));
        toolBar.add(toField);
    }

    private void addMonthlyFields() {
        // Create store
        ListStore<DateValue> store = new ListStore<DateValue>();
        DateTimeFormat format = DateTimeFormat.getFormat("MMM yyyy");
        Month month = dateUtil.getCurrentMonth();

        for (int i = 36; i != 0; i--) {
            DateRange range = dateUtil.monthRange(month);
            store.add(new DateValue(range, format.format(range.getMinDate())));
            month = month.previous();
        }

        ComboBox<DateValue> combo = new ComboBox<DateValue>();
        combo.setValueField("value");
        combo.setDisplayField("label");
        combo.setStore(store);
        combo.setValue(combo.getStore().getAt(0));
        combo.setEditable(false);
        combo.setForceSelection(true);
        combo.setAllowBlank(false);
        combo.addSelectionChangedListener(new SelectionChangedListener<DateValue>() {

            @Override
            public void selectionChanged(
                    SelectionChangedEvent<DateValue> se) {

                dateRange = se.getSelectedItem().getValue();
                // onParamChanged();

            }

        });
        toolBar.add(combo);

        // TODO: most of this should be in the presenter
        dateRange = combo.getValue().getValue();
    }

    public void setPreviewHtml(String html) {
        previewHtml.setHtml(html);

    }

    public AsyncMonitor getLoadingMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.loading());
    }
}
