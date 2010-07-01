/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.ExportCallback;
import org.sigmah.client.page.common.toolbar.ExportMenuButton;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.model.ReportElement;

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

        add((Component) form, west);
    }

    private void createPreview() {
        previewPanel = new MapPreview();
        previewPanel.setHeading(I18N.CONSTANTS.preview());

        add(previewPanel, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    protected void createToolBar() {

        toolBar = new ActionToolBar(presenter);

        toolBar.addButton(UIActions.refresh, I18N.CONSTANTS.refreshPreview(),
                IconImageBundle.ICONS.refresh());

        toolBar.add(new ExportMenuButton(RenderElement.Format.PowerPoint, new ExportCallback() {
            public void export(RenderElement.Format format) {
                if (presenter != null) {
                    presenter.export(format);
                }
            }
        }));

        toolBar.addButton(UIActions.exportData, I18N.CONSTANTS.exportData(),
                IconImageBundle.ICONS.excel());

        previewPanel.setTopComponent(toolBar);
    }

    public void bindPresenter(MapPresenter presenter) {
        this.presenter = presenter;
        toolBar.setListener(presenter);
    }

    public AsyncMonitor getSchemaLoadingMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
    }

    public AsyncMonitor getMapLoadingMonitor() {
        return new MaskingAsyncMonitor(previewPanel, I18N.CONSTANTS.loading());
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

