package org.activityinfo.client.page.entry.editor;

import org.activityinfo.client.Application;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.page.base.SaveChangesCallback;
import org.activityinfo.client.page.base.SavePromptMessageBox;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ComponentEvent;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteFormPage extends SiteForm {


    private ToolBar toolBar;



    private void createToolBar() {

        SelectionListener listener = new SelectionListener()  {
            @Override
            public void componentSelected(ComponentEvent ce) {
                if(presenter!=null) {
                    presenter.onUIAction(ce.getComponent().getItemId());
                }
            }
        };

        Button gridButton = new Button("Retour au Grille", Application.ICONS.table(), listener);
        gridButton.setItemId(UIActions.gotoGrid);
        toolBar.add(gridButton);

        Button saveButton = new Button(Application.CONSTANTS.save(), Application.ICONS.save(), listener);
        saveButton.setItemId(UIActions.save);
        toolBar.add(saveButton);

        Button discardButton = new Button(Application.CONSTANTS.discardChanges(),
                Application.ICONS.cancel(), listener);
        discardButton.setItemId(UIActions.cancel);
        toolBar.add(discardButton);

        setTopComponent(toolBar);

    }


    public void promptSaveChanges(final SaveChangesCallback callback) {

        final SavePromptMessageBox box = new SavePromptMessageBox();

        box.show(callback);

    }

}
