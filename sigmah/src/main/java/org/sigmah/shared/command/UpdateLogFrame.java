package org.sigmah.shared.command;

import org.sigmah.shared.command.result.LogFrameResult;
import org.sigmah.shared.dto.logframe.LogFrameDTO;

/**
 * Update log frame command.
 * 
 * @author tmi
 * 
 */
public class UpdateLogFrame implements Command<LogFrameResult> {

    private static final long serialVersionUID = -2882962376539056127L;

    private LogFrameDTO logFrame;
    private int projectId;

    public UpdateLogFrame() {
        // serialization
    }

    public UpdateLogFrame(LogFrameDTO logFrame, int projectId) {
        this.logFrame = logFrame;
        this.projectId = projectId;
    }

    public LogFrameDTO getLogFrame() {
        return logFrame;
    }

    public void setLogFrame(LogFrameDTO logFrame) {
        this.logFrame = logFrame;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
