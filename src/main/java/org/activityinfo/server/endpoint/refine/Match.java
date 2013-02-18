package org.activityinfo.server.endpoint.refine;

import java.util.List;

class Match implements Comparable<Match> {
	private String id;
	private String name;
	private List<MatchType> type;
	private double score;
	private boolean match;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MatchType> getType() {
		return type;
	}
	public void setType(List<MatchType> type) {
		this.type = type;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public boolean isMatch() {
		return match;
	}
	public void setMatch(boolean match) {
		this.match = match;
	}
	@Override
	public int compareTo(Match o) {
		return Double.compare(score, o.score);
	}
	
	
}
