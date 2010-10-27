package org.sigmah.client.ui;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * An upload field represented by a simple button. This component is based on
 * {@link FileUploadField}.
 * 
 * @author tmi
 * @see FileUploadField
 */
public class ButtonFileUploadField extends TextField<String> {

    private String accept;
    private Button button;
    private AbstractImagePrototype buttonIcon;
    private String buttonCaption;
    private El file;
    private BaseEventPreview focusPreview;
    private InputElement fileElement;

    /**
     * Creates a new file upload field.
     */
    public ButtonFileUploadField() {
        focusPreview = new BaseEventPreview();
        focusPreview.setAutoHide(false);
        ensureVisibilityOnSizing = true;
        setWidth(150);
    }

    public String getButtonCaption() {
        return buttonCaption;
    }

    public void setButtonCaption(String buttonCaption) {
        this.buttonCaption = buttonCaption;
    }

    /**
     * A comma-separated list of content types that a server processing this
     * form will handle correctly.
     * 
     */
    public String getAccept() {
        if (rendered) {
            return ((InputElement) file.dom.cast()).getAccept();
        }
        return accept;
    }

    /**
     * Returns the button icon class.
     */
    public AbstractImagePrototype getButtonIconStyle() {
        return buttonIcon;
    }

    /**
     * Returns the file input element. You should not store a reference to this.
     * When resetting this field the file input will change.
     */
    public InputElement getFileInput() {
        return (InputElement) file.dom.cast();
    }

    @Override
    public String getName() {
        if (rendered) {
            String n = file.dom.getAttribute("name");
            if (!n.equals("")) {
                return n;
            }
        }
        return super.getName();
    }

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if ((event.getTypeInt() != Event.ONCLICK) && ((Element) event.getEventTarget().cast()).isOrHasChild(file.dom)) {
            button.onBrowserEvent(event);
        }
    }

    @Override
    public void onComponentEvent(ComponentEvent ce) {
        super.onComponentEvent(ce);
        switch (ce.getEventTypeInt()) {
        case Event.ONCHANGE:
            onChange(ce);
            break;
        }
    }

    @Override
    public void reset() {
        super.reset();
        createFileInput();
    }

    /**
     * A comma-separated list of content types that a server processing this
     * form will handle correctly.
     * 
     */
    public void setAccept(String accept) {
        this.accept = accept;
        if (rendered) {
            ((InputElement) file.dom.cast()).setAccept(accept);
        }
    }

    /**
     * Sets the button icon class.
     * 
     * @param buttonIconStyle
     *            the button icon style
     */
    public void setButtonIcon(AbstractImagePrototype buttonIconStyle) {
        this.buttonIcon = buttonIconStyle;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (rendered) {
            file.dom.removeAttribute("name");
            if (name != null) {
                ((InputElement) file.dom.cast()).setName(name);
            }
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        if (button != null) {
            button.setEnabled(!readOnly);
        }
        if (file != null) {
            file.setEnabled(!readOnly);
        }
    }

    @Override
    protected void afterRender() {
        super.afterRender();
        el().removeStyleName(fieldStyle);
    }

    protected void createFileInput() {
        if (file != null) {
            el().removeChild(file.dom);
        }

        fileElement = Document.get().createFileInputElement();

        file = new El((Element) fileElement.cast());
        file.addEventsSunk(Event.ONCHANGE | Event.FOCUSEVENTS);
        file.setId(XDOM.getUniqueId());
        file.addStyleName("x-form-file");
        file.setTabIndex(-1);
        ((InputElement) file.dom.cast()).setName(name);
        ((InputElement) file.dom.cast()).setAccept(accept);
        file.insertInto(getElement(), 1);
        if (file != null) {
            file.setEnabled(isEnabled());
        }
    }

    @Override
    protected void doAttachChildren() {
        super.doAttachChildren();
        ComponentHelper.doAttach(button);
    }

    @Override
    protected void doDetachChildren() {
        super.doDetachChildren();
        ComponentHelper.doDetach(button);
    }

    @Override
    protected El getFocusEl() {
        return el();
    }

    @Override
    protected El getInputEl() {
        return el();
    }

    @Override
    protected El getStyleEl() {
        return el();
    }

    @Override
    protected void onBlur(ComponentEvent ce) {
        Rectangle rec = button.el().getBounds();
        if (rec.contains(BaseEventPreview.getLastXY())) {
            ce.stopEvent();
            return;
        }
        super.onBlur(ce);
        focusPreview.remove();
    }

    protected void onChange(ComponentEvent ce) {
        setValue(getFileInput().getValue());
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        if (focusPreview != null) {
            focusPreview.remove();
        }
    }

    @Override
    protected void onFocus(ComponentEvent ce) {
        focusPreview.add();
    }

    @Override
    protected void onRender(Element target, int index) {
        El wrap = new El(DOM.createDiv());
        wrap.addStyleName("x-form-field-wrap");
        wrap.addStyleName("x-form-file-wrap");

        setElement(wrap.dom, target, index);

        createFileInput();

        button = new Button(buttonCaption != null ? buttonCaption : "...");
        button.addStyleName("x-form-file-btn");
        button.setIcon(buttonIcon);
        button.render(wrap.dom);

        super.onRender(target, index);
        super.setReadOnly(true);
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        el().setWidth(button.getWidth());
        // file.setWidth(button.getWidth());
    }
}