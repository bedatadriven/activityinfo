package org.activityinfo.server.endpoint.refine;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.apache.commons.lang.StringUtils;

public class Scorer {
	private double distance = 0; 
	private double maxPossibleDistance = 0;
	private Query query;
	
	public Scorer(Query query) {
		this.query = query;
	}
	
	private void score(String expectedText, String queryText) {
		String strippedQueryText = QueryUtils.cleanupQuery(queryText.toLowerCase());
		distance += StringUtils.getLevenshteinDistance(expectedText.toLowerCase(), strippedQueryText);
		maxPossibleDistance += Math.max(expectedText.length(), strippedQueryText.length());
	}
	
	public double score(AdminEntity entity) {
		score(entity.getName(), query.getQuery());
		
		for(PropertyValue property : query.getProperties()) {
			if(isAdminUnitProperty(property)) {
				scoreLevel(entity, property);
			}
		}
		return score();
	}

	private void scoreLevel(AdminEntity entity, PropertyValue property) {
		AdminEntity parent = entity.getParent();
		while(parent != null) {
			if(parent.getLevel().getId() == levelIdFromProperty(property)) {
				score(parent.getName(), property.stringValue());
			}
			parent = parent.getParent();
		}
	}

	private boolean isAdminUnitProperty(PropertyValue property) {
		return property.getPropertyId().startsWith("adminUnit/");
	}
	
	private int levelIdFromProperty(PropertyValue property) {
		return Integer.parseInt(property.getPropertyId().substring("adminUnit/".length()));
	}
	
	public double score() {
		if(maxPossibleDistance == 0) {
			return 0;
		}
		return (maxPossibleDistance-distance) / maxPossibleDistance;
	}
}
