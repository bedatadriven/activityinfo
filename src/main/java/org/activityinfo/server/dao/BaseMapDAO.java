package org.activityinfo.server.dao;

import org.activityinfo.shared.map.BaseMap;

import java.util.List;
/*
 * @author Alex Bertram
 */

public interface BaseMapDAO {

  

       
    BaseMap getBaseMap(String id);
    
    List<BaseMap> getBaseMaps();



}
