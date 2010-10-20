package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.logframe.LogFrameDTO;

/**
 * A log frame.
 * 
 * @author tmi
 * 
 */
public class LogFrameResult implements CommandResult {

    private static final long serialVersionUID = -9082324685553248367L;

    private LogFrameDTO logFrame;

    public LogFrameResult() {
    }

    public LogFrameResult(LogFrameDTO logFrame) {
        this.logFrame = logFrame;
    }

    public LogFrameDTO getLogFrame() {
        return logFrame;
    }

    public void setLogFrame(LogFrameDTO logFrame) {
        this.logFrame = logFrame;
    }
}
