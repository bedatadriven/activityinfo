package org.activityinfo.client.command.cache;

import org.activityinfo.client.command.CommandProxyResult;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;

import java.util.*;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AbstractCache  {

    protected class CacheEntry {

        public CacheEntry(CommandResult result) {
            dateCached = new Date();
            hits = 0;
            this.result = result;
        }

        private Date dateCached;
        private int hits;
        private CommandResult result;

        public Date getDateCached() {
            return dateCached;
        }

        public int getHits() {
            return hits;
        }

        public CommandResult getResult() {
            return result;
        }

        public void hit() {
            hits++;
        }
    }

    private final Map<Command, CacheEntry> results = new HashMap<Command, CacheEntry>();

    /**
     * Tracks the order in which results were cached
     */
    private final List<Command> cache = new LinkedList<Command>();

    protected void cache(Command cmd, CommandResult result) {

        cache.remove(cmd);
        cache.add(cmd);

        results.put(cmd, new CacheEntry(result));
    }

    protected CommandResult fetch(Command command) {

        CacheEntry entry = results.get(command);
        if(entry != null) {
            entry.hit();
            return entry.getResult();
        } else {
            return null;
        }
    }

    protected Set<Map.Entry<Command, CacheEntry>> getCacheEntries() {
        return results.entrySet();
    }
}
