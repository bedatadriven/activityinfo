package org.sigmah.client.page.admin;


import org.sigmah.client.ui.StylableHBoxLayout;


import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Displays administration screen.
 * 
 * @author nrebiai
 * 
 */
public class AdminView extends LayoutContainer implements AdminPresenter.View{
	
	private final static int BORDER = 8;
    private final static String STYLE_MAIN_BACKGROUND = "main-background";

    private final ContentPanel rightPanel;
    private final ContentPanel leftNavigationPanel;
    private Widget widget;
	
	@Inject
    public AdminView() { 
		final BorderLayoutData leftLayoutData = new BorderLayoutData(LayoutRegion.WEST, 150);
        leftLayoutData.setMargins(new Margins(0, BORDER / 2, 0, 0));
		final BorderLayout borderLayout = new BorderLayout();
        borderLayout.setContainerStyle("x-border-layout-ct " + STYLE_MAIN_BACKGROUND);
        setLayout(borderLayout);
		leftNavigationPanel = new ContentPanel(new StylableHBoxLayout("main-background project-top-bar"));
		leftNavigationPanel.setHeaderVisible(false);
        
        rightPanel = new ContentPanel();
        rightPanel.setHeaderVisible(false);
        rightPanel.setSize(300, 300);
        add(leftNavigationPanel,leftLayoutData);
        add(rightPanel,new BorderLayoutData(LayoutRegion.CENTER));
	}

	@Override
	public void setMainPanel(Widget widget) {
		if(this.widget != null)
            rightPanel.remove(this.widget);

        rightPanel.add(widget);
        this.widget = widget;		
	}

	@Override
	public ContentPanel getTabPanel() {
		return leftNavigationPanel;
	}
}
