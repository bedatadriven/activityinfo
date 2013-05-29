package org.activityinfo.shared.util.mapping;

public class PolylineEncoded {
    private String points;
    private String levels;

    public PolylineEncoded(String points, String levels) {
        super();
        this.points = points;
        this.levels = levels;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

}
