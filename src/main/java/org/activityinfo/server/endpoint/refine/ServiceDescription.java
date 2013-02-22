package org.activityinfo.server.endpoint.refine;

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
        suggest.setProperty(UriBuilder.fromUri(baseUri).path("reconcile")
            .path("suggest").build());
        preview.setUrl(UriBuilder.fromUri(baseUri).path("reconcile")
            .path("preview").build().toString()
            + "{{id}}");
        preview.setWidth(350);
        preview.setHeight(200);
        view = new ViewServiceDescription(UriBuilder.fromUri(baseUri)
            .path("resources").build().toString()
            + "{{id}}");
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
