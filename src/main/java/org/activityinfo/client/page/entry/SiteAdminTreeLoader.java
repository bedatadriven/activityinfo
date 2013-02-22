package org.activityinfo.client.page.entry;

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

import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.entry.grouping.AdminGroupingModel;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Loads sites grouped by a set of AdminLevels
 */
class SiteAdminTreeLoader extends BaseTreeLoader<ModelData> implements
    SiteTreeLoader {

    private TreeProxy treeProxy;

    public SiteAdminTreeLoader(Dispatcher dispatcher,
        AdminGroupingModel groupingModel) {
        super(new TreeProxy(dispatcher));
        treeProxy = (TreeProxy) proxy;
        setAdminLeafLevelId(groupingModel.getAdminLevelId());
    }

    public void setAdminLeafLevelId(int leafLevelId) {
        treeProxy.setAdminLeafLevelId(leafLevelId);
    }

    @Override
    public void setFilter(Filter filter) {
        treeProxy.setFilter(filter);
    }

    @Override
    public boolean hasChildren(ModelData parent) {
        return parent instanceof AdminEntityDTO;
    }

    @Override
    public String getKey(ModelData model) {
        if (model instanceof AdminEntityDTO) {
            return "A" + ((AdminEntityDTO) model).getId();
        } else if (model instanceof SiteDTO) {
            return "S" + ((SiteDTO) model).getId();
        } else {
            return "X" + model.hashCode();
        }
    }

    private static class TreeProxy extends RpcProxy<List<ModelData>> {

        private final Dispatcher dispatcher;
        private List<Integer> adminLevelIds = null;
        private int leafLevelId;
        private Filter filter;

        public TreeProxy(Dispatcher dispatcher) {
            super();
            this.dispatcher = dispatcher;
        }

        public void setFilter(Filter filter) {
            this.filter = filter;
        }

        public void setAdminLeafLevelId(int leafLevelId) {
            this.leafLevelId = leafLevelId;
            this.adminLevelIds = null;
        }

        @Override
        protected void load(Object parentNode,
            AsyncCallback<List<ModelData>> callback) {

            if (adminLevelIds == null) {
                loadAdminLevels(parentNode, callback);
            } else {
                loadNodes(parentNode, callback);
            }
        }

        private void loadAdminLevels(final Object parentNode,
            final AsyncCallback<List<ModelData>> callback) {

            dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(SchemaDTO result) {
                    try {
                        initLevels(result);
                        loadNodes(parentNode, callback);
                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                }
            });
        }

        private void initLevels(SchemaDTO result) {
            CountryDTO country = result.getCountryByAdminLevelId(leafLevelId);
            adminLevelIds = Lists.newArrayList();
            for (AdminLevelDTO level : country
                .getAdminLevelAncestors(leafLevelId)) {
                adminLevelIds.add(level.getId());
            }
        }

        private void loadNodes(Object parentNode,
            AsyncCallback<List<ModelData>> callback) {
            if (parentNode == null) {
                GetAdminEntities query = new GetAdminEntities(
                    adminLevelIds.get(0));
                query.setFilter(filter);

                loadAdminEntities(query, callback);

            } else if (parentNode instanceof AdminEntityDTO) {
                AdminEntityDTO parentEntity = (AdminEntityDTO) parentNode;
                int depth = adminLevelIds.indexOf(parentEntity.getLevelId());
                if (depth + 1 < adminLevelIds.size()) {
                    loadChildAdminLevels(parentEntity,
                        adminLevelIds.get(depth + 1), callback);
                } else {
                    loadSites(parentEntity, callback);
                }
            }
        }

        private void loadChildAdminLevels(AdminEntityDTO parentEntity,
            int adminLevelId,
            AsyncCallback<List<ModelData>> callback) {

            GetAdminEntities query = new GetAdminEntities();
            query.setLevelId(adminLevelId);
            query.setParentId(parentEntity.getId());
            query.setFilter(filter);

            loadAdminEntities(query, callback);
        }

        private void loadAdminEntities(GetAdminEntities query,
            final AsyncCallback<List<ModelData>> callback) {
            dispatcher.execute(query, new AsyncCallback<AdminEntityResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(AdminEntityResult result) {
                    callback.onSuccess((List) result.getData());
                }
            });
        }

        private void loadSites(AdminEntityDTO parentEntity,
            final AsyncCallback<List<ModelData>> callback) {

            Filter childFilter = new Filter(filter);
            childFilter.addRestriction(DimensionType.AdminLevel,
                parentEntity.getId());

            GetSites siteQuery = new GetSites();
            siteQuery.setFilter(childFilter);
            siteQuery.setSortInfo(new SortInfo("locationName", SortDir.ASC));

            dispatcher.execute(siteQuery, new AsyncCallback<SiteResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(SiteResult result) {
                    callback.onSuccess((List) result.getData());
                }
            });
        }
    }

}