package org.sigmah.client.page.project.details;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class ProjectDetailsView extends ProjectDetailsPresenter.View {

    // Toolbar buttons.
    private Button saveButton;

    /**
     * Builds the details main panel.
     * 
     * @return The details main panel.
     */
    public ProjectDetailsView() {

        // Configuration
        VBoxLayout layout = new VBoxLayout() {
            @Override
            protected void onLayout(Container<?> container, El target) {
                super.onLayout(container, target);
                innerCt.addStyleName("logframe-body");
            }
        };
        layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        layout.setPadding(new Padding(0, 20, 0, 0));
        setLayout(layout);
        setHeaderVisible(true);
        setBorders(true);
        setHeading(I18N.CONSTANTS.projectDetails());

        // Toolbar.
        final ToolBar toolBar = buildToolbar();

        setTopComponent(toolBar);
    }

    /**
     * Builds the actions toolbar.
     * 
     * @return The actions toolbar.
     */
    private ToolBar buildToolbar() {

        // Save button.
        saveButton = new Button(I18N.CONSTANTS.save(), IconImageBundle.ICONS.save());
        saveButton.setEnabled(false);

        // Actions toolbar.
        final ToolBar toolBar = new ToolBar();
        toolBar.setAlignment(HorizontalAlignment.LEFT);
        toolBar.setBorders(false);

        toolBar.add(saveButton);

        return toolBar;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public ContentPanel getMainPanel() {
        return this;
    }
}
