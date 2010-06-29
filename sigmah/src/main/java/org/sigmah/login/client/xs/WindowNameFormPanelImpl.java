/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.login.client.xs;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.impl.FormPanelImpl;
import com.google.gwt.user.client.ui.impl.FormPanelImplHost;

/**
 * See http://development.lombardi.com/?p=611
 */
public class WindowNameFormPanelImpl extends FormPanelImpl {

    
    @Override
    public native String getContents(Element iframe) /*-{
        try {
          // Make sure the iframe's window is loaded.
          if (!iframe.contentWindow)
            return null;

          // Get the contents from the window.name property.
          var data = iframe.contentWindow.name;

          // restore the original name of the window so subsequent form
          // submissions will be loaded here
          iframe.contentWindow.name = iframe.name;

          return data;
        } catch (e) {
          return null;
        }
      }-*/;

    @Override
    public native void hookEvents(Element iframe, Element form,
      FormPanelImplHost listener) /*-{
    if (iframe) {
      iframe.onload = function() {
        // If there is no __formAction yet, this is a spurious onload
        // generated when the iframe is first added to the DOM.
        if (!iframe.__formAction)
          return;

        if (!iframe.__sameDomainRestored) {
          iframe.__sameDomainRestored = true;
          iframe.contentWindow.location = "about:blank";
          return;
        }
        listener.@com.google.gwt.user.client.ui.impl.FormPanelImplHost::onFrameLoad()();
      };
    }

    form.onsubmit = function() {
      // Hang on to the form's action url, needed in the
      // onload/onreadystatechange handler.
      if (iframe) {
        iframe.__formAction = form.action;
        iframe.__sameDomainRestored = false;
      }
      return listener.@com.google.gwt.user.client.ui.impl.FormPanelImplHost::onFormSubmit()();
    };
  }-*/;

}
