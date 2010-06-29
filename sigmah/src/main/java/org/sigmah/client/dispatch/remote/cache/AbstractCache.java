package org.sigmah.client.dispatch.remote.cache;

import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;

import java.util.*;

/**
 * Provides a default, in-memory, command caching implementation based on
 * command equality.
 * <p/>
 * To create a subclass:
 * <p/>
 * 1. make sure the command which you are going to cache
 * implements equals() <u>and</u> hashCode() (press ALT+INS in IntelliJ to
 * generate these methods automatically)
 * <p/>
 * 2. Your subclass constructor should accept Dispatcher as a parameter
 * and use the reference to register itself
 * <p/>
 * 3. Add your subclass to AppModule as an Eager Singleton
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class AbstractCache {

    /**
     * Internal data structure that keeps track of commands and their
     * results, as well as statistics on cache usage that can potentially
     * be used to clean the cache.
     */
    protected class CacheEntry {

        public CacheEntry(CommandResult result) {
            dateCached = new Date();
            hits = 0;
            this.result = result;
        }

        /**
         * The date when the result was received from the server
         */
        private Date dateCached;

        /**
         * The number of times this result has been accessed
         * since being cached.
         */
        private int hits;

        /**
         * The command result
         */
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

        /**
         * Increments the hit count. See the hits field
         */
        public void hit() {
            hits++;
        }
    }

    /**
     * Maps commands to their results
     */
    private final Map<Command, CacheEntry> results = new HashMap<Command, CacheEntry>();

    /**
     * Tracks the order in which results were cached
     */
    private final List<Command> cache = new LinkedList<Command>();

    /**
     * Adds a command and its result to the cache
     *
     * @param cmd
     * @param result
     */
    protected void cache(Command cmd, CommandResult result) {

        cache.remove(cmd);
        cache.add(cmd);

        results.put(cmd, new CacheEntry(result));
    }

    /**
     * Attempts to retrieve the results of a cached command
     *
     * @param command
     * @return The result originally returned by the server or null if there
     *         is no matching cache entry
     */
    protected CommandResult fetch(Command command) {

        CacheEntry entry = results.get(command);
        if (entry != null) {
            entry.hit();
            return entry.getResult();
        } else {
            return null;
        }
    }

    /**
     * @return A set of all cached commands and their results/statistics
     */
    protected Set<Map.Entry<Command, CacheEntry>> getCacheEntries() {
        return results.entrySet();
    }
}
