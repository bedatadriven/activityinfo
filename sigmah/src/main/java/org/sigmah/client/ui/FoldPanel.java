package org.sigmah.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FoldPanel extends FlowPanel {
    private final ArrayList<Widget> list = new ArrayList<Widget>();
    
    public static final int STATE_EXPANDED = 0;
    public static final int STATE_FOLDED = 1;
    public static final int STATE_COLLAPSED = 2;
    
    private int state;
    
    public FoldPanel() {
        addStyleName("fold");
        
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

        final FlowPanel content = new FlowPanel();
        
        super.add(heading);
        super.add(content);
    }
    
    public void setHeading(String text) {
        final Label heading = (Label) getWidget(0);
        heading.setText(text);
        
        if(!"".equals(text))
            getWidget(1).addStyleName("fold-content");
        else
            getWidget(1).removeStyleName("fold-content");
    }

    @Override
    public void add(Widget w) {
        ((FlowPanel) getWidget(1)).add(w);
        list.add(w);
    }
    
    public void collapse() {
        final FlowPanel content = (FlowPanel) getWidget(1);
        
        for(final Widget widget : list)
            content.remove(widget);
        
        setState(STATE_COLLAPSED);
    }
    
    public void fold(boolean propagate) {
        final FlowPanel content = (FlowPanel) getWidget(1);
        
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
        final FlowPanel content = (FlowPanel) getWidget(1);
        
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
        final Widget header = getWidget(0);
        
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
