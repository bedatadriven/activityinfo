package org.sigmah.client.page.project.logframe;

import java.util.ArrayList;
import java.util.Collection;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;

/**
 * A pop-up window to select several elements among several lists of choices.<br/>
 * The elements are shown in a combobox with uses the field <code>label</code> .
 * Ensure that your model provides this attribute.
 * 
 * @author tmi
 * 
 */
public class SelectWindow {

    /**
     * Listen the selection events.
     * 
     * @author tmi
     * 
     */
    public static interface SelectListener {

        /**
         * Method called when the selection form is correctly validated
         * (elements can be <code>null</code> if the null selection is allowed
         * for some lists). The elements are returned in the same order in which
         * lists have been added.
         * 
         * @param elements
         *            The selected elements.
         */
        public void elementsSelected(ModelData... elements);

    }

    /**
     * Combobox preferred height.
     */
    private static final int COMBOBOX_HEIGHT = 32;

    /**
     * Listeners.
     */
    private final ArrayList<SelectListener> listeners;

    /**
     * The pop-up window.
     */
    private Window window;

    /**
     * The selection form label.
     */
    private Label titleLabel;

    /**
     * The vertical panel to display comboboxes.
     */
    private ContentPanel comboBoxesPanel;

    /**
     * List of comboboxes.
     */
    private final ArrayList<ComboBox<? extends ModelData>> comboBoxes;

    /**
     * Initialize the window.
     */
    public SelectWindow() {
        listeners = new ArrayList<SelectListener>();
        comboBoxes = new ArrayList<ComboBox<? extends ModelData>>();
    }

    /**
     * Adds a listener.
     * 
     * @param l
     *            The new listener.
     */
    public void addSelectListener(SelectListener l) {

        if (l == null) {
            return;
        }

        listeners.add(l);
    }

    /**
     * Removes a listener.
     * 
     * @param l
     *            The old listener.
     */
    public void removeSelectListener(SelectListener l) {

        if (l == null) {
            return;
        }

        listeners.remove(l);
    }

    /**
     * Informs the listeners that elements have been selected.
     * 
     * @param elements
     *            The selected elements.
     */
    protected void fireElementsSelected(ModelData... elements) {
        for (final SelectListener l : listeners) {
            l.elementsSelected(elements);
        }
    }

    /**
     * Builds the pop-up window.
     */
    private void init() {

        // Build the form label.
        titleLabel = new Label(I18N.CONSTANTS.selectWindowEmptyText());

        // Builds the selection button.
        final Button selectButton = new Button(I18N.CONSTANTS.selectWindowSelectAction());
        selectButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                final ArrayList<ModelData> selected = new ArrayList<ModelData>();

                // Retrieves each selected value.
                for (final ComboBox<? extends ModelData> combobox : comboBoxes) {

                    final ModelData value = combobox.getValue();

                    // Checks if null selection is allowed for this list.
                    if (!combobox.getAllowBlank() && value == null) {
                        MessageBox.alert(I18N.CONSTANTS.selectWindowNoElementSelected(),
                                I18N.CONSTANTS.selectWindowSelectElement(), null);
                        return;
                    }

                    selected.add(value);
                }

                fireElementsSelected(selected.toArray(new ModelData[selected.size()]));

                // Closes the window.
                window.hide();
            }
        });

        // Builds the comboboxes panel.
        comboBoxesPanel = new ContentPanel();
        final VBoxLayout comboboxesPanelLayout = new VBoxLayout();
        comboboxesPanelLayout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        comboBoxesPanel.setHeaderVisible(false);
        comboBoxesPanel.setLayout(comboboxesPanelLayout);
        comboBoxesPanel.setBorders(false);
        comboBoxesPanel.setWidth("100%");

        // Builds the main panel.
        final ContentPanel mainPanel = new ContentPanel();
        final VBoxLayout layout = new VBoxLayout();
        layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        mainPanel.setHeaderVisible(false);
        mainPanel.setLayout(layout);
        mainPanel.setBorders(true);
        mainPanel.setWidth("100%");

        mainPanel.setTopComponent(null);
        mainPanel.add(titleLabel, new VBoxLayoutData(new Margins(4, 8, 0, 8)));
        mainPanel.add(comboBoxesPanel, new VBoxLayoutData(new Margins(4, 8, 0, 8)));
        mainPanel.add(selectButton, new VBoxLayoutData(new Margins(4, 8, 0, 8)));

        // Builds window.
        window = new Window();
        window.setWidth(380);
        window.setPlain(true);
        window.setModal(true);
        window.setBlinkModal(true);
        window.setLayout(new FitLayout());

        window.add(mainPanel);
    }

    /**
     * Initialize the window and open it.
     * 
     * @param title
     *            The window title.
     * @param header
     *            The heading label of the selection form.
     */
    public void show(String title, String header) {

        // Lazy building.
        if (window == null) {
            init();
        }

        titleLabel.setText(header);

        // Open the window.
        window.setHeading(title);
        window.setHeight(100 + COMBOBOX_HEIGHT * comboBoxes.size());
        window.show();
    }

    /**
     * Removes all the lists of choices of this window.<br/>
     * This method removes also the listeners.
     */
    public void clear() {

        if (window != null) {
            comboBoxesPanel.removeAll();
            comboBoxes.clear();
            listeners.clear();
            window = null;
        }
    }

    /**
     * Adds a list of choices available in the window.
     * 
     * @param <E>
     *            The type of selectable elements.
     * @param fieldLabelString
     *            The label of the list of choices. Can be <code>null</code>.
     * @param choices
     *            The list of choices.
     * @param allowBlank
     *            If the selection for this list is required.
     * @param displayField
     *            The underlying data field name to bind to this list. Must not
     *            be <code>null</code>.
     */
    public <E extends ModelData> void addChoicesList(String fieldLabelString, Collection<E> choices,
            boolean allowBlank, String displayField) {

        // Checks if the display field is specified.
        if (displayField == null || "".equals(displayField.trim())) {
            throw new IllegalArgumentException("the display field must not be null");
        }

        // Checks if the list of choices is correct.
        if (choices == null || choices.isEmpty()) {
            throw new IllegalArgumentException("the list of choices must contain at least one choice");
        }

        // Lazy building.
        if (window == null) {
            init();
        }

        // Builds the store.
        final ListStore<E> store = new ListStore<E>();
        store.add(new ArrayList<E>(choices));

        // Builds the combobox.
        final ComboBox<E> combobox = new ComboBox<E>();
        combobox.setStore(store);
        combobox.setEditable(true);
        combobox.setAllowBlank(allowBlank);
        combobox.setEmptyText(I18N.CONSTANTS.selectWindowEmptyText());
        combobox.setLoadingText(I18N.CONSTANTS.loading());
        combobox.setFieldLabel(fieldLabelString);
        combobox.setDisplayField(displayField.trim());

        comboBoxes.add(combobox);

        // Builds the field label.
        final Label fieldLabel = new Label(fieldLabelString);
        fieldLabel.setWidth("100px");
        fieldLabel.addStyleName("flexibility-element-label");

        // Builds the combobox panel.
        final ContentPanel comboboxPanel = new ContentPanel();
        comboboxPanel.setBodyBorder(false);
        comboboxPanel.setHeaderVisible(false);
        comboboxPanel.setLayout(new HBoxLayout());

        comboboxPanel.add(fieldLabel, new HBoxLayoutData(new Margins(4, 5, 0, 0)));
        final HBoxLayoutData flex = new HBoxLayoutData(new Margins(0, 5, 0, 0));
        flex.setFlex(1);
        comboboxPanel.add(combobox, flex);

        // Adds the combobox in the panel.
        comboBoxesPanel.setHeight(COMBOBOX_HEIGHT * comboBoxes.size());
        comboBoxesPanel.add(comboboxPanel, new VBoxLayoutData(new Margins(4, 0, 0, 0)));
        comboBoxesPanel.layout();
    }
}
