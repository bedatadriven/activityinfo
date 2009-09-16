package org.activityinfo.client.page.map;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.util.Margins;

import org.activityinfo.client.Application;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.command.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.common.action.ActionToolBar;
import org.activityinfo.client.page.base.ExportCallback;
import org.activityinfo.client.page.base.ExportMenuButton;
import org.activityinfo.client.page.map.MapPresenter;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.model.ReportElement;

public class MapPage extends ContentPanel implements MapPresenter.View {

    private MapPresenter presenter;

    private final MapForm form;
    private MapPreview previewPanel;
    private ActionToolBar toolBar;

    public MapPage(MapForm form) {

        setLayout(new BorderLayout());
        setHeaderVisible(false);

        this.form = form;
        createFormPane(form);
        createPreview();
        createToolBar();

    }

    private void createFormPane(MapForm form) {
        BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST, 0.50f);
        west.setCollapsible(true);
        west.setSplit(true);
        west.setMargins(new Margins(0, 5, 0, 0));

        add((Component)form, west);
    }

    private void createPreview() {
        previewPanel = new MapPreview();
        previewPanel.setHeading(Application.CONSTANTS.preview());

        add(previewPanel, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    protected void createToolBar() {

        toolBar = new ActionToolBar(presenter);

        toolBar.addButton(UIActions.refresh, Application.CONSTANTS.refreshPreview(),
                Application.ICONS.refresh());

        toolBar.add(new ExportMenuButton(RenderElement.Format.PowerPoint, new ExportCallback() {
            public void export(RenderElement.Format format) {
                if(presenter!=null) {
                    presenter.export(format);
                }
            }
        }));

        toolBar.addButton(UIActions.exportData, "Exporter données",
                Application.ICONS.excel());

        previewPanel.setTopComponent(toolBar);
    }

    public void bindPresenter(MapPresenter presenter) {
        this.presenter = presenter;
        toolBar.setListener(presenter);
    }

    public AsyncMonitor getSchemaLoadingMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.loading());
    }

    public AsyncMonitor getMapLoadingMonitor() {
        return new MaskingAsyncMonitor(previewPanel, Application.CONSTANTS.loading());
    }

    public void setSchema(Schema schema) {
        form.setSchema(schema);
    }

    public ReportElement getMapElement() {
        return form.getMapElement();
    }

    public void setContent(ReportElement element, Content content) {
        previewPanel.setContent(element, content);
    }

    public boolean validate() {
        return form.validate(); 
    }
}

