package org.activityinfo.server.dao;

import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.map.LocalBaseMap;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
/*
 * @author Alex Bertram
 */

public class BaseMapDAOImpl implements BaseMapDAO {

    private Map<String, BaseMap> baseMaps;

    public BaseMapDAOImpl() {
        baseMaps = new HashMap<String, BaseMap>();

        File tileRoot = new File("e://tiles");
        if(!tileRoot.exists()) {
            tileRoot = new File("c://tiles"); // for the development machine
        }

        for(File tileSet : tileRoot.listFiles()) {
            if(tileSet.isDirectory()) {

                LocalBaseMap baseMap = new LocalBaseMap();
                baseMap.setId(tileSet.getName());
                baseMap.setVersion(getLatestVersion(tileSet));
                baseMap.setMinZoom(Integer.MAX_VALUE);
                baseMap.setMaxZoom(Integer.MIN_VALUE);

                baseMap.setTileRoot(tileRoot.getAbsolutePath());

                for(String file : tileSet.list()) {
                    if(file.endsWith(".name")) {
                        baseMap.setName(file.substring(0, file.length()-5));
                    } else if(file.endsWith(".copyright")) {
                        baseMap.setCopyright(file.substring(0, file.length()-".copyright".length()));
                    }
                }

                if(baseMap.getVersion() != -1) {

                    File versionFolder = new File(tileSet.getAbsolutePath() + "/v" + baseMap.getVersion());
                    for(File zoomLevel : versionFolder.listFiles()) {
                        if(zoomLevel.isDirectory() && zoomLevel.getName().startsWith("z")) {
                            int level = Integer.parseInt(zoomLevel.getName().substring(1));
                            if(level > baseMap.getMaxZoom()) {
                                baseMap.setMaxZoom(level);
                            }
                            if(level < baseMap.getMinZoom())  {
                                baseMap.setMinZoom(level);
                            }
                        }
                    }

                    baseMaps.put(baseMap.getId(), baseMap);
                }
            }
        }

    }

    private int getLatestVersion(File tileSet) {
        int latestVersion = -1;
        for(File file : tileSet.listFiles()) {
            if(file.isDirectory() && file.getName().startsWith("v")) {
                int version = Integer.parseInt(file.getName().substring(1));
                if(version > latestVersion)
                    latestVersion = version;
            }
        }
        return latestVersion;
    }

    public BaseMap getBaseMap(String id) {
        return baseMaps.get(id);
    }

    public List<BaseMap> getBaseMaps() {
        return new ArrayList<BaseMap>(baseMaps.values());
    }
}
