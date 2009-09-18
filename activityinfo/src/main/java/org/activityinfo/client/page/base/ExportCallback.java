package org.activityinfo.client.page.base;

import org.activityinfo.shared.command.RenderElement;

public interface ExportCallback {

    public void export(RenderElement.Format format);
}
