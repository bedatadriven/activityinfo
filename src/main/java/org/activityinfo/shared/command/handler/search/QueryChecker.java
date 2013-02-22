package org.activityinfo.shared.command.handler.search;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.handler.search.QueryChecker.QueryFail.EmptyQuery;

import com.google.common.base.Strings;

/** Checks a search query for failure and obvious stuff a user can do wrong */
public class QueryChecker {
    private List<QueryFail> fails = new ArrayList<QueryFail>();
    private String query;

    /** True when query is OK */
    public boolean checkQuery(String query) {
        this.query = query;

        checkIsEmpty();

        return fails.isEmpty();
    }

    private void checkIsEmpty() {
        if (Strings.isNullOrEmpty(query)) {
            fails.add(new EmptyQuery());
        }
    }

    public List<QueryFail> getFails() {
        return fails;
    }

    public interface QueryFail extends Serializable {
        public String fail();

        public abstract class Abstract implements QueryFail {
            private String reason;

            public Abstract(String reason) {
                super();
                this.reason = reason;
            }

            @Override
            public String fail() {
                return reason;
            };
        }

        public static final class EmptyQuery extends Abstract {
            public EmptyQuery() {
                super(I18N.MESSAGES.searchQueryEmpty());
            }
        }

        public static final class TooShortQuery extends Abstract {
            public TooShortQuery() {
                super(I18N.MESSAGES.searchQueryTooShort());
            }
        }

    }
}
