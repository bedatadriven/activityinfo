package org.sigmah.client.ui;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

/**
 * Slightly edited version of {@link com.extjs.gxt.ui.client.widget.Viewport}
 * that do not span to the entire page height.
 */
public class SigmahViewport extends LayoutContainer {

  private String loadingPanelId = "loading";
  private boolean enableScroll;

  private int clutterWidth;
  private int clutterHeight;
  
  /**
   * @param clutter Height reserved by other widgets.
   */
  public SigmahViewport(int clutterWidth, int clutterHeight) {
    monitorWindowResize = true;
    baseStyle = "x-viewport";
    this.clutterWidth = clutterWidth;
    this.clutterHeight = clutterHeight;
  }

  /**
   * Returns the window resizing state.
   * 
   * @return true if window scrolling is enabled
   */
  public boolean getEnableScroll() {
    return enableScroll;
  }

  /**
   * The loading panel id.
   * 
   * @return the id
   */
  public String getLoadingPanelId() {
    return loadingPanelId;
  }

    @Override
  public void onAttach() {
    setEnableScroll(enableScroll);
    setSize(Window.getClientWidth() - clutterWidth, Window.getClientHeight() - clutterHeight);
    super.onAttach();
    GXT.hideLoadingPanel(loadingPanelId);
  }

  /**
   * Sets whether window scrolling is enabled.
   * 
   * @param enableScroll the window scroll state
   */
  public void setEnableScroll(boolean enableScroll) {
    this.enableScroll = enableScroll;
    Window.enableScrolling(enableScroll);
  }

  /**
   * The element id of the loading panel which will be hidden when the viewport
   * is attached (defaults to 'loading').
   * 
   * @param loadingPanelId the loading panel element id
   */
  public void setLoadingPanelId(String loadingPanelId) {
    this.loadingPanelId = loadingPanelId;
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, 0);
  }

  @Override
  protected void onWindowResize(int width, int height) {
    setSize(width - clutterWidth, height - clutterHeight);
  }

}
