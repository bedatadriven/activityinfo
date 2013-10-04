package org.activityinfo.server.endpoint.adminreport;

import java.util.List;
import java.util.Map;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

public class AdminReport {
    @Inject
    protected final Provider<EntityManager> entityManager = null;

    private String title;
    private List<String> headers;
    private String query;

    public AdminReport() {
    }

    public AdminReport setTitle(String title) {
        this.title = title;
        return this;
    }

    public AdminReport setHeaders(String... headers) {
        return setHeaders(Lists.newArrayList(headers));
    }

    public AdminReport setHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    public AdminReport setQuery(String query) {
        this.query = query;
        return this;
    }

    public Viewable asViewable() throws Exception {
        assert (title != null);
        assert (headers != null);
        assert (query != null);

        Map<String, Object> map = Maps.newHashMap();
        map.put("title", title);
        map.put("headers", headers);

        Query q = entityManager.get().createNativeQuery(query);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        map.put("rows", rows);

        return new Viewable("/adminreport/AdminReport.ftl", map);
    }
}
