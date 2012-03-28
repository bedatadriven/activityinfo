package org.sigmah.shared.command.handler.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.handler.search.QueryChecker.QueryFail.EmptyQuery;
import org.sigmah.shared.command.handler.search.QueryChecker.QueryFail.TooShortQuery;

import com.google.common.base.Strings;

/** Checks a search query for failure and obvious stuff a user can do wrong */
public class QueryChecker {
	private List<QueryFail> fails = new ArrayList<QueryFail>();
	private String query;
		
	/** True when query is OK */
	public boolean checkQuery(String query) {
		this.query=query;
		
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
			
			public String fail() {
				return reason;
			};
		}
		
		public static final class EmptyQuery extends Abstract{
			public EmptyQuery() {
				super(I18N.MESSAGES.searchQueryEmpty());
			}
		}
		
		public static final class TooShortQuery extends Abstract{
			public TooShortQuery() {
				super(I18N.MESSAGES.searchQueryTooShort());
			}
		}
		
	}
}
