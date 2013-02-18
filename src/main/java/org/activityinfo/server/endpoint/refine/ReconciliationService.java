package org.activityinfo.server.endpoint.refine;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.database.hibernate.entity.AdminLevel;
import org.activityinfo.server.util.logging.LogSlow;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.sun.jersey.api.json.JSONWithPadding;
import com.sun.jersey.api.view.Viewable;


/**
 * End-point for Open Refine (previously Google Refine) reconciliation
 * service. This is allows you to use Google Refine to match existing data
 * to administrative entities and locations within ActivityInfo's geographic
 * module and import that data into AI.
 *
 * @see https://github.com/OpenRefine/OpenRefine/wiki/Reconciliation-Service-Api
 */
@Path("/reconcile")
public class ReconciliationService {

	private Provider<EntityManager> entityManager;
	
	
	@Inject
	public ReconciliationService(Provider<EntityManager> entityManager) {
		super();
		this.entityManager = entityManager;
	}

	/**
	 * 
	 * @param callback the name of the javascript callback
	 * @return basic metadata about this service
	 */
	@GET
	@Produces("application/x-javascript")
	public Response getMetadata(@Context UriInfo uri, @QueryParam("callback") String callback) {
		ServiceDescription description = new ServiceDescription(uri.getBaseUri());

		if(Strings.isNullOrEmpty(callback)) {
			return Response.ok(description).type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.ok(new JSONWithPadding(description, callback)).type("application/x-javascript").build();
		}
	}

	@POST
	@LogSlow(threshold = 100)
	@Produces(MediaType.APPLICATION_JSON)
	public Object reconcile(MultivaluedMap<String, String> formParams) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		if(!formParams.get("queries").isEmpty()) {
			Map<String, Query> batch = mapper.readValue(formParams.getFirst("queries"),
					new TypeReference<Map<String, Query>>() {});
			return batchReconcile(batch);
		}
		System.out.println(formParams.toString());
		return Response.ok().build();
	}
	
	private Map<String, QueryResponse> batchReconcile(Map<String, Query> batch) throws JsonParseException, JsonMappingException, IOException {
		Map<String, QueryResponse> response = Maps.newHashMap();
		for(Map.Entry<String, Query> entry : batch.entrySet()) {
			response.put(entry.getKey(), reconcile(entry.getValue()));
		}
		return response;
	}
	
	public QueryResponse reconcile(Query query) {
		
		String queryText = QueryUtils.cleanupQuery(query.getQuery());
		
		if(query.getQuery().toLowerCase().contains("nioro")) {
			System.out.println("STOP!");
		}
		
		List<AdminEntity> entities;
		if(Strings.isNullOrEmpty(query.getType())) {
			// for type-less queries, comparing each admin entity
			// by levenshtein distance is too much, so we'll use the
			// metaphone index
			entities = searchBySound(queryText);
		} else {
			// if we have a type (adminlevel) then we compare all 
			// using edit distance
			int levelId = adminLevelIdFromType(query.getType());
			entities = queryByLevel(levelId);			
		}
		
		List<Match> matches = match(query, queryText, entities);
				
		Collections.sort(matches, Ordering.natural().reverse());
		if(query.hasLimit()) {
			while(matches.size() > query.getLimit()) {
				matches.remove(matches.size() - 1);
			}
		}

		return new QueryResponse(matches);
		
	}

	private List<AdminEntity> queryByLevel(int levelId) {
		List<AdminEntity> entities;
		entities = entityManager.get()
					.createQuery("select e from AdminEntity e where e.level.id = :levelId")
					.setHint("org.hibernate.cacheable", true)
					.setParameter("levelId", levelId)
					.getResultList();
		return entities;
	}

	private List<AdminEntity> searchBySound(String queryText) {
		DoubleMetaphone encoder = new DoubleMetaphone();
		String metaphone = encoder.doubleMetaphone(queryText);
		
		List<AdminEntity> entities = entityManager.get()
		.createQuery("select e from AdminEntity e where e.soundex = :soundex")
		.setParameter("soundex", metaphone)
		.getResultList();
		return entities;
	}
	
	private List<Match> match(Query query, String queryText, List<AdminEntity> entities) {
		List<Match> matches = Lists.newArrayList();
		for(AdminEntity entity : entities) {
			MatchType type = typeOf(entity);
			if(Strings.isNullOrEmpty(query.getType()) || type.getId().equals(query.getType())) {	
				Match match = new Match();
				match.setId("/adminUnit/" + entity.getId());
				match.setName(entity.getName());
				match.setType(Lists.newArrayList(type));
				match.setMatch(entity.getName().equalsIgnoreCase(queryText));
				match.setScore(new Scorer(query).score(entity));

				matches.add(match);
			}
		}
		return matches;
	}

	private int adminLevelIdFromType(String type) {
		if(!type.startsWith("adminUnitLevel/")) {
			throw new IllegalArgumentException(type);
		}
		return Integer.parseInt(type.substring("adminUnitLevel/".length()));
	}

	private MatchType typeOf(AdminEntity entity) {
		return new MatchType("adminUnitLevel/" + entity.getLevel().getId(),
				String.format("%s: %s", entity.getLevel().getCountry().getName(),
						entity.getLevel().getName()));
	}

	@GET
	@Path("suggest")
	@Produces("application/x-javascript")
	public JSONWithPadding suggestProperty(
			@QueryParam("callback") String callback, 
			@QueryParam("prefix") String prefix,
			@QueryParam("type_strict") String typeStrict, 
			@QueryParam("all_types") String allTypes, 
			@QueryParam("schema") String schema) {
		// http://1.refine-helper.stefanomazzocchi.user.dev.freebaseapps.com/
		// suggest_property?callback=jsonp1359985511537
		// &prefix=c
		// &type_strict=any
		// &all_types=false
		// &schema=%2Flocation%2Fadministrative_division
		
		SuggestPropertyResponse response = new SuggestPropertyResponse();
		response.setPrefix(prefix);
		
		for(PropertyDescription property : queryProperties(schema)) {
			if(property.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
				response.addResult(property);
			}
		}
		
		return new JSONWithPadding(response);
	}

	private List<PropertyDescription> queryProperties(String schema) {
		if(schema.startsWith("adminUnitLevel/")) {
			int adminLevelId = Integer.parseInt(schema.substring("adminUnitLevel/".length()));
			return queryAdminLevelProperties(adminLevelId);
		} else {
			return Collections.emptyList();
		}
	}

	private List<PropertyDescription> queryAdminLevelProperties(int adminLevelId) {
		List<PropertyDescription> properties = Lists.newArrayList();
		
		properties.add(new PropertyDescription("/admin/country_name", "Country"));
		
		AdminLevel level = entityManager.get().find(AdminLevel.class, adminLevelId);
		AdminLevel parent = level.getParent();
		while(parent != null) {
			PropertyDescription parentEntityName = new PropertyDescription("/admin/level/" + parent.getId() + "/name", 
					parent.getName() + " Name");
			properties.add(parentEntityName);
		}
		
		return properties;
		
	}
	
	@GET
	@Path("preview/adminUnit/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Viewable preview(@PathParam("id") int id) {
		return new Viewable("/preview/AdminEntity.ftl", 
				new AdminEntityPreview(entityManager.get().find(AdminEntity.class, id))); 
	}

}
