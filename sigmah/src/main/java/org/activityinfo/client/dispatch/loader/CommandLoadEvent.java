package org.activityinfo.client.dispatch.loader;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Command;

public class CommandLoadEvent extends LoadEvent {

    private BatchCommand batch = new BatchCommand();

    public CommandLoadEvent(Loader<?> loader) {
        super(loader);
    }

    public CommandLoadEvent(Loader<?> loader, Object config) {
        super(loader, config);
    }

    public CommandLoadEvent(Loader<?> loader, Object config, Object data) {
        super(loader, config, data);
    }

    public CommandLoadEvent(Loader<?> loader, Object config, Throwable t) {
        super(loader, config, t);
    }

    public BatchCommand getBatch() {
        return batch;
    }

    public void addCommandToBatch(Command command) {
        batch.add(command);
    }

}
