package org.sigmah.client.ui;

import com.google.gwt.dom.client.Style;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A FoldPanel contains a header and a body.<br>
 * <br>
 * This widget can have one of the 3 following states :<ul>
 *     <li>Expanded : the full content of the FoldPanel is displayed</li>
 *     <li>Folded : only the header of the FoldPanel and of its contained FoldPanels are displayed</li>
 *     <li>Collapsed : only the header of the FoldPanel is displayed</li>
 * </ul>
 * This widget can hide and show RichTextElements without making them crash in Firefox and Webkit.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class FoldPanel extends FlowPanel {
    private final ArrayList<Widget> list = new ArrayList<Widget>();
    
    public static final int STATE_EXPANDED = 0;
    public static final int STATE_FOLDED = 1;
    public static final int STATE_COLLAPSED = 2;

    private static final int HEADER_INDEX = 0;
    private static final int BODY_INDEX = 1;

    private int state;

    /**
     * Creates a new and empty fold panel.
     */
    public FoldPanel() {
        addStyleName("fold");

        final HorizontalPanel titleBar = new HorizontalPanel();
        titleBar.getElement().getStyle().setDisplay(Style.Display.NONE);
        titleBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        final Label heading = new Label();
        heading.addStyleName("fold-title");
        heading.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if(state == STATE_EXPANDED)
                    collapse();
                else
                    expand(true);
            }
        });
        heading.addStyleName("fold-expanded");
        titleBar.add(heading);
        titleBar.setCellWidth(heading, "100%");

        final FlowPanel toolButtonPanel = new FlowPanel();
        titleBar.add(toolButtonPanel);

        final FlowPanel content = new FlowPanel();
        
        super.add(titleBar);
        super.add(content);
    }

    /**
     * Defines the header of this fold panel.
     * @param text Text to display in the header of this fold panel.
     */
    public void setHeading(String text) {
        final HorizontalPanel titleBar = (HorizontalPanel) super.getWidget(HEADER_INDEX);
        final Label heading = (Label) titleBar.getWidget(0);
        heading.setText(text);
        
        if(text != null && !"".equals(text)) {
            super.getWidget(BODY_INDEX).addStyleName("fold-content");
            titleBar.getElement().getStyle().setProperty("display", "");

        } else {
            super.getWidget(BODY_INDEX).removeStyleName("fold-content");
            titleBar.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
    }

    @Override
    public void add(Widget w) {
        ((FlowPanel) super.getWidget(BODY_INDEX)).add(w);
        list.add(w);
    }

    @Override
    public Widget getWidget(int index) {
        return ((FlowPanel) super.getWidget(BODY_INDEX)).getWidget(index);
    }

    @Override
    public int getWidgetCount() {
        return ((FlowPanel) super.getWidget(BODY_INDEX)).getWidgetCount();
    }

    /**
     * Adds a button with the specified <code>image</code> and <code>action</code> in the top right corner of this panel.
     * @param image Image of the button.
     * @param action Action to execute when the button is clicked.
     * @return The index of the new button.
     */
    public void addToolButton(ImageResource image, ClickHandler action) {
        final Image button = new Image(image);
        button.addStyleName("fold-tool-button");
        button.addClickHandler(action);

        final HorizontalPanel titleBar = (HorizontalPanel) super.getWidget(HEADER_INDEX);
        final FlowPanel toolButtonPanel = (FlowPanel) titleBar.getWidget(1);

        toolButtonPanel.add(button);

        toolButtonPanel.setWidth(16 * toolButtonPanel.getWidgetCount()+"px");
    }

    /**
     * Edits a tool button by replacing its current image by the one provided.
     * @param index The index of the button to edit.
     * @param image The new image to display.
     */
    public void setToolButtonImage(int index, ImageResource image) {
        final HorizontalPanel titleBar = (HorizontalPanel) super.getWidget(HEADER_INDEX);
        final FlowPanel toolButtonPanel = (FlowPanel) titleBar.getWidget(1);

        final Image button = (Image) toolButtonPanel.getWidget(index);
        button.setResource(image);
    }

    /**
     * Returns the current number of tool buttons displayed by this panel.
     * @return the number of tool button.
     */
    public int getToolButtonCount() {
        final HorizontalPanel titleBar = (HorizontalPanel) super.getWidget(HEADER_INDEX);
        final FlowPanel toolButtonPanel = (FlowPanel) titleBar.getWidget(1);
        return toolButtonPanel.getWidgetCount();
    }

    public void collapse() {
        final FlowPanel content = (FlowPanel) super.getWidget(BODY_INDEX);
        
        for(final Widget widget : list)
            content.remove(widget);
        
        setState(STATE_COLLAPSED);
    }
    
    public void fold(boolean propagate) {
        final FlowPanel content = (FlowPanel) super.getWidget(BODY_INDEX);
        
        boolean collapse = true;
        
        for(int index = 0; index < list.size(); index++) {
            final Widget child = list.get(index);
            
            if(child instanceof FoldPanel) {
                collapse = false;
                
                if(propagate)
                    ((FoldPanel) child).fold(propagate);
            } else {
                content.remove(child);
            }
        }
        
        setState(collapse?STATE_COLLAPSED:STATE_FOLDED);
    }
    
    public void expand(boolean propagate) {
        final FlowPanel content = (FlowPanel) super.getWidget(BODY_INDEX);
        
        Widget next = null;
        final Iterator<Widget> children = content.iterator();
        if(children.hasNext())
            next = children.next();
        
        for(int index = 0; index < list.size(); index++) {
            final Widget widget = list.get(index);
            
            if(widget != next) {
                content.insert(widget, index);
                
            } else {
                if(children.hasNext())
                    next = children.next();
                else
                    next = null;
            }
            
            if(propagate && widget instanceof FoldPanel)
                ((FoldPanel) widget).expand(propagate);
        }
        
        setState(STATE_EXPANDED);
    }

    private void setState(int state) {
        // Removing the previous style
        final HorizontalPanel titleBar = (HorizontalPanel) super.getWidget(HEADER_INDEX);
        final Widget header = titleBar.getWidget(0);
        
        switch(this.state) {
            case STATE_EXPANDED:
                header.removeStyleName("fold-expanded");
                break;
            case STATE_FOLDED:
                header.removeStyleName("fold-folded");
                break;
            case STATE_COLLAPSED:
                header.removeStyleName("fold-collapsed");
                break;
        }

        // Adding the new style
        switch(state) {
            case STATE_EXPANDED:
                header.addStyleName("fold-expanded");
                break;
            case STATE_FOLDED:
                header.addStyleName("fold-folded");
                break;
            case STATE_COLLAPSED:
                header.addStyleName("fold-collapsed");
                break;
        }

        this.state = state;
    }
}
