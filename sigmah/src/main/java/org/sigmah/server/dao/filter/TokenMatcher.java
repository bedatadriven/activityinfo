package org.sigmah.server.dao.filter;

import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public interface TokenMatcher {

    public MatchResult match(List<String> tokens, int startIndex);

    void init(List<String> tokens);
}
