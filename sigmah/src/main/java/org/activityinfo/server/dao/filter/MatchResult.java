package org.activityinfo.server.dao.filter;

import org.hibernate.criterion.Criterion;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MatchResult {

    private final int tokenCount;
    private final Criterion criterion;

    public MatchResult(int tokenCount, Criterion criterion) {
        this.tokenCount = tokenCount;
        this.criterion = criterion;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public Criterion getCriterion() {
        return criterion;
    }
}
