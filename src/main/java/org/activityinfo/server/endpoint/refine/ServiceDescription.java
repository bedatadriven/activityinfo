package org.activityinfo.server.endpoint.refine;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.Lists;

/**
 * Describes the reconciliation service
 *
 */
public class ServiceDescription {
	
	public class SuggestServices {
		private String property;

		public String getProperty() {
			return property;
		}

		public void setProperty(URI uri) {
			this.property = uri.toString();
		}
		
	}
	
	private String name = "ActivityInfo reconciliation";
	private String identifierSpace = "http://www.activityinfo.org/ns/admin";
	private String schemaSpace = "http://www.activityinfo.org/ns/type.object.id";
	
	private SuggestServices suggest = new SuggestServices();
	private PreviewService preview = new PreviewService();
	private ViewServiceDescription view;
	
	private List<MatchType> defaultTypes = Lists.newArrayList();
		
	public ServiceDescription(URI baseUri) {
		defaultTypes.add(new MatchType("admin", "Administrative Entity"));
		suggest.setProperty( UriBuilder.fromUri(baseUri).path("reconcile").path("suggest").build() );
		preview.setUrl( UriBuilder.fromUri(baseUri).path("reconcile").path("preview").build().toString() + "{{id}}" );
		preview.setWidth(350);
		preview.setHeight(200);
		view = new ViewServiceDescription( UriBuilder.fromUri(baseUri).path("resources").build().toString() + "{{id}}" );
	}
		
	public String getName() {
		return name;
	}
	public String getIdentifierSpace() {
		return identifierSpace;
	}
	public String getSchemaSpace() {
		return schemaSpace;
	}

	public List<MatchType> getDefaultTypes() {
		return defaultTypes;
	}

	public SuggestServices getSuggest() {
		return suggest;
	}

	public PreviewService getPreview() {
		return preview;
	}

	public ViewServiceDescription getView() {
		return view;
	}

}
