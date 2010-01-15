package org.activityinfo.server.dao;

import com.google.inject.ImplementedBy;
import org.activityinfo.shared.map.BaseMap;

import java.util.List;

@ImplementedBy(BaseMapDAOImpl.class)
public interface BaseMapDAO {


    BaseMap getBaseMap(String id);

    List<BaseMap> getBaseMaps();


}
