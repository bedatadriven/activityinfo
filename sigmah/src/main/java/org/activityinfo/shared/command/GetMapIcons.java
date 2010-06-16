package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.MapIconResult;

/**
 * Returns a list of available {@link org.activityinfo.shared.report.model.MapIcon}s on the
 * server
 *
 * @author Alex Bertram
 */
public class GetMapIcons extends GetListCommand<MapIconResult> {

    public GetMapIcons() {
    }
}
