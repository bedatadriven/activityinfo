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

public class Grid {
    @Inject
    protected final Provider<EntityManager> entityManager = null;

    private String title;
    private String hint;
    private List<String> headers;
    private String query;
    private List<Object> params;

    public Grid() {
    }

    public Grid setTitle(String title) {
        this.title = title;
        return this;
    }

    public Grid setHint(String hint) {
        this.hint = hint;
        return this;
    }

    public Grid setHeaders(String... headers) {
        return setHeaders(Lists.newArrayList(headers));
    }

    public Grid setHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    public Grid setQuery(String query) {
        this.query = query;
        return this;
    }

    public Grid setParams(Object... params) {
        return setParams(Lists.newArrayList(params));
    }

    public Grid setParams(List<Object> params) {
        this.params = params;
        return this;
    }

    public Viewable asViewable() throws Exception {
        assert (title != null);
        assert (headers != null);
        assert (query != null);

        Map<String, Object> map = Maps.newHashMap();
        map.put("title", title);
        map.put("hint", hint);
        map.put("headers", headers);

        Query q = entityManager.get().createNativeQuery(query);

        if (params != null) {
            for (int i = 1; i <= params.size(); i++) {
                q.setParameter(i, params.get(i - 1));
            }
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        map.put("rows", rows);

        return new Viewable("/adminreport/Grid.ftl", map);
    }
}
