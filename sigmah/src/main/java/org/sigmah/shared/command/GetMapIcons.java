package org.sigmah.shared.command;

import org.sigmah.shared.command.result.MapIconResult;

/**
 * Returns a list of available {@link org.sigmah.shared.report.model.MapIcon}s on the
 * server
 *
 * @author Alex Bertram
 */
public class GetMapIcons extends GetListCommand<MapIconResult> {

    public GetMapIcons() {
    }
}
