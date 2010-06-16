package org.activityinfo.shared.command.result;

import java.util.List;

/**
 *
 * @author Alex Bertram
 */
public class BatchResult implements CommandResult {

    private List<CommandResult> results;

    public BatchResult() {
    }

    public BatchResult(List<CommandResult> results) {
        this.results = results;
    }

    public List<CommandResult> getResults() {
        return results;
    }

    public void setResults(List<CommandResult> results) {
        this.results = results;
    }

    public CommandResult getLastResult() {
        return results.get(results.size()-1);    
    }
}
