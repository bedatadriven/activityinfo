package org.activityinfo.shared.map;
/*
 * @author Alex Bertram
 */

public class LocalBaseMap extends BaseMap {

    private int requestIndex;
    private int version;
    private String tileRoot;


    public int getRequestIndex() {
        return requestIndex;
    }

    public void setRequestIndex(int requestIndex) {
        this.requestIndex = requestIndex;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTileRoot() {
        return tileRoot;
    }

    public void setTileRoot(String tileRoot) {
        this.tileRoot = tileRoot;
    }

    @Override
    public String getTileUrl(int zoom, int x, int y) {
        int server = (requestIndex++)%4;
        StringBuilder sb = new StringBuilder();
        sb.append("http://mt").append(server).append(".activityinfo.org/tiles/")
                .append(getId()).append("/v").append(version).append("/z")
                .append(zoom).append("/").append(x).append("x").append(y).append(".png");

        return sb.toString();
    }

    @Override
    public String getLocalTilePath(int zoom, int x, int y) {
        StringBuilder sb = new StringBuilder();
        sb.append(tileRoot).append("/").append(getId())
                .append("/v").append(version).append("/z")
                .append(zoom).append("/").append(x).append("x").append(y).append(".png");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof LocalBaseMap))
            return false;
        LocalBaseMap other = (LocalBaseMap)obj;

        return getId() == other.getId() && version == other.getVersion();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
