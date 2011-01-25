/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.dto.logframe.LogFrameDTO;

/**
 * Ask the server to replace a log frame by an other one.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class CopyLogFrame implements Command<LogFrameDTO> {
    /**
     * ID of the log frame to copy.
     */
    private int sourceId;
    /**
     * ID of the project of destination.
     */
    private int destinationId;

    public CopyLogFrame() {
    }

    public CopyLogFrame(int sourceId, int destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
}
