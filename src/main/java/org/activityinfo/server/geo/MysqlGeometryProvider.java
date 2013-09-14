package org.activityinfo.server.geo;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;

import com.google.common.collect.Lists;

public class MysqlGeometryProvider implements AdminGeometryProvider {

    private final Provider<EntityManager> em;
    
    @Inject
    public MysqlGeometryProvider(Provider<EntityManager> em) {
        super();
        this.em = em;
    }

    @Override
    public List<AdminGeo> getGeometries(int adminLevelId) {
        List<AdminEntity> entityList = em.get()
            .createQuery("select e from AdminEntity e where e.level.id = :levelId")
            .setParameter("levelId", adminLevelId)
            .getResultList();
        
        List<AdminGeo> resultList = Lists.newArrayList();
        for(AdminEntity entity : entityList) {
            resultList.add(new AdminGeo(entity.getId(), entity.getGeometry()));
        }
        return resultList;    
    }

}
